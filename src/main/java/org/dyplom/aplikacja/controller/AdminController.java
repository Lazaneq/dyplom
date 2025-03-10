package org.dyplom.aplikacja.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.dyplom.aplikacja.logic.UserService;
import org.dyplom.aplikacja.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/admin")
public class AdminController {

  @Autowired
  private UserService userService;

  @PostMapping("/register")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> registerUser(@RequestBody User user) {
    userService.registerUser(user);
    return ResponseEntity.ok("User registered successfully");
  }
}
