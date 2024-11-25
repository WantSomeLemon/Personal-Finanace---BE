package com.example.personalfinance.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final JWTGenerator jwtGenerator;

    /**
     * Endpoint to fetch all budgets of a user.
     * @param token The JWT token from the request header.
     * @return ResponseEntity containing a BaseResponse with all user budgets.
     */
    @GetMapping
    public ResponseEntity<BaseResponse> getAllBudgets(@RequestHeader(value = "Authorization") String token) {
        // Extracting user information from the JWT token.
        User user = userRepository.findByEmail(jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token)))
                .orElseThrow();
        List<Budget> budgets = budgetService.getAllBudgetByUser(user);
        return ResponseEntity.ok(new BaseResponse(budgets));
    }

    /**
     * Endpoint to fetch a particular budget by ID.
     * @param id The ID of the budget to retrieve.
     * @return ResponseEntity containing the budget or a not found status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Budget> getBudgetById(@PathVariable("id") Long id) {
        Budget budget = budgetService.getBudgetById(id).orElse(null);
        if (budget == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(budget);
    }

    /**
     * Endpoint for creating a new budget.
     * @param token Authorization token.
     * @param budgetRequest Request body containing budget details.
     * @return ResponseEntity with success or already existing response.
     */
    @PostMapping
    public ResponseEntity<BaseResponse> createBudgets(@RequestHeader(value = "Authorization") String token,
                                                      @RequestBody BudgetRequest budgetRequest) {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        if (!budgetService.hasAlready(userName, budgetRequest.getCategoryId())) {
            // Create budget if not already existing for the category
            Budget createdBudget = budgetService.createBudget(budgetRequest, userName);
            return ResponseEntity.ok().body(new BaseResponse(createdBudget));
        } else {
            return ResponseEntity.ok(new BaseResponse("Already exist"));
        }
    }

    /**
     * Endpoint to update an existing budget.
     * @param id The ID of the budget to update.
     * @param budgetRequest Request body containing updated budget details.
     * @return ResponseEntity with success or not found status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateBudget(@PathVariable("id") Long id,
                                                     @RequestBody BudgetRequest budgetRequest) {
        Budget existingBudget = budgetService.getBudgetById(id).orElse(null);
        if (existingBudget == null) {
            return ResponseEntity.notFound().build();
        }
        // Update the budget's category and amount based on request
        Category category = categoryService.getCategoryById(budgetRequest.getCategoryId());
        existingBudget.setCategory(category);
        existingBudget.setAmount(budgetRequest.getAmount());
        budgetService.updateBudget(existingBudget);
        return ResponseEntity.ok(new BaseResponse("update success", existingBudget));
    }

    /**
     * Endpoint to delete an existing budget.
     * @param id The ID of the budget to delete.
     * @return ResponseEntity with success or not found status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteBudget(@PathVariable("id") Long id) {
        Budget budget = budgetService.getBudgetById(id).orElse(null);
        if (budget == null) {
            return ResponseEntity.notFound().build();
        }
        budgetService.deleteBudget(id);
        return ResponseEntity.ok(new BaseResponse("delete success budgetId " + id, budget));
    }
}