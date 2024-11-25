package com.example.personalfinance.repository;

import com.example.personalfinance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Marks this interface as a Spring Data repository for dependency injection.
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their email.
     *
     * @param email The email address of the user.
     * @return An Optional containing the user if found, otherwise an empty Optional.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the given email address.
     *
     * @param email The email address to check.
     * @return True if a user exists with the given email, otherwise false.
     */
    Boolean existsByEmail(String email);
}
