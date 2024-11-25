package com.example.personalfinance.repository;

import com.example.personalfinance.entity.Goals;
import com.example.personalfinance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing 'Goals' entities.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository // Marks this interface as a Spring Data repository
public interface GoalsRepository extends JpaRepository<Goals, Long> {

    /**
     * Finds all goals associated with a specific user.
     *
     * @param user The user whose goals are to be retrieved.
     * @return A list of goals belonging to the specified user.
     */
    List<Goals> findAllByUser(User user);
}
