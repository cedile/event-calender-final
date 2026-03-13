package com.devtiro.tasks.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.devtiro.tasks.domain.entities.TaskList;

public interface TaskListService {
    // Original methods (keep for backward compatibility)
    List<TaskList> listTaskLists();
    TaskList createTaskList(TaskList taskList);
    Optional<TaskList> getTaskList(UUID id);
    TaskList updateTaskList(UUID taskListId, TaskList taskList);
    void deleteTaskList(UUID taskListId);
    
    // New user-specific methods
    List<TaskList> listTaskListsByUsername(String username);
    TaskList createTaskListForUser(TaskList taskList, String username);
    Optional<TaskList> getTaskListByIdAndUsername(UUID id, String username);
    TaskList updateTaskListForUser(UUID taskListId, TaskList taskList, String username);
    void deleteTaskListForUser(UUID taskListId, String username);
}
