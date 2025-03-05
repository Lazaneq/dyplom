package org.dyplom.aplikacja.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.dyplom.aplikacja.logic.UserService;
import org.dyplom.aplikacja.model.User;
import org.dyplom.aplikacja.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="User Controller")
@RestController
@RequestMapping("/users")
public class UserController {

  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Autowired
  private UserService userService;

  @Description("Get all users")
  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Integer id) {
    User user = userService.getUserById(id);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(user);
  }

  @GetMapping("/me")
  public ResponseEntity<?> getCurrentUser(Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    User user = userService.getAllUsers().stream()
        .filter(u -> u.getUsername().equals(userDetails.getUsername()))
        .findFirst()
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return ResponseEntity.ok(new UserResponse(userDetails.getUsername(), user.getRole()));
  }

  @PostMapping
  public ResponseEntity<User> createUser(
      @RequestParam("name") String name,
      @RequestParam("password") String password,
      @RequestParam("role") String role
  ) {

    String hashedPassword = passwordEncoder.encode(password);

    User user = new User();
    user.setUsername(name);
    user.setPassword(hashedPassword);
    user.setRole(role);

    User createdUser = userService.createUser(user);
    return ResponseEntity.status(201).body(createdUser);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
    User updatedUser = userService.updateUser(id, user);
    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}

