package com.example.personalfinance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.personalfinance.entity.Budget;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;

/**
 * The repository interface for managing 'Budget' entities.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository // Marks this interface as a Spring Data repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    /**
     * Custom query to find all budgets associated with a specific user.
     * This query also calculates the available balance by subtracting the total transaction amount
     * from the budgeted amount for each category.
     *
     * @param userId The ID of the user whose budgets are to be retrieved.
     * @return A list of object arrays containing budget data and balance information.
     */
    @Query(value = """
        SELECT 
            b.id AS id, 
            b.amount AS amount, 
            b.category_id AS categoryId, 
            b.budget_status AS budgetStatus, 
            b.budget_balance AS budgetBalance,
            b.created_at AS createdAt,
            b.updated_at AS updatedAt,
            (b.amount - 
             COALESCE((SELECT SUM(t.amount) 
                       FROM transactions t 
                       WHERE t.user_id = ?1 
                       AND b.category_id = t.category_id), 0)
            ) AS balance
        FROM budgets b
        WHERE b.user_id = ?1
        """, nativeQuery = true)
    List<Object[]> findAllByUser(Integer userId);

    /**
     * Finds the budget associated with a specific category and user.
     *
     * @param category The category of the budget.
     * @param user The user whose budget is to be retrieved.
     * @return The budget for the given category and user, or null if not found.
     */
    Budget findByCategoryAndUser(Category category, User user);
}
