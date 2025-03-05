package org.dyplom.aplikacja.model.DTO;

import java.util.Set;

public class ResourceDTO {
  private Long id;
  private String name;
  private String description;
  private String type;
  private String serialNumber;
  private String status;
  private Set<TaskDTO> assignedTasks;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(final String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(final String status) {
    this.status = status;
  }

  public Set<TaskDTO> getAssignedTasks() {
    return assignedTasks;
  }

  public void setAssignedTasks(final Set<TaskDTO> assignedTasks) {
    this.assignedTasks = assignedTasks;
  }
}