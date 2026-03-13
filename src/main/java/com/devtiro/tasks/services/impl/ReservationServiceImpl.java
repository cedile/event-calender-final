package com.devtiro.tasks.services.impl;

import com.devtiro.tasks.domain.dto.ReservationDto;
import com.devtiro.tasks.domain.entities.Participant;
import com.devtiro.tasks.domain.entities.Reservation;
import com.devtiro.tasks.domain.entities.Room;
import com.devtiro.tasks.mappers.ReservationMapper;
import com.devtiro.tasks.repositories.ParticipantRepository;
import com.devtiro.tasks.repositories.ReservationRepository;
import com.devtiro.tasks.repositories.RoomRepository;
import com.devtiro.tasks.services.ReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;
    private final ReservationMapper reservationMapper;
    private final SecureRandom secureRandom = new SecureRandom();

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  RoomRepository roomRepository,
                                  ParticipantRepository participantRepository,
                                  ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.participantRepository = participantRepository;
        this.reservationMapper = reservationMapper;
        initializeRooms();
        initializeTestData();
    }

    private void initializeRooms() {
        for (int i = 101; i <= 105; i++) {
            if (!roomRepository.existsById(i)) {
                roomRepository.save(new Room(i));
            }
        }
    }

    private void initializeTestData() {
        if (reservationRepository.count() == 0) {
            // Test-Reservierung 1
            Room room1 = roomRepository.findByRoomNumber(101).orElse(new Room(101));
            if (!roomRepository.existsById(101)) {
                roomRepository.save(room1);
            }
            
            Reservation testRes1 = new Reservation();
            testRes1.setReservationDate(LocalDate.now().plusDays(1));
            testRes1.setTimeFrom(LocalTime.of(10, 0));
            testRes1.setTimeTo(LocalTime.of(11, 30));
            testRes1.setRoom(room1);
            testRes1.setComment("Test-Meeting für Projektbesprechung");
            testRes1.setPrivateCode(generateCode());
            testRes1.setPublicCode(generateCode());
            
            Participant p1 = new Participant("Max", "Mustermann", testRes1);
            testRes1.addParticipant(p1);
            Participant p2 = new Participant("Anna", "Schmidt", testRes1);
            testRes1.addParticipant(p2);
            
            reservationRepository.save(testRes1);

            // Test-Reservierung 2
            Room room2 = roomRepository.findByRoomNumber(102).orElse(new Room(102));
            if (!roomRepository.existsById(102)) {
                roomRepository.save(room2);
            }
            
            Reservation testRes2 = new Reservation();
            testRes2.setReservationDate(LocalDate.now().plusDays(2));
            testRes2.setTimeFrom(LocalTime.of(14, 0));
            testRes2.setTimeTo(LocalTime.of(15, 0));
            testRes2.setRoom(room2);
            testRes2.setComment("Präsentation für neue Mitarbeiter");
            testRes2.setPrivateCode(generateCode());
            testRes2.setPublicCode(generateCode());
            
            Participant p3 = new Participant("Peter", "Weber", testRes2);
            testRes2.addParticipant(p3);
            
            reservationRepository.save(testRes2);

            // Test-Reservierung 3
            Room room3 = roomRepository.findByRoomNumber(103).orElse(new Room(103));
            if (!roomRepository.existsById(103)) {
                roomRepository.save(room3);
            }
            
            Reservation testRes3 = new Reservation();
            testRes3.setReservationDate(LocalDate.now().plusDays(3));
            testRes3.setTimeFrom(LocalTime.of(9, 0));
            testRes3.setTimeTo(LocalTime.of(10, 30));
            testRes3.setRoom(room3);
            testRes3.setComment("Team-Retrospektive und Planung");
            testRes3.setPrivateCode(generateCode());
            testRes3.setPublicCode(generateCode());
            
            Participant p4 = new Participant("Lisa", "Müller", testRes3);
            testRes3.addParticipant(p4);
            Participant p5 = new Participant("Thomas", "Fischer", testRes3);
            testRes3.addParticipant(p5);
            
            reservationRepository.save(testRes3);
        }
    }

    private String generateCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            code.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }
        return code.toString();
    }

    @Override
    @Transactional
    public ReservationDto createReservation(ReservationDto reservationDto) {
        // Validierung: Datum in der Zukunft
        if (reservationDto.getReservationDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Das Datum muss in der Zukunft liegen");
        }

        // Validierung: Zeit Von < Zeit Bis
        if (!reservationDto.getTimeFrom().isBefore(reservationDto.getTimeTo())) {
            throw new IllegalArgumentException("Die Zeit 'Von' muss vor der Zeit 'Bis' liegen");
        }

        // Validierung: Raum verfügbar
        if (!isRoomAvailable(reservationDto.getRoomNumber(), 
                            reservationDto.getReservationDate(),
                            reservationDto.getTimeFrom(),
                            reservationDto.getTimeTo(),
                            null)) {
            throw new IllegalArgumentException("Das Zimmer ist zu diesem Zeitpunkt bereits reserviert");
        }

        // Raum laden oder erstellen
        Room room = roomRepository.findByRoomNumber(reservationDto.getRoomNumber())
                .orElseGet(() -> {
                    Room newRoom = new Room(reservationDto.getRoomNumber());
                    return roomRepository.save(newRoom);
                });

        // Reservierung erstellen
        Reservation reservation = reservationMapper.fromDto(reservationDto);
        reservation.setRoom(room);
        reservation.setPrivateCode(generateCode());
        reservation.setPublicCode(generateCode());

        // Teilnehmer setzen
        if (reservationDto.getParticipants() != null) {
            reservationDto.getParticipants().forEach(participantDto -> {
                Participant participant = new Participant(
                    participantDto.getFirstName(),
                    participantDto.getLastName(),
                    reservation
                );
                reservation.addParticipant(participant);
            });
        }

        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toDto(savedReservation);
    }

    @Override
    @Transactional
    public ReservationDto updateReservation(String privateCode, ReservationDto reservationDto) {
        Reservation existingReservation = reservationRepository.findByPrivateCode(privateCode)
                .orElseThrow(() -> new IllegalArgumentException("Reservierung nicht gefunden"));

        // Validierung: Datum in der Zukunft
        if (reservationDto.getReservationDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Das Datum muss in der Zukunft liegen");
        }

        // Validierung: Zeit Von < Zeit Bis
        if (!reservationDto.getTimeFrom().isBefore(reservationDto.getTimeTo())) {
            throw new IllegalArgumentException("Die Zeit 'Von' muss vor der Zeit 'Bis' liegen");
        }

        // Validierung: Raum verfügbar (außer dieser Reservierung)
        if (!isRoomAvailable(reservationDto.getRoomNumber(),
                            reservationDto.getReservationDate(),
                            reservationDto.getTimeFrom(),
                            reservationDto.getTimeTo(),
                            existingReservation.getId())) {
            throw new IllegalArgumentException("Das Zimmer ist zu diesem Zeitpunkt bereits reserviert");
        }

        // Raum laden oder erstellen
        Room room = roomRepository.findByRoomNumber(reservationDto.getRoomNumber())
                .orElseGet(() -> {
                    Room newRoom = new Room(reservationDto.getRoomNumber());
                    return roomRepository.save(newRoom);
                });

        // Bestehende Teilnehmer löschen
        existingReservation.getParticipants().forEach(participant -> {
            participantRepository.delete(participant);
        });
        existingReservation.getParticipants().clear();

        // Reservierung aktualisieren
        existingReservation.setReservationDate(reservationDto.getReservationDate());
        existingReservation.setTimeFrom(reservationDto.getTimeFrom());
        existingReservation.setTimeTo(reservationDto.getTimeTo());
        existingReservation.setRoom(room);
        existingReservation.setComment(reservationDto.getComment());

        // Neue Teilnehmer hinzufügen
        if (reservationDto.getParticipants() != null) {
            reservationDto.getParticipants().forEach(participantDto -> {
                Participant participant = new Participant(
                    participantDto.getFirstName(),
                    participantDto.getLastName(),
                    existingReservation
                );
                existingReservation.addParticipant(participant);
            });
        }

        Reservation updatedReservation = reservationRepository.save(existingReservation);
        return reservationMapper.toDto(updatedReservation);
    }

    @Override
    public ReservationDto getReservationByPrivateCode(String privateCode) {
        Reservation reservation = reservationRepository.findByPrivateCode(privateCode)
                .orElseThrow(() -> new IllegalArgumentException("Reservierung nicht gefunden"));
        return reservationMapper.toDto(reservation);
    }

    @Override
    public ReservationDto getReservationByPublicCode(String publicCode) {
        Reservation reservation = reservationRepository.findByPublicCode(publicCode)
                .orElseThrow(() -> new IllegalArgumentException("Reservierung nicht gefunden"));
        return reservationMapper.toDto(reservation);
    }

    @Override
    @Transactional
    public void deleteReservation(String privateCode) {
        Reservation reservation = reservationRepository.findByPrivateCode(privateCode)
                .orElseThrow(() -> new IllegalArgumentException("Reservierung nicht gefunden"));
        reservationRepository.delete(reservation);
    }

    @Override
    public List<ReservationDto> getAllReservations() {
        return reservationRepository.findAllByOrderByReservationDateAscTimeFromAsc().stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isRoomAvailable(Integer roomNumber, LocalDate date, 
                                  LocalTime timeFrom, LocalTime timeTo, UUID excludeReservationId) {
        List<Reservation> overlappingReservations;
        
        if (excludeReservationId != null) {
            overlappingReservations = reservationRepository.findOverlappingReservationsExcluding(
                roomNumber, date, timeFrom, timeTo, excludeReservationId);
        } else {
            overlappingReservations = reservationRepository.findOverlappingReservations(
                roomNumber, date, timeFrom, timeTo);
        }
        
        return overlappingReservations.isEmpty();
    }
}

