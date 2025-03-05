package org.dyplom.aplikacja.model;

public class UserResponse {

  private String username;
  private String role;

  public UserResponse(String username, String role) {
    this.username = username;
    this.role = role;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getRole() {
    return role;
  }

  public void setRole(final String role) {
    this.role = role;
  }
}

