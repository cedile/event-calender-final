package com.devtiro.tasks.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devtiro.tasks.domain.dto.TaskListDto;
import com.devtiro.tasks.domain.entities.TaskList;
import com.devtiro.tasks.mappers.TaskListMapper;
import com.devtiro.tasks.services.TaskListService;

@RestController
@RequestMapping(path = "/api/task-lists")
public class TaskListController {

    private final TaskListService taskListService;
    private final TaskListMapper taskListMapper;

    public TaskListController(TaskListService taskListService, TaskListMapper taskListMapper) {
        this.taskListService = taskListService;
        this.taskListMapper = taskListMapper;
    }

    @GetMapping
    public List<TaskListDto> listTaskLists(Authentication authentication) {
        String username = authentication.getName(); // Get username from JWT
        return taskListService.listTaskListsByUsername(username)
                .stream()
                .map(taskListMapper::toDto)
                .toList();
    }

    @PostMapping
    public TaskListDto createTaskList(@RequestBody TaskListDto taskListDto, Authentication authentication) {
        String username = authentication.getName();
        TaskList createdTaskList = taskListService.createTaskListForUser(
                taskListMapper.fromDto(taskListDto),
                username
        );
        return taskListMapper.toDto(createdTaskList);
    }

    @GetMapping(path = "/{task_list_id}")
    public Optional<TaskListDto> getTaskList(@PathVariable("task_list_id") UUID taskListId, Authentication authentication){
        String username = authentication.getName();
        return taskListService.getTaskListByIdAndUsername(taskListId, username).map(taskListMapper::toDto);
    }

    @PutMapping(path = "/{task_list_id}")
    public TaskListDto updateTaskList(
            @PathVariable("task_list_id") UUID taskListId,
            @RequestBody TaskListDto taskListDto,
            Authentication authentication
    ) {
        String username = authentication.getName();
        TaskList updatedTaskList = taskListService.updateTaskListForUser(
                taskListId,
                taskListMapper.fromDto(taskListDto),
                username
        );

        return taskListMapper.toDto(updatedTaskList);
    }

    @DeleteMapping(path = "/{task_list_id}")
    public void deleteTaskList(@PathVariable("task_list_id") UUID taskListId, Authentication authentication) {
        String username = authentication.getName();
        taskListService.deleteTaskListForUser(taskListId, username);
    }
}
