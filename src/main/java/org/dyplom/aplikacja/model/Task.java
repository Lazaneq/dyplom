package org.dyplom.aplikacja.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String description;
  private String status;
  private LocalDate dueDate;

  @ManyToOne
  @JoinColumn(name = "assigned_user_id", nullable = true)
  private User assignedElectrician;

  private String imagePath;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "task_resource",
      joinColumns = @JoinColumn(name = "task_id"),
      inverseJoinColumns = @JoinColumn(name = "resource_id")
  )
  private Set<Resource> resources;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(final String imagePath) {
    this.imagePath = imagePath;
  }

  public User getAssignedElectrician() {
    return assignedElectrician;
  }

  public void setAssignedElectrician(final User assignedElectrician) {
    this.assignedElectrician = assignedElectrician;
  }

  public Set<Resource> getResources() {
    return resources;
  }

  public void setResources(final Set<Resource> resources) {
    this.resources = resources;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(final LocalDate dueDate) {
    this.dueDate = dueDate;
  }
}
