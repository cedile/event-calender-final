package com.devtiro.tasks.mappers;

import com.devtiro.tasks.domain.dto.ReservationDto;
import com.devtiro.tasks.domain.entities.Reservation;

public interface ReservationMapper {
    Reservation fromDto(ReservationDto reservationDto);
    ReservationDto toDto(Reservation reservation);
}

