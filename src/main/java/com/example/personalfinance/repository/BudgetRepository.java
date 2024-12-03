package com.example.personalfinance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.personalfinance.entity.Budget;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;


@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
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

    Budget findByCategoryAndUser(Category category, User user);

}
