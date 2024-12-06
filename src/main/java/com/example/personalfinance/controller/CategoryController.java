package com.example.personalfinance.controller;


import java.util.List;

import com.example.personalfinance.exception.user.UserNotFoundException;
import com.example.personalfinance.exception.categories.CategoryAlreadyExistsException;
import com.example.personalfinance.exception.categories.CategoryDeleteFailedException;
import com.example.personalfinance.exception.categories.CategoryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final JWTGenerator jwtGenerator;

    @GetMapping
    public ResponseEntity<BaseResponse> getCategories(@RequestHeader(value = "Authorization") String token) {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        try {
            List<Category> categories = categoryService.getCategoriesByUserName(userName);
            return ResponseEntity.ok(new BaseResponse("success", categories));
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse("User not found"));
        }
    }

    @PostMapping
    public ResponseEntity<BaseResponse> addCategories(@RequestHeader(value = "Authorization") String token,
                                                      @RequestBody Category category) {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        try {
            categoryService.addCategories(category, userName);
            return ResponseEntity.ok(new BaseResponse("success", categoryService.getCategoryById(category.getCategoryId())));
        } catch (CategoryAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse("Category already exists"));
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse("User not found"));
        }
    }

    @DeleteMapping("/{category_id}")
    public ResponseEntity<BaseResponse> deleteCategories(@PathVariable String category_id) {
        try {
            categoryService.deleteCategories(Integer.parseInt(category_id));
            return ResponseEntity.ok(new BaseResponse("success"));
        } catch (CategoryNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse("Category not found"));
        } catch (CategoryDeleteFailedException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse("Failed to delete category"));
        }
    }

    @GetMapping("/total-transactions/{id}")
    public ResponseEntity<BaseResponse> sortTransaction(@RequestHeader(value = "Authorization") String token,
                                                        @PathVariable("id") Integer categoryId) {
        try {
            return categoryService.sortTransaction(categoryId);
        } catch (CategoryNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse("Category not found"));
        }
    }
}
