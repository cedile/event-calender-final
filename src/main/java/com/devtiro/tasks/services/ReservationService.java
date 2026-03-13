package com.devtiro.tasks.services;

import com.devtiro.tasks.domain.dto.ReservationDto;
import java.util.List;
import java.util.UUID;

public interface ReservationService {
    ReservationDto createReservation(ReservationDto reservationDto);
    ReservationDto updateReservation(String privateCode, ReservationDto reservationDto);
    ReservationDto getReservationByPrivateCode(String privateCode);
    ReservationDto getReservationByPublicCode(String publicCode);
    void deleteReservation(String privateCode);
    List<ReservationDto> getAllReservations();
    boolean isRoomAvailable(Integer roomNumber, java.time.LocalDate date, 
                           java.time.LocalTime timeFrom, java.time.LocalTime timeTo, UUID excludeReservationId);
}

