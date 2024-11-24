package org.dyplom.aplikacja.logic;

import java.util.Optional;
import org.dyplom.aplikacja.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByUsername(String username);

}
