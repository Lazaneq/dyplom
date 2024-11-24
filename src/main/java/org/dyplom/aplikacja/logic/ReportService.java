package org.dyplom.aplikacja.logic;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.dyplom.aplikacja.exceptions.ReportGenerationException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

@Service
public class ReportService {

  // Generowanie raportu sprzedaży
  public byte[] generateSalesReport(LocalDate startDate, LocalDate endDate) {
    try {
      String reportData = fetchSalesData(startDate, endDate);
      return generatePdfReport(reportData, "Sales Report");
    } catch (Exception e) {
      throw new ReportGenerationException("Failed to generate sales report", e);
    }
  }

  // Generowanie raportu magazynowego
  public byte[] generateInventoryReport() {
    try {
      String reportData = fetchInventoryData();
      return generatePdfReport(reportData, "Inventory Report");
    } catch (Exception e) {
      throw new ReportGenerationException("Failed to generate inventory report", e);
    }
  }

  // Generowanie raportu użytkowników
  public byte[] generateUserReport() {
    try {
      String reportData = fetchUserData();
      return generatePdfReport(reportData, "User Report");
    } catch (Exception e) {
      throw new ReportGenerationException("Failed to generate user report", e);
    }
  }

  // Metoda do generowania PDF z iText 5.x
  private byte[] generatePdfReport(String reportData, String reportTitle) throws DocumentException, IOException {
    // Użycie ByteArrayOutputStream do przechowywania danych PDF w pamięci
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    Document document = new Document();

    // Tworzenie dokumentu PDF
    PdfWriter.getInstance(document, byteArrayOutputStream);

    document.open();
    // Dodawanie tytułu i danych raportu do dokumentu PDF
    document.add(new Paragraph(reportTitle));
    document.add(new Paragraph(reportData));
    document.close();

    // Zwracanie danych PDF jako tablica bajtów
    return byteArrayOutputStream.toByteArray();
  }

  // Przykładowa metoda pobierająca dane sprzedaży
  private String fetchSalesData(LocalDate startDate, LocalDate endDate) {
    // Logika pobierania danych sprzedaży z bazy danych
    return "Sales data from " + startDate + " to " + endDate;
  }

  // Przykładowa metoda pobierająca dane magazynowe
  private String fetchInventoryData() {
    return "Inventory data";
  }

  // Przykładowa metoda pobierająca dane użytkowników
  private String fetchUserData() {
    return "User data";
  }
}
