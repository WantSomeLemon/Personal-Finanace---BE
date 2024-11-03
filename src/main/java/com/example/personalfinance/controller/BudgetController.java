package com.example.personalfinance.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetService budgetService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final JWTGenerator jwtGenerator;

    @GetMapping("api/budgets")
    public ResponseEntity<BaseResponse> getAllBudgets(@RequestHeader(value = "Authorization", defaultValue = "") String token) {
        User user = userRepository.findByEmail(jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token))).orElseThrow();
        List<Budget> budgets = budgetService.getAllBudgetByUser(user);
        return new ResponseEntity<>(new BaseResponse("success", budgets), HttpStatus.OK);
    }

    @GetMapping("/api/budget/{id}")
    public ResponseEntity<Budget> getBudgetById(@PathVariable("id") Long id) {
        Budget budget = budgetService.getBudgetById(id).orElse(null);
        if (budget == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @PostMapping("api/budgets")
    public ResponseEntity<BaseResponse> createBudgets(@RequestHeader(value = "Authorization", defaultValue = "") String token,
                                                      @RequestBody BudgetRequest budgetRequest) {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        if (!budgetService.hasAlready(userName, budgetRequest.getCategoryId())) {
            Budget createdBudget = budgetService.createBudget(budgetRequest, userName);
            return new ResponseEntity<>(new BaseResponse("success", createdBudget), HttpStatus.CREATED);
        } else {
            return ResponseEntity.ok(new BaseResponse("Already exist"));
        }
    }

    @PutMapping("api/budgets/{id}")
    public ResponseEntity<BaseResponse> updateBudget(@PathVariable("id") Long id,
                                                     @RequestBody BudgetRequest budgetRequest) {
        Budget existingBudget = budgetService.getBudgetById(id).orElse(null);
        if (existingBudget == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Category category = categoryService.getCategoryById(budgetRequest.getCategoryId());
        existingBudget.setCategory(category);
        existingBudget.setAmount(budgetRequest.getAmount());
        budgetService.updateBudget(existingBudget);
        return new ResponseEntity<>(new BaseResponse("success"), HttpStatus.OK);
    }

    @DeleteMapping("api/budgets/{id}")
    public ResponseEntity<BaseResponse> deleteBudget(@PathVariable("id") Long id) {
        Budget budget = budgetService.getBudgetById(id).orElse(null);
        if (budget == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        budgetService.deleteBudget(id);
        return ResponseEntity.ok(new BaseResponse("success"));
    }

    //test case
    @GetMapping("/budget")
    public ResponseEntity<List<Account>> getAllBudget() {
        List<Account> accounts = new ArrayList<>();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/budget/{id}")
    public ResponseEntity<List<Account>> getBudgetById(@PathVariable Integer id) {
        List<Account> accounts = new ArrayList<>();
        return ResponseEntity.ok(accounts);
    }
}
