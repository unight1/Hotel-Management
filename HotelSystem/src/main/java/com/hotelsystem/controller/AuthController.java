package com.hotelsystem.controller;

import com.hotelsystem.dto.AuthRequest;
import com.hotelsystem.dto.AuthResponse;
import com.hotelsystem.dto.UserDto;
import com.hotelsystem.dto.GuestDto;
import com.hotelsystem.entity.User;
import com.hotelsystem.security.JwtUtil;
import com.hotelsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final com.hotelsystem.service.GuestService guestService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtUtil.generateToken(userDetails);
            
            // 返回统一格式的响应
            return ResponseEntity.ok(com.hotelsystem.dto.ApiResponse.success("登录成功", new AuthResponse(token)));
        } catch (Exception e) {
            return ResponseEntity.ok(com.hotelsystem.dto.ApiResponse.error("登录失败: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerGuest(@Valid @RequestBody GuestDto guestDto) {
        try {
            GuestDto created = guestService.createGuest(guestDto);
            return ResponseEntity.ok(created);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
