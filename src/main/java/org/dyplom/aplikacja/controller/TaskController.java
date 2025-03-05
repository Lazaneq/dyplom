package org.dyplom.aplikacja.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.dyplom.aplikacja.logic.TaskService;
import org.dyplom.aplikacja.logic.UserService;
import org.dyplom.aplikacja.model.CustomUserDetails;
import org.dyplom.aplikacja.model.Task;
import org.dyplom.aplikacja.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Task Controller")
@RestController
@RequestMapping("/tasks")
public class TaskController {

  private final String UPLOAD_DIR = "src/main/resources/static/uploads/";

  @Autowired
  private TaskService taskService;

  @Autowired
  private UserService userService;


  @GetMapping("/all")
  public List<Task> getAllTasks() {
    List<Task> tasks = taskService.getAllTasks();
    System.out.println("Fetched tasks: " + tasks);
    return tasks;
  }

  @GetMapping()
  public ResponseEntity<?> getTasks(@AuthenticationPrincipal CustomUserDetails currentUser) {
    List<Task> tasks;

    if (currentUser.getUser().getRole().equals("ADMIN")) {
      tasks = taskService.getAllTasks();
    } else if (currentUser.getUser().getRole().equals("ELECTRICIAN")) {
      tasks = taskService.getTasksForElectrician(currentUser.getUser().getId());
    } else {
      tasks = new ArrayList<>();
    }
    return ResponseEntity.ok(tasks);
  }

  @PostMapping
  public ResponseEntity<Task> createTask(
      @RequestParam("title") String title,
      @RequestParam("description") String description,
      @RequestParam("status") String status,
      @RequestParam(value = "images", required = false) MultipartFile[] images) {
    try {
      List<String> imagePaths = new ArrayList<>();
      if (images != null) {
        for (MultipartFile image : images) {
          String imagePath = saveImage(image);
          imagePaths.add(imagePath);
        }
      }

      Task task = new Task();
      task.setTitle(title);
      task.setDescription(description);
      task.setStatus(status);
      task.setImagePath(String.join(",", imagePaths));
      task.setDueDate(LocalDate.now());

      taskService.createTask(task);

      return ResponseEntity.status(HttpStatus.CREATED).body(task);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Task> updateTask(
      @PathVariable Long id,
      @RequestParam("title") String title,
      @RequestParam("description") String description,
      @RequestParam("status") String status,
      @RequestParam(value = "images", required = false) MultipartFile[] images) {

    try {
      Task existingTask = taskService.getTaskById(id);
      if (existingTask == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }

      existingTask.setTitle(title);
      existingTask.setDescription(description);
      if(status.equals("COMPLETED")){
        taskService.updateTaskStatus(existingTask.getId(), status);
      }else {
        existingTask.setStatus(status);
      }

      List<String> imagePaths = new ArrayList<>();
      if (images != null) {
        for (MultipartFile image : images) {
          String imagePath = saveImage(image);
          imagePaths.add(imagePath);
        }
      }
      existingTask.setImagePath(String.join(",", imagePaths));

      Task updatedTask = taskService.updateTask(existingTask);

      return ResponseEntity.ok(updatedTask);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails currentUser) {
    if (!currentUser.getUser().getRole().equals("ADMIN")) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();
  }


  @GetMapping("/images/{imageName}")
  public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
    try {
      Path path = Paths.get(UPLOAD_DIR + imageName);
      Resource resource = new UrlResource(path.toUri());

      if (resource.exists() || resource.isReadable()) {
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(resource);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (MalformedURLException e) {
      return ResponseEntity.notFound().build();
    }
  }

  private String saveImage(MultipartFile image) throws Exception {
    Files.createDirectories(Paths.get(UPLOAD_DIR));

    Path path = Paths.get(UPLOAD_DIR + image.getOriginalFilename());
    Files.write(path, image.getBytes());

    return path.getFileName().toString();
  }

  @PutMapping("/{id}/assign")
  public ResponseEntity<Task> assignElectricianToTask(
      @PathVariable Long id,
      @RequestParam Long electricianId,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    Task task = taskService.getTaskById(id);

    if (task.getStatus().equals("COMPLETED")) {
      System.out.println("Task completed by electrician " + electricianId);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    if (task.getAssignedElectrician() != null) {
      System.out.println("Assigned electrician: " + task.getAssignedElectrician());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    if (currentUser.getUser().getRole().equals("ADMIN")) {
      User electrician = userService.getUserById(Math.toIntExact(electricianId));
      task.setAssignedElectrician(electrician);
      task.setStatus("IN PROGRESS");
      Task updatedTask = taskService.updateTask(task);
      return ResponseEntity.ok(updatedTask);
    }

    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
  }

  @PutMapping("/{id}/assignToMe")
  public ResponseEntity<Task> assignTaskToCurrentUser(@PathVariable Long id,
      @AuthenticationPrincipal CustomUserDetails currentUser) {
    Task task = taskService.getTaskById(id);

    if (task.getAssignedElectrician() != null || task.getStatus().equals("COMPLETED")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    if (currentUser.getUser().getRole().equals("ELECTRICIAN") || currentUser.getUser().getRole().equals("ADMIN")) {
      task.setAssignedElectrician(currentUser.getUser());
      task.setStatus("IN PROGRESS");
      taskService.updateTask(task);
      return ResponseEntity.ok(task);
    }

    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
  }

  @PutMapping("/{taskId}/status")
  public ResponseEntity<Task> updateTaskStatus(@PathVariable Long taskId, @RequestBody Map<String, String> statusUpdate) {
    String status = statusUpdate.get("status");
    Task task = taskService.updateTaskStatus(taskId, status);
    return ResponseEntity.ok(task);
  }

}
