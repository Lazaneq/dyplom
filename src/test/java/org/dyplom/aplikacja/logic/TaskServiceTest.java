package org.dyplom.aplikacja.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.dyplom.aplikacja.logic.repositories.TaskRepository;
import org.dyplom.aplikacja.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TaskServiceTest {

  @Mock
  private TaskRepository taskRepository;

  @InjectMocks
  private TaskService taskService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createTask() {
    Task task = new Task();
    task.setTitle("Test Task");

    when(taskRepository.save(task)).thenReturn(task);

    Task createdTask = taskService.createTask(task);

    assertNotNull(createdTask);
    assertEquals("Test Task", createdTask.getTitle());
  }

  @Test
  void getTaskById() {
    Task task = new Task();
    task.setId(1L);

    when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

    Task foundTask = taskService.getTaskById(1L);

    assertNotNull(foundTask);
    assertEquals(1L, foundTask.getId());
  }

  @Test
  void testGetTaskByIdNotFound() {
    when(taskRepository.findById(1L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> {
      taskService.getTaskById(1L);
    });

    assertEquals("Task not found", exception.getMessage());
  }

  @Test
  void updateTaskStatus() {
    Task task = new Task();
    task.setId(1L);
    task.setStatus("NEW");

    when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    Task updatedTask = taskService.updateTaskStatus(1L, "IN PROGRESS");

    assertNotNull(updatedTask);
    assertEquals("IN PROGRESS", updatedTask.getStatus());
  }
}