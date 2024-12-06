package com.example.personalfinance.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import com.example.personalfinance.exception.budget.*;
import org.springframework.stereotype.Service;

import com.example.personalfinance.bean.request.BudgetRequest;
import com.example.personalfinance.entity.Budget;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.BudgetRepository;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.BudgetService;
import com.example.personalfinance.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final CategoryService categoryService;
    private final UserRepository userRepository;

    @Override
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    @Override
    public List<Budget> getAllBudgetByUser(User user) {
        List<Object[]> results = budgetRepository.findAllByUserIsDeletedFalse(user.getUserId());

        return mapResultsToBudgets(results, user);
    }

    @Override
    public Optional<Budget> getBudgetById(Long id) {
        return budgetRepository.findById(id);
    }

    @Override
    public Budget createBudget(BudgetRequest budgetRequest, String userName) {
        if (hasAlready(userName, budgetRequest.getCategoryId())) {
            throw new BudgetAlreadyExistsException("Budget for category ID " + budgetRequest.getCategoryId() + " already exists");
        }
        try {
            Category category = categoryService.getCategoryById(budgetRequest.getCategoryId());
            User user = userRepository.findByEmail(userName).orElseThrow(() ->
                    new RuntimeException("User not found with username: " + userName));
            Budget budget = new Budget(category, budgetRequest.getAmount(), user, 0L, 0L);
            return budgetRepository.save(budget);
        } catch (Exception ex) {
            throw new BudgetCreationException("Failed to create budget: " + ex.getMessage());
        }
    }
    
    @Override
    public Budget updateBudget(Budget budget) {
        try {
            if (!budgetRepository.existsById(budget.getId())) {
                throw new BudgetNotFoundException("Budget with ID " + budget.getId() + " not found");
            }
            return budgetRepository.save(budget);
        } catch (Exception ex) {
            throw new BudgetUpdateException("Failed to update budget: " + ex.getMessage());
        }
    }
    
    @Override
    public void deleteBudget(Long id) {
        try {
            Budget budget = budgetRepository.findById(id)
                    .orElseThrow(() -> new BudgetNotFoundException("Budget with ID " + id + " not found"));
            budget.setDeleted(true);
            budgetRepository.save(budget);
        } catch (Exception ex) {
            throw new BudgetDeletionException("Failed to delete budget: " + ex.getMessage());
        }
    }

    @Override
    public boolean hasAlready(String userName, int categoryId) {
        User user = userRepository.findByEmail(userName).orElseThrow();
        List<Object[]> results = budgetRepository.findAllByUserIsDeletedFalse(user.getUserId());

        List<Budget> budgetList = mapResultsToBudgets(results, user);
        
        return budgetList.stream().anyMatch(b -> b.getCategory().getCategoryId() == categoryId);
    }


    private List<Budget> mapResultsToBudgets(List<Object[]> results, User user) {
        return results.stream().map(result -> {
            Budget budget = new Budget();
            // Set the budget properties using the result array
            budget.setId(((Number) result[0]).longValue()); // Safely cast to Long
            budget.setAmount(((Number) result[1]).doubleValue()); // Safely cast to Double
            budget.setCategory(categoryService.getCategoryById(((Number) result[2]).intValue())); // Fetch the Category by ID
            budget.setUsed(((Number) result[3]).doubleValue()); // Safely cast to Double
            budget.setBalance(((Number) result[4]).doubleValue()); // Safely cast to Double

            // Map createdAt and updatedAt timestamps
            budget.setCreatedAt(((Timestamp) result[5]).toLocalDateTime()); // Convert Timestamp to LocalDateTime
            budget.setUpdatedAt(((Timestamp) result[6]).toLocalDateTime()); // Convert Timestamp to LocalDateTime

            // Set the associated User
            budget.setUser(user);

            return budget;
        }).toList(); // Collect and return the list of Budget objects
    }
}
