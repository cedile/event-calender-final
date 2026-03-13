package com.devtiro.tasks.controllers;

import com.devtiro.tasks.domain.dto.ReservationDto;
import com.devtiro.tasks.services.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(@Valid @RequestBody ReservationDto reservationDto) {
        try {
            ReservationDto created = reservationService.createReservation(reservationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/private/{privateCode}")
    public ResponseEntity<ReservationDto> updateReservation(
            @PathVariable String privateCode,
            @Valid @RequestBody ReservationDto reservationDto) {
        try {
            ReservationDto updated = reservationService.updateReservation(privateCode, reservationDto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/private/{privateCode}")
    public ResponseEntity<ReservationDto> getReservationByPrivateCode(@PathVariable String privateCode) {
        try {
            ReservationDto reservation = reservationService.getReservationByPrivateCode(privateCode);
            return ResponseEntity.ok(reservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/{publicCode}")
    public ResponseEntity<ReservationDto> getReservationByPublicCode(@PathVariable String publicCode) {
        try {
            ReservationDto reservation = reservationService.getReservationByPublicCode(publicCode);
            return ResponseEntity.ok(reservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/private/{privateCode}")
    public ResponseEntity<Void> deleteReservation(@PathVariable String privateCode) {
        try {
            reservationService.deleteReservation(privateCode);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        List<ReservationDto> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }
}

