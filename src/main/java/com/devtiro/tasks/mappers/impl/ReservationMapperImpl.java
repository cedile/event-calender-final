package com.devtiro.tasks.mappers.impl;

import com.devtiro.tasks.domain.dto.ParticipantDto;
import com.devtiro.tasks.domain.dto.ReservationDto;
import com.devtiro.tasks.domain.entities.Participant;
import com.devtiro.tasks.domain.entities.Reservation;
import com.devtiro.tasks.domain.entities.Room;
import com.devtiro.tasks.mappers.ReservationMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ReservationMapperImpl implements ReservationMapper {

    @Override
    public Reservation fromDto(ReservationDto reservationDto) {
        if (reservationDto == null) {
            return null;
        }

        Reservation reservation = new Reservation();
        reservation.setId(reservationDto.getId());
        reservation.setReservationDate(reservationDto.getReservationDate());
        reservation.setTimeFrom(reservationDto.getTimeFrom());
        reservation.setTimeTo(reservationDto.getTimeTo());
        reservation.setComment(reservationDto.getComment());
        reservation.setPrivateCode(reservationDto.getPrivateCode());
        reservation.setPublicCode(reservationDto.getPublicCode());

        if (reservationDto.getRoomNumber() != null) {
            Room room = new Room();
            room.setRoomNumber(reservationDto.getRoomNumber());
            reservation.setRoom(room);
        }

        if (reservationDto.getParticipants() != null) {
            reservationDto.getParticipants().forEach(participantDto -> {
                Participant participant = new Participant();
                participant.setFirstName(participantDto.getFirstName());
                participant.setLastName(participantDto.getLastName());
                participant.setReservation(reservation);
                reservation.addParticipant(participant);
            });
        }

        return reservation;
    }

    @Override
    public ReservationDto toDto(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservation.getId());
        reservationDto.setReservationDate(reservation.getReservationDate());
        reservationDto.setTimeFrom(reservation.getTimeFrom());
        reservationDto.setTimeTo(reservation.getTimeTo());
        reservationDto.setComment(reservation.getComment());
        reservationDto.setPrivateCode(reservation.getPrivateCode());
        reservationDto.setPublicCode(reservation.getPublicCode());

        if (reservation.getRoom() != null) {
            reservationDto.setRoomNumber(reservation.getRoom().getRoomNumber());
        }

        if (reservation.getParticipants() != null) {
            reservationDto.setParticipants(
                reservation.getParticipants().stream()
                    .map(participant -> {
                        ParticipantDto participantDto = new ParticipantDto();
                        participantDto.setId(participant.getId());
                        participantDto.setFirstName(participant.getFirstName());
                        participantDto.setLastName(participant.getLastName());
                        return participantDto;
                    })
                    .collect(Collectors.toList())
            );
        }

        return reservationDto;
    }
}

