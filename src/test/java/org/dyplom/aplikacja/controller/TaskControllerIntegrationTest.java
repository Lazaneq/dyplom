package org.dyplom.aplikacja.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.dyplom.aplikacja.logic.repositories.TaskRepository;
import org.dyplom.aplikacja.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TaskRepository taskRepository;

  private final String jwtSecret = "my_secret_key";

  private String jwtToken;

  @BeforeEach
  void setUp() {
    jwtToken = generateTokenForTest("admin", "ADMIN");
  }

  private String generateTokenForTest(String username, String role) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + 86400000); // 24 godziny

    return Jwts.builder()
        .setSubject(username)
        .claim("role", role)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  @Test
  void testGetAllTasksWithJwt() throws Exception {
    mockMvc.perform(get("/tasks/all")
            .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isOk());
  }

  @Test
  void testCreateTaskWithJwt() throws Exception {
    mockMvc.perform(post("/tasks")
            .header("Authorization", "Bearer " + jwtToken)
            .param("title", "New Task")
            .param("description", "Task description")
            .param("status", "NEW"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title", is("New Task")));
  }

  @Test
  void testUpdateTaskWithJwt() throws Exception {
    Task task = new Task();
    task.setTitle("Old Task");
    task.setStatus("NEW");
    taskRepository.save(task);

    mockMvc.perform(put("/tasks/" + task.getId())
            .header("Authorization", "Bearer " + jwtToken)
            .param("title", "Updated Task")
            .param("description", "Updated description")
            .param("status", "IN PROGRESS"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is("Updated Task")))
        .andExpect(jsonPath("$.status", is("IN PROGRESS")));

    taskRepository.delete(task);
  }

  @Test
  void testDeleteTaskWithJwt() throws Exception {

    Task task = new Task();
    task.setTitle("Task to delete");
    task.setStatus("NEW");
    taskRepository.save(task);

    mockMvc.perform(delete("/tasks/" + task.getId())
            .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isNoContent());
  }
}
