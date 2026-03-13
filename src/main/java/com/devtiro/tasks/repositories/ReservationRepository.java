package com.devtiro.tasks.repositories;

import com.devtiro.tasks.domain.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    
    Optional<Reservation> findByPrivateCode(String privateCode);
    
    Optional<Reservation> findByPublicCode(String publicCode);
    
    List<Reservation> findAllByOrderByReservationDateAscTimeFromAsc();
    
    @Query("SELECT r FROM Reservation r WHERE r.room.roomNumber = :roomNumber " +
           "AND r.reservationDate = :date " +
           "AND ((r.timeFrom < :timeTo AND r.timeTo > :timeFrom))")
    List<Reservation> findOverlappingReservations(
        @Param("roomNumber") Integer roomNumber,
        @Param("date") LocalDate date,
        @Param("timeFrom") LocalTime timeFrom,
        @Param("timeTo") LocalTime timeTo
    );
    
    @Query("SELECT r FROM Reservation r WHERE r.id != :excludeId " +
           "AND r.room.roomNumber = :roomNumber " +
           "AND r.reservationDate = :date " +
           "AND ((r.timeFrom < :timeTo AND r.timeTo > :timeFrom))")
    List<Reservation> findOverlappingReservationsExcluding(
        @Param("roomNumber") Integer roomNumber,
        @Param("date") LocalDate date,
        @Param("timeFrom") LocalTime timeFrom,
        @Param("timeTo") LocalTime timeTo,
        @Param("excludeId") UUID excludeId
    );
}

