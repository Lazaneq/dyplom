package org.dyplom.aplikacja.logic.repositories;

import org.dyplom.aplikacja.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}

