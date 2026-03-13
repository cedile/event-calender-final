package com.devtiro.tasks.domain.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @Column(name = "room_number", nullable = false)
    private Integer roomNumber;

    public Room() {
    }

    public Room(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomNumber, room.roomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber);
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                '}';
    }
}

