package com.example.personalfinance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;

/**
 * Repository interface for managing 'Category' entities.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository // Marks this interface as a Spring Data repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    /**
     * Finds all categories associated with a specific user.
     *
     * @param userId The user whose categories are to be retrieved.
     * @return A list of categories belonging to the specified user.
     */
    List<Category> findAllByUserId(User userId);
}
