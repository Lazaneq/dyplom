package org.dyplom.aplikacja.controller;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.dyplom.aplikacja.logic.CustomerOrderService;
import org.dyplom.aplikacja.model.CustomerOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/orders")
public class CustomerOrderController {

  private final String UPLOAD_DIR = "src/main/resources/static/uploads/";

  @Autowired
  private CustomerOrderService customerOrderService;

  @GetMapping
  public List<CustomerOrder> getAllOrders() {
    List<CustomerOrder> orders = customerOrderService.getAllOrders();
    System.out.println("Fetched orders: " + orders);
    return orders;
  }

  @PostMapping
  public ResponseEntity<CustomerOrder> createOrder(
      @RequestParam("title") String title,
      @RequestParam("description") String description,
      @RequestParam("status") String status,
      @RequestParam(value = "images", required = false) MultipartFile[] images) { // Changed to accept multiple images
    try {
      // Zapisz wszystkie obrazy
      List<String> imagePaths = new ArrayList<>();
      if (images != null) {
        for (MultipartFile image : images) {
          String imagePath = saveImage(image);
          imagePaths.add(imagePath);
        }
      }

      // Stwórz nowe zlecenie
      CustomerOrder order = new CustomerOrder();
      order.setTitle(title);
      order.setDescription(description);
      order.setStatus(status);
      order.setImagePath(String.join(",", imagePaths)); // Join paths into a single string

      customerOrderService.createOrder(order);

      return ResponseEntity.status(HttpStatus.CREATED).body(order);
    } catch (Exception e) {
      e.printStackTrace(); // Dodałem logowanie błędów
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<CustomerOrder> updateOrder(@PathVariable Long id, @RequestBody CustomerOrder orderDetails) {
    CustomerOrder updatedOrder = customerOrderService.updateOrder(id, orderDetails);
    return ResponseEntity.ok(updatedOrder);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
    customerOrderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/images/{imageName}")
  public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
    try {
      Path path = Paths.get(UPLOAD_DIR + imageName);
      Resource resource = new UrlResource(path.toUri());

      if (resource.exists() || resource.isReadable()) {
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG) // lub inny typ, jeśli używasz
            .body(resource);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (MalformedURLException e) {
      return ResponseEntity.notFound().build();
    }
  }

  private String saveImage(MultipartFile image) throws Exception {
    // Tworzenie katalogu, jeśli nie istnieje
    Files.createDirectories(Paths.get(UPLOAD_DIR));

    // Tworzenie pełnej ścieżki do pliku
    Path path = Paths.get(UPLOAD_DIR + image.getOriginalFilename());
    Files.write(path, image.getBytes());

    // Zwracanie względnej ścieżki do pliku
    return path.getFileName().toString(); // Return only the filename for storage
  }
}
