package org.dyplom.aplikacja.logic.repositories;

import org.dyplom.aplikacja.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
