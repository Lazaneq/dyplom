package org.dyplom.aplikacja.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Resource {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  private String type;
  private String serialNumber;
  private String status;

  @ManyToMany(mappedBy = "resources", fetch = FetchType.EAGER)
  @JsonIgnore
  private Set<Task> tasks;

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

  public Set<Task> getTasks() {
    return tasks;
  }

  public void setTasks(final Set<Task> tasks) {
    this.tasks = tasks;
  }
}

