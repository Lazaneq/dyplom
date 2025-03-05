package org.dyplom.aplikacja.logic;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.Element;
import java.util.stream.Collectors;
import org.dyplom.aplikacja.exceptions.ReportGenerationException;
import org.dyplom.aplikacja.logic.repositories.ProductRepository;
import org.dyplom.aplikacja.logic.repositories.TaskRepository;
import org.dyplom.aplikacja.logic.repositories.UserRepository;
import org.dyplom.aplikacja.model.Product;
import org.dyplom.aplikacja.model.Task;
import org.dyplom.aplikacja.model.User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Service
public class ReportService {

  private final TaskRepository taskRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  public ReportService(final TaskRepository taskRepository, final UserRepository userRepository, final ProductRepository productRepository) {
    this.taskRepository = taskRepository;
    this.userRepository = userRepository;
    this.productRepository = productRepository;
  }

  public byte[] generateUserReport() {
    try {
      List<User> users = fetchUserData();
      return generateUserPdfReport(users);
    } catch (Exception e) {
      throw new ReportGenerationException("Failed to generate user report", e);
    }
  }

  public byte[] generateProductReport() {
    try {
      List<Product> products = fetchInventoryData();
      return generateProductPdfReport(products);
    } catch (Exception e) {
      throw new ReportGenerationException("Failed to generate inventory report", e);
    }
  }

  public byte[] generateTaskReport(LocalDate startDate, LocalDate endDate) {
    try {
      List<Task> tasks = fetchTaskData(startDate, endDate);
      return generateTaskPdfReport(tasks, startDate, endDate);
    } catch (Exception e) {
      throw new ReportGenerationException("Failed to generate task report", e);
    }
  }

  private byte[] generateUserPdfReport(List<User> users) throws DocumentException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    Document document = new Document();
    PdfWriter.getInstance(document, byteArrayOutputStream);

    document.open();
    document.add(new Paragraph("Raport Uzytkownikow"));

    PdfPTable userTable = new PdfPTable(3);
    userTable.setWidthPercentage(100);
    userTable.setSpacingBefore(10f);

    PdfPCell cell = new PdfPCell(new Paragraph("Uzytkownicy"));
    cell.setColspan(3);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    userTable.addCell(cell);

    userTable.addCell("ID");
    userTable.addCell("Nazwa uzytkownika");
    userTable.addCell("Rola");

    for (User user : users) {
      userTable.addCell(user.getId().toString());
      userTable.addCell(user.getUsername());
      userTable.addCell(user.getRole());
    }

    document.add(userTable);
    document.close();

    return byteArrayOutputStream.toByteArray();
  }

  private byte[] generateProductPdfReport(List<Product> products) throws DocumentException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    Document document = new Document();
    PdfWriter.getInstance(document, byteArrayOutputStream);

    document.open();
    document.add(new Paragraph("Raport Magazynu"));

    PdfPTable productTable = new PdfPTable(4);
    productTable.setWidthPercentage(100);
    productTable.setSpacingBefore(10f);

    PdfPCell cell = new PdfPCell(new Paragraph("Produkty"));
    cell.setColspan(4);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    productTable.addCell(cell);

    productTable.addCell("ID");
    productTable.addCell("Nazwa");
    productTable.addCell("Ilosc");
    productTable.addCell("Cena");

    for (Product product : products) {
      productTable.addCell(product.getId().toString());
      productTable.addCell(product.getName());
      productTable.addCell(String.valueOf(product.getQuantity()));
      productTable.addCell(product.getPrice() + " zl");
    }

    document.add(productTable);
    document.close();

    return byteArrayOutputStream.toByteArray();
  }

  private byte[] generateTaskPdfReport(List<Task> tasks,
      LocalDate startDate, LocalDate endDate) throws DocumentException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    Document document = new Document();
    PdfWriter.getInstance(document, byteArrayOutputStream);

    document.open();
    document.add(new Paragraph("Raport Zadan"));

    document.add(new Paragraph("Data generowania raportu: " + LocalDate.now()));
    document.add(new Paragraph("Zakres dat: " + startDate + " do " + endDate));

    PdfPTable taskTable = new PdfPTable(5);
    taskTable.setWidthPercentage(100);
    taskTable.setSpacingBefore(10f);

    PdfPCell cell = new PdfPCell(new Paragraph("Zadania"));
    cell.setColspan(5);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    taskTable.addCell(cell);

    taskTable.addCell("ID");
    taskTable.addCell("Tytul");
    taskTable.addCell("Stan");
    taskTable.addCell("Przypisany elektryk");
    taskTable.addCell("Data");

    for (Task task : tasks) {
      taskTable.addCell(task.getId().toString());
      taskTable.addCell(task.getTitle());
      taskTable.addCell(translate(task));
      taskTable.addCell(task.getAssignedElectrician() != null ? task.getAssignedElectrician().getUsername() : "N/A");
      taskTable.addCell(task.getDueDate() != null ? task.getDueDate().toString() : "N/A");
    }

    document.add(taskTable);
    document.close();

    return byteArrayOutputStream.toByteArray();
  }

  private List<User> fetchUserData() {
    return userRepository.findAll();
  }

  private List<Product> fetchInventoryData() {
    return productRepository.findAll();
  }

  private List<Task> fetchTaskData(LocalDate startDate, LocalDate endDate) {
    return taskRepository.findAll().stream()
        .filter(task -> task.getDueDate() != null && task.getDueDate().isAfter(startDate) && task.getDueDate()
            .isBefore(endDate))
        .collect(Collectors.toList());
  }
  private String translate(Task task) {
    switch (task.getStatus()){
      case "NEW" -> {
        return "Nowy";
      }
      case "COMPLETED" -> {
        return "Zakończony";
      }
      case "IN PROGRESS" -> {
        return "W trakcie";
      }
      default -> {
        return "Błąd";
      }
    }
  }
}
