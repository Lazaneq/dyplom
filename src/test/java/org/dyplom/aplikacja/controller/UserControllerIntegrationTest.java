package org.dyplom.aplikacja.controller;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.dyplom.aplikacja.logic.repositories.UserRepository;
import org.dyplom.aplikacja.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  private final String jwtSecret = "my_secret_key";
  private String jwtToken;

  @BeforeEach
  void setUp() {
    jwtToken = generateTokenForTest("admin", "ADMIN");
  }

  private String generateTokenForTest(String username, String role) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + 86400000);

    return Jwts.builder()
        .setSubject(username)
        .claim("role", role)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  @Test
  void testGetAllUsersWithJwt() throws Exception {
    mockMvc.perform(get("/users")
            .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
  }

  @Test
  void testGetUserByIdWithJwt() throws Exception {
    User user = new User();
    user.setUsername("testUser");
    user.setPassword("hashedPassword");
    user.setRole("USER");
    userRepository.save(user);

    mockMvc.perform(get("/users/" + user.getId())
            .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username", is("testUser")));

    userRepository.delete(user);
  }

  @Test
  void testCreateUserWithJwt() throws Exception {
    mockMvc.perform(post("/users")
            .header("Authorization", "Bearer " + jwtToken)
            .param("name", "newUser")
            .param("password", "password123")
            .param("role", "USER"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username", is("newUser")))
        .andExpect(jsonPath("$.role", is("USER")));
  }

  @Test
  void testUpdateUserWithJwt() throws Exception {
    User user = new User();
    user.setUsername("updateUser");
    user.setPassword("hashedPassword");
    user.setRole("USER");
    userRepository.save(user);

    user.setUsername("updatedUser");
    String newPassword = "newHashedPassword";

    String requestBody = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", user.getUsername(), newPassword);

    mockMvc.perform(put("/users/" + user.getId())
            .header("Authorization", "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username", is("updatedUser")))
        .andExpect(jsonPath("$.role", is("USER")))
        .andExpect(jsonPath("$.password").exists());
  }



  @Test
  void testDeleteUserWithJwt() throws Exception {
    User user = new User();
    user.setUsername("deleteUser");
    user.setPassword("hashedPassword");
    user.setRole("USER");
    userRepository.save(user);

    mockMvc.perform(delete("/users/" + user.getId())
            .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/users/" + user.getId())
            .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isNotFound());
  }

  @Test
  void testGetCurrentUserWithJwt() throws Exception {
    User user = new User();
    user.setUsername("currentUser");
    user.setPassword("hashedPassword");
    user.setRole("USER");
    userRepository.save(user);

    String userToken = generateTokenForTest("currentUser", "USER");

    mockMvc.perform(get("/users/me")
            .header("Authorization", "Bearer " + userToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username", is("currentUser")))
        .andExpect(jsonPath("$.role", is("USER")));
  }
}
