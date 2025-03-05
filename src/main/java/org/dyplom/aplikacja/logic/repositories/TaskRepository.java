package org.dyplom.aplikacja.logic.repositories;

import java.util.List;
import org.dyplom.aplikacja.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

  List<Task> findByStatusOrAssignedElectricianId(String status, Integer electricianId);
}