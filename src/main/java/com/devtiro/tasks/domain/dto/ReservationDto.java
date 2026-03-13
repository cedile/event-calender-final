package com.devtiro.tasks.domain.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class ReservationDto {

    private UUID id;

    @NotNull(message = "Datum ist erforderlich")
    @Future(message = "Datum muss in der Zukunft liegen")
    private LocalDate reservationDate;

    @NotNull(message = "Zeit 'Von' ist erforderlich")
    private LocalTime timeFrom;

    @NotNull(message = "Zeit 'Bis' ist erforderlich")
    private LocalTime timeTo;

    @NotNull(message = "Zimmer ist erforderlich")
    @Min(value = 101, message = "Zimmernummer muss zwischen 101 und 105 liegen")
    @Max(value = 105, message = "Zimmernummer muss zwischen 101 und 105 liegen")
    private Integer roomNumber;

    @NotBlank(message = "Bemerkung ist erforderlich")
    @Size(min = 10, max = 200, message = "Bemerkung muss zwischen 10 und 200 Zeichen lang sein")
    private String comment;

    @NotEmpty(message = "Mindestens ein Teilnehmer ist erforderlich")
    @Size(min = 1, message = "Mindestens ein Teilnehmer ist erforderlich")
    private List<ParticipantDto> participants;

    private String privateCode;
    private String publicCode;

    public ReservationDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalTime getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(LocalTime timeFrom) {
        this.timeFrom = timeFrom;
    }

    public LocalTime getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(LocalTime timeTo) {
        this.timeTo = timeTo;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ParticipantDto> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantDto> participants) {
        this.participants = participants;
    }

    public String getPrivateCode() {
        return privateCode;
    }

    public void setPrivateCode(String privateCode) {
        this.privateCode = privateCode;
    }

    public String getPublicCode() {
        return publicCode;
    }

    public void setPublicCode(String publicCode) {
        this.publicCode = publicCode;
    }
}

