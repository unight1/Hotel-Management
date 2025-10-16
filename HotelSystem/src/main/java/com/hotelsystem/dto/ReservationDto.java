package com.hotelsystem.dto;

import com.hotelsystem.entity.Reservation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReservationDto {
    private Long id;
    private String reservationNumber;

    @NotNull(message = "宾客ID不能为空")
    private Long guestId;

    @NotNull(message = "房间ID不能为空")
    private Long roomId;

    @NotNull(message = "入住日期不能为空")
    private LocalDate checkInDate;

    @NotNull(message = "离店日期不能为空")
    private LocalDate checkOutDate;

    private Integer numberOfGuests;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private Reservation.ReservationStatus status;
    private String specialRequests;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 关联信息（用于响应）
    private GuestDto guest;
    private RoomDto room;

    public static ReservationDto fromEntity(Reservation reservation) {
        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId());
        dto.setReservationNumber(reservation.getReservationNumber());
        dto.setGuestId(reservation.getGuest().getId());
        dto.setRoomId(reservation.getRoom().getId());
        dto.setCheckInDate(reservation.getCheckInDate());
        dto.setCheckOutDate(reservation.getCheckOutDate());
        dto.setNumberOfGuests(reservation.getNumberOfGuests());
        dto.setTotalAmount(reservation.getTotalAmount());
        dto.setPaidAmount(reservation.getPaidAmount());
        dto.setStatus(reservation.getStatus());
        dto.setSpecialRequests(reservation.getSpecialRequests());
        dto.setCreatedBy(reservation.getCreatedBy());
        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setUpdatedAt(reservation.getUpdatedAt());
        return dto;
    }

    public Reservation toEntity() {
        Reservation reservation = new Reservation();
        reservation.setReservationNumber(this.reservationNumber);
        reservation.setCheckInDate(this.checkInDate);
        reservation.setCheckOutDate(this.checkOutDate);
        reservation.setNumberOfGuests(this.numberOfGuests);
        reservation.setTotalAmount(this.totalAmount);
        reservation.setPaidAmount(this.paidAmount);
        reservation.setStatus(this.status != null ? this.status : Reservation.ReservationStatus.CONFIRMED);
        reservation.setSpecialRequests(this.specialRequests);
        reservation.setCreatedBy(this.createdBy);
        return reservation;
    }
}
