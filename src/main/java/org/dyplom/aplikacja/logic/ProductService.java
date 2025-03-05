package org.dyplom.aplikacja.logic;

import java.time.LocalDate;
import java.util.List;
import org.dyplom.aplikacja.logic.repositories.ProductRepository;
import org.dyplom.aplikacja.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  public Product getProductById(Long id) {
    return productRepository.findById(id).orElse(null);
  }

  public Product createProduct(Product product) {
    return productRepository.save(product);
  }

  public Product updateProduct(Long id, Product product) {
    product.setId(id);
    product.setLastUpdated(LocalDate.now());
    return productRepository.save(product);
  }

  public void deleteProduct(Long id) {
    productRepository.deleteById(id);
  }
}

