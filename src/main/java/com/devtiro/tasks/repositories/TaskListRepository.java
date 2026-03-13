package com.devtiro.tasks.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devtiro.tasks.domain.entities.TaskList;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, UUID> {

     // Find task lists by user
    List<TaskList> findByUserUsernameOrderByCreatedDesc(String username);
    
    // Find by ID and user (for security)
    Optional<TaskList> findByIdAndUserUsername(UUID id, String username);
}

