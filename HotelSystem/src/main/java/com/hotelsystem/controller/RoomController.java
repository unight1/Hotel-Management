package com.hotelsystem.controller;

import com.hotelsystem.dto.ApiResponse;
import com.hotelsystem.dto.RoomDto;
import com.hotelsystem.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomDto>>> getAllRooms() {
        List<RoomDto> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(ApiResponse.success(rooms));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomDto>> getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id)
                .map(room -> ResponseEntity.ok(ApiResponse.success(room)))
                .orElse(ResponseEntity.ok(ApiResponse.error("房间不存在")));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoomDto>> createRoom(@Valid @RequestBody RoomDto roomDto) {
        try {
            RoomDto createdRoom = roomService.createRoom(roomDto);
            return ResponseEntity.ok(ApiResponse.success("房间创建成功", createdRoom));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomDto>> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomDto roomDto) {
        try {
            RoomDto updatedRoom = roomService.updateRoom(id, roomDto);
            return ResponseEntity.ok(ApiResponse.success("房间更新成功", updatedRoom));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.ok(ApiResponse.success("房间删除成功", null));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<RoomDto>>> getAvailableRooms() {
        List<RoomDto> availableRooms = roomService.getAvailableRooms();
        return ResponseEntity.ok(ApiResponse.success(availableRooms));
    }

    @GetMapping("/type/{roomType}")
    public ResponseEntity<ApiResponse<List<RoomDto>>> getRoomsByType(@PathVariable String roomType) {
        List<RoomDto> rooms = roomService.getRoomsByType(roomType);
        return ResponseEntity.ok(ApiResponse.success(rooms));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<RoomDto>>> getActiveRooms() {
        List<RoomDto> activeRooms = roomService.getActiveRooms();
        return ResponseEntity.ok(ApiResponse.success(activeRooms));
    }
}
