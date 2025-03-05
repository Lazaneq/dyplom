package org.dyplom.aplikacja.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.dyplom.aplikacja.logic.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Report Controller")
@RestController
@RequestMapping("/reports")
public class ReportController {

  private final ReportService reportService;

  public ReportController(ReportService reportService) {
    this.reportService = reportService;
  }

  @GetMapping("/users")
  public ResponseEntity<byte[]> generateUserReport() {
    byte[] report = reportService.generateUserReport();
    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=user_report.pdf")
        .body(report);
  }

  @GetMapping("/inventory")
  public ResponseEntity<byte[]> generateInventoryReport() {
    byte[] report = reportService.generateProductReport();
    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=inventory_report.pdf")
        .body(report);
  }

  @GetMapping("/tasks")
  public ResponseEntity<byte[]> generateTaskReport(@RequestParam(name = "startDate") LocalDate from,
      @RequestParam(name = "endDate") LocalDate to) {
    byte[] report = reportService.generateTaskReport(from, to);
    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=task_report.pdf")
        .body(report);
  }
}
