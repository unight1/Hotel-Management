package com.hotelsystem.repository;

import com.hotelsystem.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByReservationNumber(String reservationNumber);
    List<Reservation> findByGuestId(Long guestId);
    List<Reservation> findByRoomId(Long roomId);
    List<Reservation> findByStatus(Reservation.ReservationStatus status);
    List<Reservation> findByCheckInDateBetween(LocalDate start, LocalDate end);
    List<Reservation> findByCheckOutDateBetween(LocalDate start, LocalDate end);

    // 检查房间在指定日期是否有冲突的预订
    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId " +
            "AND r.status IN (com.hotelsystem.entity.Reservation.ReservationStatus.CONFIRMED, " +
            "com.hotelsystem.entity.Reservation.ReservationStatus.CHECKED_IN) " +
            "AND ((r.checkInDate < :checkOut AND r.checkOutDate > :checkIn) " +
            "OR (r.checkInDate = :checkIn OR r.checkOutDate = :checkOut)) " +
            "AND (:excludeReservationId IS NULL OR r.id != :excludeReservationId)")
    List<Reservation> findConflictingReservations(@Param("roomId") Long roomId,
                                                  @Param("checkIn") LocalDate checkIn,
                                                  @Param("checkOut") LocalDate checkOut,
                                                  @Param("excludeReservationId") Long excludeReservationId);

}
