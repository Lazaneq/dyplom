package org.dyplom.aplikacja.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import org.dyplom.aplikacja.logic.UserRepository;
import org.dyplom.aplikacja.logic.UserService;
import org.dyplom.aplikacja.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="User Controller")
@RestController
@RequestMapping("/users")
public class UserController {

  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Autowired
  private UserService userService; // Injecting UserService
  @Autowired
  private UserRepository userRepository;

  // Get all users
  @Description("Get all users")
  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  // Get user by ID
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Integer id) {
    User user = userService.getUserById(id);
    return ResponseEntity.ok(user);
  }

  @GetMapping("/me")
  public ResponseEntity<?> getCurrentUser(Principal principal) {
    if (principal == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
    }
    Optional<User> user = userRepository.findByUsername(principal.getName());
    return ResponseEntity.ok(user);
  }


  // Create a new user
  @PostMapping
  public ResponseEntity<User> createUser(
      @RequestParam("name") String name,
      @RequestParam("password") String password,
      @RequestParam("role") Set<String > role
  ) {
    // Hash the password before saving
    String hashedPassword = passwordEncoder.encode(password);

    // Create a new User instance
    User user = new User();
    user.setUsername(name);
    user.setPassword(hashedPassword); // Set the hashed password
    user.setRoles(role); // Set the role

    User createdUser = userService.createUser(user);
    return ResponseEntity.status(201).body(createdUser);
  }

  // Update an existing user
  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
    User updatedUser = userService.updateUser(id, user);
    return ResponseEntity.ok(updatedUser);
  }

  // Delete a user
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}

