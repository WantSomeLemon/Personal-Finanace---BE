package com.example.personalfinance.service;

import com.example.personalfinance.bean.request.BudgetRequest;
import com.example.personalfinance.entity.Budget;
import com.example.personalfinance.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BudgetService {

    /**
     * Retrieves all budgets in the system.
     *
     * @return a list of all Budget objects in the system
     */
    List<Budget> getAllBudgets();

    /**
     * Retrieves all budgets associated with a specific user.
     *
     * @param user the user whose budgets are to be retrieved
     * @return a list of Budget objects associated with the user
     */
    List<Budget> getAllBudgetByUser(User user);

    /**
     * Retrieves a specific budget by its ID.
     *
     * @param id the ID of the budget to retrieve
     * @return an Optional containing the Budget object if found, empty if not
     */
    Optional<Budget> getBudgetById(Long id);

    /**
     * Creates a new budget based on the provided budget request and associates it with the specified user.
     *
     * @param budgetRequest the request containing the budget details
     * @param userName the username of the user who owns the budget
     * @return the created Budget object
     */
    Budget createBudget(BudgetRequest budgetRequest, String userName);

    /**
     * Updates the details of an existing budget.
     *
     * @param budget the budget object containing the updated details
     * @return the updated Budget object
     */
    Budget updateBudget(Budget budget);

    /**
     * Deletes the budget with the specified ID from the system.
     *
     * @param id the ID of the budget to delete
     */
    void deleteBudget(Long id);

    /**
     * Checks if the user has already created a budget for the specified category.
     *
     * @param userName the username of the user to check
     * @param categoryId the ID of the category to check
     * @return true if the user has already created a budget for the category, false otherwise
     */
    boolean hasAlready(String userName, int categoryId);
}
