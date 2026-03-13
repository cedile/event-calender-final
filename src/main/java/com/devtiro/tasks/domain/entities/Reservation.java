package com.devtiro.tasks.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    @Column(name = "time_from", nullable = false)
    private LocalTime timeFrom;

    @Column(name = "time_to", nullable = false)
    private LocalTime timeTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_number", nullable = false)
    private Room room;

    @Column(name = "comment", length = 200)
    private String comment;

    @Column(name = "private_code", nullable = false, unique = true)
    private String privateCode;

    @Column(name = "public_code", nullable = false, unique = true)
    private String publicCode;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    public Reservation() {
    }

    public Reservation(LocalDate reservationDate, LocalTime timeFrom, LocalTime timeTo, 
                      Room room, String comment, String privateCode, String publicCode) {
        this.reservationDate = reservationDate;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.room = room;
        this.comment = comment;
        this.privateCode = privateCode;
        this.publicCode = publicCode;
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
        participant.setReservation(this);
    }

    public void removeParticipant(Participant participant) {
        participants.remove(participant);
        participant.setReservation(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", reservationDate=" + reservationDate +
                ", timeFrom=" + timeFrom +
                ", timeTo=" + timeTo +
                ", room=" + room +
                ", comment='" + comment + '\'' +
                '}';
    }
}

