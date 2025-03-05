package org.dyplom.aplikacja.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dyplom.aplikacja.logic.repositories.UserRepository;
import org.dyplom.aplikacja.logic.UserService;
import org.dyplom.aplikacja.model.LoginRequest;
import org.dyplom.aplikacja.model.LoginResponse;
import org.dyplom.aplikacja.model.User;
import org.dyplom.aplikacja.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Auth Controller")
@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  UserService userService;

  @Autowired
  private UserRepository userRepository;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
      );
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String token = jwtTokenProvider.generateToken(authentication);

      User user = userRepository.findByUsername(loginRequest.getUsername())
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      String role = user.getRole();

      return ResponseEntity.ok(new LoginResponse(token, role));
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
  }
}
