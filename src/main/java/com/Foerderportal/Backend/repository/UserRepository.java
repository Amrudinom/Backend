package com.Foerderportal.Backend.repository;

import com.Foerderportal.Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAuth0Id(String auth0Id);
    boolean existsByEmail(String email);
}