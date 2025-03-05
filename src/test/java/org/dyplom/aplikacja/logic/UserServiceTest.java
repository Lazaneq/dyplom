package org.dyplom.aplikacja.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.dyplom.aplikacja.logic.repositories.UserRepository;
import org.dyplom.aplikacja.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getUserById() {
    User user = new User();
    user.setId(1);
    user.setUsername("testuser");

    when(userRepository.findById(1)).thenReturn(Optional.of(user));

    User foundUser = userService.getUserById(1);

    assertNotNull(foundUser);
    assertEquals("testuser", foundUser.getUsername());
  }

  @Test
  void createUser() {
    User user = new User();
    user.setUsername("testuser");
    user.setPassword("password");

    when(userRepository.save(user)).thenReturn(user);

    User createdUser = userService.createUser(user);

    assertNotNull(createdUser);
    assertEquals("testuser", createdUser.getUsername());
  }

  @Test
  void testUpdateUserPassword() {
    User existingUser = new User();
    existingUser.setId(1);
    existingUser.setUsername("user1");
    existingUser.setPassword("old_password");

    User newUser = new User();
    newUser.setPassword("new_password");

    when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
    when(passwordEncoder.encode(newUser.getPassword())).thenReturn("encoded_password");
    when(userRepository.save(existingUser)).thenReturn(existingUser);

    User updatedUser = userService.updateUser(1, newUser);

    assertNotNull(updatedUser);
    assertEquals("encoded_password", updatedUser.getPassword());
  }
}