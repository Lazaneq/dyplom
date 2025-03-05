package org.dyplom.aplikacja.logic;

import jakarta.transaction.Transactional;
import java.util.List;
import org.dyplom.aplikacja.exceptions.ResourceNotFoundException;
import org.dyplom.aplikacja.logic.repositories.ResourceRepository;
import org.dyplom.aplikacja.logic.repositories.TaskRepository;
import org.dyplom.aplikacja.model.Resource;
import org.dyplom.aplikacja.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private ResourceRepository resourceRepository;

  public Task createTask(Task task) {
    return taskRepository.save(task);
  }

  public Task updateTask(Task task) {
    return taskRepository.save(task);
  }

  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  public Task getTaskById(Long id) {
    return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
  }
  public void deleteTask(Long id) {
    taskRepository.deleteById(id);
  }

  public List<Task> getTasksForElectrician(final Integer electricianId) {
    return taskRepository.findByStatusOrAssignedElectricianId("NEW", electricianId);
  }

  @Transactional
  public Task updateTaskStatus(Long taskId, String newStatus) {
    Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    if (newStatus.equals("COMPLETED")) {
      releaseResources(task);
    }
    task.setStatus(newStatus);
    return taskRepository.save(task);
  }

  private void releaseResources(Task task) {
    for (Resource resource : task.getResources()) {
      resource.setStatus("AVAILABLE");
      resource.getTasks().remove(task);
      resourceRepository.save(resource);
    }

    task.getResources().clear();
    taskRepository.save(task);
  }
}
