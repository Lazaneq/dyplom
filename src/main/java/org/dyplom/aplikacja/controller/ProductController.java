package org.dyplom.aplikacja.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.dyplom.aplikacja.logic.ProductService;
import org.dyplom.aplikacja.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Product Controller")
@RestController
@RequestMapping("/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping
  public ResponseEntity<List<Product>> getAllProducts() {
    return ResponseEntity.ok(productService.getAllProducts());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.getProductById(id));
  }

  @PostMapping
  public ResponseEntity<?> createProduct(@RequestBody Product product) {
    try {
      // Walidacja, aby ilość produktów nie była ujemna
      if (product.getQuantity() < 0) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity cannot be negative.");
      }
      Product createdProduct = productService.createProduct(product);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating product: " + e.getMessage());
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
    try {
      if (product.getQuantity() < 0) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity cannot be negative.");
      }
      Product updatedProduct = productService.updateProduct(id, product);
      return ResponseEntity.ok(updatedProduct);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating product: " + e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}


