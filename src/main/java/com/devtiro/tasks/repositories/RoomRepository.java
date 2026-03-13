package com.devtiro.tasks.repositories;

import com.devtiro.tasks.domain.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Optional<Room> findByRoomNumber(Integer roomNumber);
}

