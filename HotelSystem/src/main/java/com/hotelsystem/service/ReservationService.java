package com.hotelsystem.service;

import com.hotelsystem.dto.ReservationDto;
import com.hotelsystem.entity.Guest;
import com.hotelsystem.entity.Reservation;
import com.hotelsystem.entity.Room;
import com.hotelsystem.repository.GuestRepository;
import com.hotelsystem.repository.ReservationRepository;
import com.hotelsystem.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;

    public List<ReservationDto> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<ReservationDto> getReservationById(Long id) {
        return reservationRepository.findById(id)
                .map(ReservationDto::fromEntity);
    }

    public ReservationDto createReservation(ReservationDto reservationDto) {
        // 验证宾客是否存在
        Guest guest = guestRepository.findById(reservationDto.getGuestId())
                .orElseThrow(() -> new RuntimeException("宾客不存在"));

        // 验证房间是否存在
        Room room = roomRepository.findById(reservationDto.getRoomId())
                .orElseThrow(() -> new RuntimeException("房间不存在"));

        // 检查房间在指定日期是否可用
        if (!isRoomAvailable(room.getId(), reservationDto.getCheckInDate(), reservationDto.getCheckOutDate())) {
            throw new RuntimeException("房间在指定日期不可用");
        }

        Reservation reservation = reservationDto.toEntity();
        reservation.setGuest(guest);
        reservation.setRoom(room);

        // 计算总金额（简化版：天数 × 房间价格）
        long days = reservationDto.getCheckInDate().until(reservationDto.getCheckOutDate()).getDays();
        if (days <= 0) {
            throw new RuntimeException("离店日期必须晚于入住日期");
        }

        if (reservation.getTotalAmount() == null) {
            BigDecimal totalAmount = room.getPrice().multiply(BigDecimal.valueOf(days));
            reservation.setTotalAmount(totalAmount);
        }

        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationDto.fromEntity(savedReservation);
    }

    public ReservationDto updateReservation(Long id, ReservationDto reservationDto) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("预订不存在"));

        // 验证宾客是否存在
        Guest guest = guestRepository.findById(reservationDto.getGuestId())
                .orElseThrow(() -> new RuntimeException("宾客不存在"));

        // 验证房间是否存在
        Room room = roomRepository.findById(reservationDto.getRoomId())
                .orElseThrow(() -> new RuntimeException("房间不存在"));

        // 如果日期或房间有变化，检查房间可用性
        if (!existingReservation.getRoom().getId().equals(reservationDto.getRoomId()) ||
                !existingReservation.getCheckInDate().equals(reservationDto.getCheckInDate()) ||
                !existingReservation.getCheckOutDate().equals(reservationDto.getCheckOutDate())) {

            if (!isRoomAvailable(room.getId(), reservationDto.getCheckInDate(), reservationDto.getCheckOutDate(), id)) {
                throw new RuntimeException("房间在指定日期不可用");
            }
        }

        existingReservation.setGuest(guest);
        existingReservation.setRoom(room);
        existingReservation.setCheckInDate(reservationDto.getCheckInDate());
        existingReservation.setCheckOutDate(reservationDto.getCheckOutDate());
        existingReservation.setNumberOfGuests(reservationDto.getNumberOfGuests());
        existingReservation.setTotalAmount(reservationDto.getTotalAmount());
        existingReservation.setPaidAmount(reservationDto.getPaidAmount());
        existingReservation.setStatus(reservationDto.getStatus());
        existingReservation.setSpecialRequests(reservationDto.getSpecialRequests());

        Reservation updatedReservation = reservationRepository.save(existingReservation);
        return ReservationDto.fromEntity(updatedReservation);
    }

    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException("预订不存在");
        }
        reservationRepository.deleteById(id);
    }

    public List<ReservationDto> getReservationsByGuestId(Long guestId) {
        return reservationRepository.findByGuestId(guestId).stream()
                .map(ReservationDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ReservationDto> getReservationsByRoomId(Long roomId) {
        return reservationRepository.findByRoomId(roomId).stream()
                .map(ReservationDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ReservationDto> getReservationsByStatus(Reservation.ReservationStatus status) {
        return reservationRepository.findByStatus(status).stream()
                .map(ReservationDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ReservationDto> getReservationsByCheckInDateRange(LocalDate start, LocalDate end) {
        return reservationRepository.findByCheckInDateBetween(start, end).stream()
                .map(ReservationDto::fromEntity)
                .collect(Collectors.toList());
    }

    private boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        return isRoomAvailable(roomId, checkIn, checkOut, null);
    }

    private boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut, Long excludeReservationId) {
        List<Reservation> conflictingReservations = reservationRepository
                .findConflictingReservations(roomId, checkIn, checkOut, excludeReservationId);
        return conflictingReservations.isEmpty();
    }
}