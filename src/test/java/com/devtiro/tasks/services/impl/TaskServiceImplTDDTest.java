package com.devtiro.tasks.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devtiro.tasks.domain.entities.Task;
import com.devtiro.tasks.domain.entities.TaskPriority;
import com.devtiro.tasks.domain.entities.TaskStatus;
import com.devtiro.tasks.repositories.TaskRepository;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTDDTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private UUID taskListId;
    private UUID taskId;
    private Task existingTask;
    private Task updatedTaskData;

    @BeforeEach
    void setUp() {
        taskListId = UUID.randomUUID();
        taskId = UUID.randomUUID();

        existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Original Title");
        existingTask.setDescription("Original Description");
        existingTask.setPriority(TaskPriority.LOW);
        existingTask.setStatus(TaskStatus.OPEN);
        existingTask.setCreated(LocalDateTime.now().minusDays(1));  // Fixed method name
        existingTask.setUpdated(LocalDateTime.now().minusDays(1));  // Fixed method name

        updatedTaskData = new Task();
        updatedTaskData.setTitle("Updated Title");
        updatedTaskData.setDescription("Updated Description");
        updatedTaskData.setPriority(TaskPriority.HIGH);
    }

    @Test
    void updateTask_ShouldUpdateTaskSuccessfully_WhenTaskExists() {
        // Arrange
        when(taskRepository.findByTaskListIdAndId(taskListId, taskId))
            .thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Task result = taskService.updateTask(taskListId, taskId, updatedTaskData);

        // Assert
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(TaskPriority.HIGH, result.getPriority());
        assertEquals(TaskStatus.OPEN, result.getStatus()); // Status shouldn't change
        assertEquals(taskId, result.getId()); // ID shouldn't change
        assertNotNull(result.getUpdated());  // Fixed method name
        
        verify(taskRepository, times(1)).findByTaskListIdAndId(taskListId, taskId);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void updateTask_ShouldThrowException_WhenTaskNotFound() {
        // Arrange
        when(taskRepository.findByTaskListIdAndId(taskListId, taskId))
            .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> taskService.updateTask(taskListId, taskId, updatedTaskData)
        );

        assertEquals("Task not found", exception.getMessage());
        verify(taskRepository, times(1)).findByTaskListIdAndId(taskListId, taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void updateTask_ShouldThrowException_WhenTitleIsBlank() {
        // Arrange
        updatedTaskData.setTitle("   ");
        // Remove the unnecessary stubbing - we don't need to mock repository calls 
        // because the validation happens before repository interaction

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> taskService.updateTask(taskListId, taskId, updatedTaskData)
        );

        assertEquals("Task must have a title!", exception.getMessage());
        // No repository interaction should happen due to early validation
        verify(taskRepository, never()).findByTaskListIdAndId(taskListId, taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void updateTask_ShouldThrowException_WhenTitleIsNull() {
        // Arrange
        updatedTaskData.setTitle(null);
        // Remove the unnecessary stubbing - we don't need to mock repository calls 
        // because the validation happens before repository interaction

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> taskService.updateTask(taskListId, taskId, updatedTaskData)
        );

        assertEquals("Task must have a title!", exception.getMessage());
        // No repository interaction should happen due to early validation
        verify(taskRepository, never()).findByTaskListIdAndId(taskListId, taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }
}