package com.devtiro.tasks.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.devtiro.tasks.domain.entities.TaskList;
import com.devtiro.tasks.domain.entities.User;
import com.devtiro.tasks.repositories.TaskListRepository;
import com.devtiro.tasks.repositories.UserRepository;
import com.devtiro.tasks.services.TaskListService;

import jakarta.transaction.Transactional;

@Service
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;
    private final UserRepository userRepository;

    public TaskListServiceImpl(TaskListRepository taskListRepository, UserRepository userRepository) {
        this.taskListRepository = taskListRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TaskList> listTaskLists() {
        return taskListRepository.findAll();
    }

    @Override
    public TaskList createTaskList(TaskList taskList) {
        if(null != taskList.getId()) {
            throw new IllegalArgumentException("Task list already has an ID!");
        }
        if(null == taskList.getTitle() || taskList.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task list title must be present!");
        }

        LocalDateTime now = LocalDateTime.now();
        return taskListRepository.save(new TaskList(
                null,
                taskList.getTitle(),
                taskList.getDescription(),
                null,
                now,
                now
        ));
    }

    @Override
    public Optional<TaskList> getTaskList(UUID id) {
        return taskListRepository.findById(id);
    }

    @Transactional
    @Override
    public TaskList updateTaskList(UUID taskListId, TaskList taskList) {
        if(null == taskList.getId()) {
            throw new IllegalArgumentException("Task list must have an ID");
        }

        if(!Objects.equals(taskList.getId(), taskListId)) {
            throw new IllegalArgumentException("Attempting to change task list ID, this is not permitted!");
        }

        TaskList existingTaskList = taskListRepository.findById(taskListId).orElseThrow(() ->
                new IllegalArgumentException("Task list not found!"));

        existingTaskList.setTitle(taskList.getTitle());
        existingTaskList.setDescription(taskList.getDescription());
        existingTaskList.setUpdated(LocalDateTime.now());
        return taskListRepository.save(existingTaskList);
    }

    @Override
    public void deleteTaskList(UUID taskListId) {
        taskListRepository.deleteById(taskListId);
    }

    // NEW USER-SPECIFIC METHODS
    @Override
    public List<TaskList> listTaskListsByUsername(String username) {
        return taskListRepository.findByUserUsernameOrderByCreatedDesc(username);
    }

    @Override
    public TaskList createTaskListForUser(TaskList taskList, String username) {
        if(null != taskList.getId()) {
            throw new IllegalArgumentException("Task list already has an ID!");
        }
        if(null == taskList.getTitle() || taskList.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task list title must be present!");
        }

        // Find the user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        LocalDateTime now = LocalDateTime.now();
        TaskList newTaskList = new TaskList(
                null,
                taskList.getTitle(),
                taskList.getDescription(),
                null,
                now,
                now
        );
        newTaskList.setUser(user);
        
        return taskListRepository.save(newTaskList);
    }

    @Override
    public Optional<TaskList> getTaskListByIdAndUsername(UUID id, String username) {
        return taskListRepository.findByIdAndUserUsername(id, username);
    }

    @Transactional
    @Override
    public TaskList updateTaskListForUser(UUID taskListId, TaskList taskList, String username) {
        if(null == taskList.getId()) {
            throw new IllegalArgumentException("Task list must have an ID");
        }

        if(!Objects.equals(taskList.getId(), taskListId)) {
            throw new IllegalArgumentException("Attempting to change task list ID, this is not permitted!");
        }

        TaskList existingTaskList = taskListRepository.findByIdAndUserUsername(taskListId, username)
                .orElseThrow(() -> new IllegalArgumentException("Task list not found or access denied!"));

        existingTaskList.setTitle(taskList.getTitle());
        existingTaskList.setDescription(taskList.getDescription());
        existingTaskList.setUpdated(LocalDateTime.now());
        return taskListRepository.save(existingTaskList);
    }

    @Override
    public void deleteTaskListForUser(UUID taskListId, String username) {
        TaskList existingTaskList = taskListRepository.findByIdAndUserUsername(taskListId, username)
                .orElseThrow(() -> new IllegalArgumentException("Task list not found or access denied!"));
        
        taskListRepository.delete(existingTaskList);
    }
}
