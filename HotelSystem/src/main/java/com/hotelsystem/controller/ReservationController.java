package com.hotelsystem.controller;

import com.hotelsystem.dto.ApiResponse;
import com.hotelsystem.dto.ReservationDto;
import com.hotelsystem.entity.Reservation;
import com.hotelsystem.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationDto>>> getAllReservations() {
        List<ReservationDto> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(ApiResponse.success(reservations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationDto>> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(reservation -> ResponseEntity.ok(ApiResponse.success(reservation)))
                .orElse(ResponseEntity.ok(ApiResponse.error("预订不存在")));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationDto>> createReservation(@Valid @RequestBody ReservationDto reservationDto) {
        try {
            ReservationDto createdReservation = reservationService.createReservation(reservationDto);
            return ResponseEntity.ok(ApiResponse.success("预订创建成功", createdReservation));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationDto>> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody ReservationDto reservationDto) {
        try {
            ReservationDto updatedReservation = reservationService.updateReservation(id, reservationDto);
            return ResponseEntity.ok(ApiResponse.success("预订更新成功", updatedReservation));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservation(id);
            return ResponseEntity.ok(ApiResponse.success("预订删除成功", null));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<ApiResponse<List<ReservationDto>>> getReservationsByGuestId(@PathVariable Long guestId) {
        List<ReservationDto> reservations = reservationService.getReservationsByGuestId(guestId);
        return ResponseEntity.ok(ApiResponse.success(reservations));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<List<ReservationDto>>> getReservationsByRoomId(@PathVariable Long roomId) {
        List<ReservationDto> reservations = reservationService.getReservationsByRoomId(roomId);
        return ResponseEntity.ok(ApiResponse.success(reservations));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<ReservationDto>>> getReservationsByStatus(@PathVariable Reservation.ReservationStatus status) {
        List<ReservationDto> reservations = reservationService.getReservationsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(reservations));
    }

    @GetMapping("/checkin-range")
    public ResponseEntity<ApiResponse<List<ReservationDto>>> getReservationsByCheckInDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<ReservationDto> reservations = reservationService.getReservationsByCheckInDateRange(start, end);
        return ResponseEntity.ok(ApiResponse.success(reservations));
    }
}