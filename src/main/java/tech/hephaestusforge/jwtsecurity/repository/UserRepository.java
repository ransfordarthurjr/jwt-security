package tech.hephaestusforge.jwtsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.hephaestusforge.jwtsecurity.model.datasource.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
