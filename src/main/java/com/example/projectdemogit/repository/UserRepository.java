package com.example.projectdemogit.repository;

import com.example.projectdemogit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    List<User> findAllByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findAllByUsername(String username);
    Optional<User> findByPasswordResetToken(String token);

}
