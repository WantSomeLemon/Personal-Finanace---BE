package com.example.personalfinance.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.personalfinance.exception.budget.BudgetAlreadyExistsException;
import com.example.personalfinance.exception.budget.BudgetCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.personalfinance.bean.request.BudgetRequest;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.Budget;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.BudgetService;
import com.example.personalfinance.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetService budgetService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final JWTGenerator jwtGenerator;

    @GetMapping
    public ResponseEntity<BaseResponse> getAllBudgets(@RequestHeader(value = "Authorization") String token) {
        try {
            User user = userRepository.findByEmail(jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token)))
                    .orElseThrow(() -> new RuntimeException("User not found"));
            List<Budget> budgets = budgetService.getAllBudgetByUser(user);
            return ResponseEntity.ok(new BaseResponse("success", budgets));
        } catch (Exception ex) {
            throw new RuntimeException("Failed to fetch budgets: " + ex.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getBudgetById(@PathVariable("id") Long id) {
        Budget budget = budgetService.getBudgetById(id).orElse(null);
        if (budget == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse("budget not found"));
        }
        return ResponseEntity.ok(new BaseResponse("success", budget));

    }

    @PostMapping
    public ResponseEntity<BaseResponse> createBudgets(@RequestHeader(value = "Authorization") String token,
                                                      @RequestBody BudgetRequest budgetRequest) {
        try {
            String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
            Budget createdBudget = budgetService.createBudget(budgetRequest, userName);
            return ResponseEntity.ok(new BaseResponse("success", createdBudget));
        } catch (BudgetAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse("Budget already exists"));
        } catch (BudgetCreationException ex) {
            throw ex;
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateBudget(@PathVariable("id") Long id,
                                                     @RequestBody BudgetRequest budgetRequest) {
        Budget existingBudget = budgetService.getBudgetById(id).orElse(null);
        if (existingBudget == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse("budget not found"));
        }
        Category category = categoryService.getCategoryById(budgetRequest.getCategoryId());
        existingBudget.setCategory(category);
        existingBudget.setAmount(budgetRequest.getAmount());
        budgetService.updateBudget(existingBudget);
        return ResponseEntity.ok(new BaseResponse("success", existingBudget));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteBudget(@PathVariable("id") Long id) {
        Budget budget = budgetService.getBudgetById(id).orElse(null);
        if (budget == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse("budget not found"));
        }
        budgetService.deleteBudget(id);
        return ResponseEntity.ok(new BaseResponse("success"));
    }
    
}
