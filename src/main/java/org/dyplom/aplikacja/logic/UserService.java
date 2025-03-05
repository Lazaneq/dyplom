package org.dyplom.aplikacja.logic;
import org.dyplom.aplikacja.logic.repositories.UserRepository;
import org.dyplom.aplikacja.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public User getUserById(Integer id) {
    return userRepository.findById(Math.toIntExact(id)).orElse(null);
  }

  public User createUser(User user) {
    return userRepository.save(user);
  }

  public User updateUser(Integer id, User user) {
    int uId = id.intValue();
    User existingUser = userRepository.findById(uId).orElseThrow(() -> new RuntimeException("User not found"));

    existingUser.setUsername(user.getUsername());

    if (user.getPassword() != null && !user.getPassword().isEmpty()) {
      String encodedPassword = passwordEncoder.encode(user.getPassword());
      existingUser.setPassword(encodedPassword);
    }

    return userRepository.save(existingUser);
  }

  public void deleteUser(Integer id) {
    userRepository.deleteById(id);
  }

  public void registerUser(User userDto) {
    User user = new User();
    user.setUsername(userDto.getUsername());
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user.setRole(userDto.getRole());
    userRepository.save(user);
  }
}