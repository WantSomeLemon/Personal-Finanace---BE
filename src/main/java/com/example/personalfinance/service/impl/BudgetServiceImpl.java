package com.example.personalfinance.service.impl;

import com.example.personalfinance.bean.request.BudgetRequest;
import com.example.personalfinance.entity.Budget;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.BudgetRepository;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.BudgetService;
import com.example.personalfinance.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marks this class as a Spring service for dependency injection.
@RequiredArgsConstructor // Generates a constructor to inject required dependencies.
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository; // Budget repository for CRUD operations
    private final CategoryService categoryService; // Service to interact with categories
    private final UserRepository userRepository; // User repository to find users by email

    /**
     * Helper method to convert raw query results into a list of Budget objects.
     *
     * @param results Raw query results (Object[]).
     * @param user The user associated with these budgets.
     * @return A list of Budget objects.
     */
    private List<Budget> mapResultsToBudgets(List<Object[]> results, User user) {
        return results.stream().map(result -> {
            Budget budget = new Budget();
            // Set the budget properties using the result array
            budget.setId((Long) result[0]);
            budget.setAmount((Double) result[1]);
            budget.setCategory(categoryService.getCategoryById((Integer) result[2])); // Set the associated category
            budget.setUser(user); // Set the associated user
            budget.setUsed((Double) result[3]);
            budget.setBalance((Double) result[4]);
            return budget;
        }).toList(); // Collect and return the list of Budget objects
    }

    /**
     * Fetches all budgets from the repository.
     *
     * @return A list of all Budget entities.
     */
    @Override
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll(); // Fetches all budgets from the repository
    }

    /**
     * Fetches all budgets for a specific user.
     *
     * @param user The user whose budgets are to be fetched.
     * @return A list of Budget entities associated with the specified user.
     */
    @Override
    public List<Budget> getAllBudgetByUser(User user) {
        // Query the repository to fetch budgets for the given user
        List<Object[]> results = budgetRepository.findAllByUser(user.getUserId());

        // Use the helper method to map the raw query results into a list of Budget objects
        return mapResultsToBudgets(results, user);
    }

    /**
     * Fetches a budget by its ID.
     *
     * @param id The ID of the budget to be fetched.
     * @return An Optional containing the Budget if found, otherwise an empty Optional.
     */
    @Override
    public Optional<Budget> getBudgetById(Long id) {
        return budgetRepository.findById(id); // Fetch the budget from the repository using its ID
    }

    /**
     * Creates a new budget based on the provided budget request and the user's email.
     *
     * @param budgetRequest The request containing the details of the budget to be created.
     * @param userName The email of the user creating the budget.
     * @return The newly created Budget entity.
     */
    @Override
    public Budget createBudget(BudgetRequest budgetRequest, String userName) {
        // Fetch the category using the category service based on the category ID from the request
        Category category = categoryService.getCategoryById(budgetRequest.getCategoryId());

        // Fetch the user by their email from the user repository
        User user = userRepository.findByEmail(userName).orElseThrow();

        // Create a new Budget entity with the provided category, amount, and user
        Budget budget = new Budget(category, budgetRequest.getAmount(), user);

        // Save the budget to the repository and return the saved budget
        return budgetRepository.save(budget);
    }

    /**
     * Updates an existing budget.
     *
     * @param budget The Budget entity to be updated.
     * @return The updated Budget entity.
     */
    @Override
    public Budget updateBudget(Budget budget) {
        return budgetRepository.save(budget); // Save and return the updated budget
    }

    /**
     * Deletes a budget by its ID.
     *
     * @param id The ID of the budget to be deleted.
     */
    @Override
    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id); // Delete the budget by its ID
    }

    /**
     * Checks if the user has already created a budget for a specific category.
     *
     * @param userName The email of the user.
     * @param categoryId The ID of the category to check.
     * @return true if a budget already exists for the category, otherwise false.
     */
    @Override
    public boolean hasAlready(String userName, int categoryId) {
        // Fetch user by their email from the user repository
        User user = userRepository.findByEmail(userName).orElseThrow();

        // Query the repository to fetch budgets for the given user
        List<Object[]> results = budgetRepository.findAllByUser(user.getUserId());

        // Use the helper method to map the raw query results into a list of Budget objects
        List<Budget> budgetList = mapResultsToBudgets(results, user);

        // Check if there is already a budget for the specified category and return the result
        return budgetList.stream().anyMatch(b -> b.getCategory().getCategoryId() == categoryId);
    }
}
