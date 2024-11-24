package org.dyplom.aplikacja.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.dyplom.aplikacja.logic.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
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

  @GetMapping("/sales")
  public ResponseEntity<byte[]> generateSalesReport(
      @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
  ) {
    byte[] report = reportService.generateSalesReport(startDate, endDate);
    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=sales_report.pdf")
        .body(report);
  }

  @GetMapping("/inventory")
  public ResponseEntity<byte[]> generateInventoryReport() {
    byte[] report = reportService.generateInventoryReport();
    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=inventory_report.pdf")
        .body(report);
  }

  @GetMapping("/users")
  public ResponseEntity<byte[]> generateUserReport() {
    byte[] report = reportService.generateUserReport();
    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=user_report.pdf")
        .body(report);
  }
}
