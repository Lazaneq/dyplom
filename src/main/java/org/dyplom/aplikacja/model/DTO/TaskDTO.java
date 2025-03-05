package org.dyplom.aplikacja.model.DTO;

import java.time.LocalDate;

public class TaskDTO {
  private Long id;
  private String title;
  private String status;
  private LocalDate date;

  public TaskDTO(Long id, String title, String status) {
    this.id = id;
    this.title = title;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(final String status) {
    this.status = status;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(final LocalDate date) {
    this.date = date;
  }
}