package com.example.personalfinance.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.personalfinance.bean.request.BudgetRequest;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Budget;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.BudgetService;
import com.example.personalfinance.service.CategoryService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetService budgetService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final JWTGenerator jwtGenerator;

    /**
     * API Endpoint to fetch all budgets for the authenticated user.
     *
     * @param token Authorization token from the request header.
     * @return ResponseEntity containing the list of budgets or an error response.
     */
    @GetMapping
    public ResponseEntity<BaseResponse> getAllBudgets(@RequestHeader(value = "Authorization") String token) {
        try {
            // Extract the username from the token
            String username = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
            // Find the user by their email
            User user = userRepository.findByEmail(username).orElseThrow(() -> new NoSuchElementException("User not found"));
            // Get all budgets associated with the user
            List<Budget> budgets = budgetService.getAllBudgetByUser(user);
            return ResponseEntity.ok(new BaseResponse(budgets));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse("User not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse("An unexpected error occurred"));
        }
    }

    /**
     * API Endpoint to fetch a specific budget by its ID.
     *
     * @param id Budget ID.
     * @return ResponseEntity containing the budget or an error response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getBudgetById(@PathVariable("id") Long id) {
        try {
            // Fetch the budget by ID
            Budget budget = budgetService.getBudgetById(id).orElseThrow(() -> new NoSuchElementException("Budget not found"));
            return ResponseEntity.ok().body(new BaseResponse(budget));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse("Budget not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse("An unexpected error occurred"));
        }
    }

    /**
     * API Endpoint to create a new budget for the authenticated user.
     *
     * @param token Authorization token from the request header.
     * @param budgetRequest Request body containing budget details.
     * @return ResponseEntity containing the created budget or an error response.
     */
    @PostMapping
    public ResponseEntity<BaseResponse> createBudgets(@RequestHeader(value = "Authorization") String token,
                                                      @RequestBody BudgetRequest budgetRequest) {
        try {
            // Extract the username from the token
            String username = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
            // Check if the budget already exists for the user
            if (!budgetService.hasAlready(username, budgetRequest.getCategoryId())) {
                // Create a new budget
                Budget createdBudget = budgetService.createBudget(budgetRequest, username);
                return ResponseEntity.ok().body(new BaseResponse(createdBudget));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse("Budget already exists"));
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse("User not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse("An unexpected error occurred"));
        }
    }

    /**
     * API Endpoint to update an existing budget.
     *
     * @param id Budget ID.
     * @param budgetRequest Request body containing updated budget details.
     * @return ResponseEntity containing the updated budget or an error response.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateBudget(@PathVariable("id") Long id,
                                                     @RequestBody BudgetRequest budgetRequest) {
        try {
            // Fetch the existing budget
            Budget existingBudget = budgetService.getBudgetById(id).orElseThrow(() -> new NoSuchElementException("Budget not found"));
            // Update budget details
            Category category = categoryService.getCategoryById(budgetRequest.getCategoryId());
            existingBudget.setCategory(category);
            existingBudget.setAmount(budgetRequest.getAmount());
            Budget updatedBudget = budgetService.updateBudget(existingBudget);
            return ResponseEntity.ok(new BaseResponse("success", updatedBudget));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse("An unexpected error occurred"));
        }
    }

    /**
     * API Endpoint to delete an existing budget by its ID.
     *
     * @param id Budget ID.
     * @return ResponseEntity containing the result of the operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteBudget(@PathVariable("id") Long id) {
        try {
            // Fetch the budget to ensure it exists
            Budget budget = budgetService.getBudgetById(id).orElseThrow(() -> new NoSuchElementException("Budget not found"));
            // Delete the budget
            budgetService.deleteBudget(id);
            return ResponseEntity.ok(new BaseResponse("success", budget));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse("Budget not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse("An unexpected error occurred"));
        }
    }
}
