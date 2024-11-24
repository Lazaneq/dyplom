package org.dyplom.aplikacja.logic;

import org.dyplom.aplikacja.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}

