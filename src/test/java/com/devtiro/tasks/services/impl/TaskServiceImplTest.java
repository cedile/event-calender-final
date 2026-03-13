package com.devtiro.tasks.services.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devtiro.tasks.domain.entities.Task;
import com.devtiro.tasks.domain.entities.TaskList;
import com.devtiro.tasks.domain.entities.TaskPriority;
import com.devtiro.tasks.domain.entities.TaskStatus;
import com.devtiro.tasks.repositories.TaskListRepository;
import com.devtiro.tasks.repositories.TaskRepository;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskListRepository taskListRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private UUID taskListId;
    private UUID taskId;
    private Task task;
    private TaskList taskList;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        taskListId = UUID.randomUUID();
        taskId = UUID.randomUUID();

        // Create TaskList
        taskList = new TaskList();
        taskList.setId(taskListId);
        taskList.setTitle("Test Task List");
        taskList.setDescription("Test Description");
        taskList.setCreated(LocalDateTime.now());
        taskList.setUpdated(LocalDateTime.now());

        // Create Task
        task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setPriority(TaskPriority.HIGH);
        task.setDueDate(LocalDateTime.now().plusDays(1));
    }

    // ===== CREATE TASK TESTS =====
    @Test
    void createTask_ShouldCreateTaskSuccessfully_WhenValidInputProvided() {
        // Arrange
        when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(taskList));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task savedTask = invocation.getArgument(0);
            savedTask.setId(UUID.randomUUID());
            return savedTask;
        });

        // Act
        Task result = taskService.createTask(taskListId, task);

        // Assert
        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(TaskPriority.HIGH, result.getPriority());
        assertEquals(TaskStatus.OPEN, result.getStatus());
        assertEquals(taskList, result.getTaskList());
        assertNotNull(result.getCreated());
        assertNotNull(result.getUpdated());

        verify(taskListRepository, times(1)).findById(taskListId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createTask_ShouldThrowException_WhenTaskAlreadyHasId() {
        // Arrange
        task.setId(UUID.randomUUID());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> taskService.createTask(taskListId, task)
        );

        assertEquals("Task already has an ID!", exception.getMessage());
        verifyNoInteractions(taskRepository);
        verifyNoInteractions(taskListRepository);
    }

    @Test
    void createTask_ShouldThrowException_WhenTitleIsNull() {
        // Arrange
        task.setTitle(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> taskService.createTask(taskListId, task)
        );

        assertEquals("Task must have a title!", exception.getMessage());
        verifyNoInteractions(taskRepository);
        verifyNoInteractions(taskListRepository);
    }

    @Test
    void createTask_ShouldThrowException_WhenTitleIsBlank() {
        // Arrange
        task.setTitle("   ");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> taskService.createTask(taskListId, task)
        );

        assertEquals("Task must have a title!", exception.getMessage());
        verifyNoInteractions(taskRepository);
        verifyNoInteractions(taskListRepository);
    }

    @Test
    void createTask_ShouldThrowException_WhenTaskListNotFound() {
        // Arrange
        when(taskListRepository.findById(taskListId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> taskService.createTask(taskListId, task)
        );

        assertEquals("Invalid Task List ID provided!", exception.getMessage());
        verify(taskListRepository, times(1)).findById(taskListId);
        verifyNoInteractions(taskRepository);
    }

    @Test
    void createTask_ShouldSetDefaultPriority_WhenPriorityIsNull() {
        // Arrange
        task.setPriority(null);
        when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(taskList));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Task result = taskService.createTask(taskListId, task);

        // Assert
        assertEquals(TaskPriority.MEDIUM, result.getPriority());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    // ===== DELETE TASK TESTS =====
    @Test
    void deleteTask_ShouldDeleteTaskSuccessfully() {
        // Arrange
        doNothing().when(taskRepository).deleteByTaskListIdAndId(taskListId, taskId);

        // Act & Assert
        assertDoesNotThrow(() -> taskService.deleteTask(taskListId, taskId));
        
        verify(taskRepository, times(1)).deleteByTaskListIdAndId(taskListId, taskId);
    }

    @Test
    void deleteTask_ShouldHandleNonExistentTask() {
        // Arrange
        doNothing().when(taskRepository).deleteByTaskListIdAndId(taskListId, taskId);

        // Act & Assert
        assertDoesNotThrow(() -> taskService.deleteTask(taskListId, taskId));
        
        verify(taskRepository, times(1)).deleteByTaskListIdAndId(taskListId, taskId);
    }

    // ===== LIST TASKS TESTS =====
    @Test
    void listTasks_ShouldReturnTasksSuccessfully() {
        // Arrange
        Task task1 = new Task();
        task1.setId(UUID.randomUUID());
        task1.setTitle("Task 1");
        
        Task task2 = new Task();
        task2.setId(UUID.randomUUID());
        task2.setTitle("Task 2");
        
        List<Task> expectedTasks = Arrays.asList(task1, task2);
        when(taskRepository.findByTaskListId(taskListId)).thenReturn(expectedTasks);

        // Act
        List<Task> result = taskService.listTasks(taskListId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Task 2", result.get(1).getTitle());
        verify(taskRepository, times(1)).findByTaskListId(taskListId);
    }

    @Test
    void listTasks_ShouldReturnEmptyList_WhenNoTasksFound() {
        // Arrange
        when(taskRepository.findByTaskListId(taskListId)).thenReturn(Arrays.asList());

        // Act
        List<Task> result = taskService.listTasks(taskListId);

        // Assert
        assertTrue(result.isEmpty());
        verify(taskRepository, times(1)).findByTaskListId(taskListId);
    }
}