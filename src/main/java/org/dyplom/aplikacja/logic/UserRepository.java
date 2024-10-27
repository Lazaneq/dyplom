package org.dyplom.aplikacja.logic;

import org.dyplom.aplikacja.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
