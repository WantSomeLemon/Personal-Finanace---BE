package com.example.personalfinance.service.impl;

import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.exception.UserNotFoundException;
import com.example.personalfinance.exception.categories.CategoryAlreadyExistsException;
import com.example.personalfinance.exception.categories.CategoryDeleteFailedException;
import com.example.personalfinance.exception.categories.CategoryNotFoundException;
import com.example.personalfinance.repository.CategoryRepository;
import com.example.personalfinance.repository.TransactionRepository;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public List<Category> getCategoriesByUserName(String userName) {
        try {
            User user = userRepository.findByEmail(userName).orElseThrow(() -> new UserNotFoundException("User with email " + userName + " not found"));
            return categoryRepository.findAllByUserIdAndIsDeletedFalse(user);
        } catch (Exception e) {
            throw new CategoryNotFoundException("Categories not found for user " + userName);
        }
    }

    @Override
    public void addCategories(Category category, String userName) {
        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new UserNotFoundException("User with email " + userName + " not found"));
        
        if (categoryRepository.existsByNameAndUserId(category.getName(), user)) {
            throw new CategoryAlreadyExistsException("Category with name " + category.getName() + " already exists for user " + userName);
        }
        
        category.setUserId(user);
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategories(int categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Category with ID " + categoryId + " not found"));
        try {
            category.setDeleted(true);
            categoryRepository.save(category);
        } catch (Exception e) {
            throw new CategoryDeleteFailedException("Failed to delete category with ID " + categoryId);
        }
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category with ID " + id + " not found"));
    }

    @Override
    public ResponseEntity<BaseResponse> sortTransaction(Integer categoryId) {
        List<Transaction> transactions = transactionRepository.findByCategory(categoryId);
        if (transactions.isEmpty()) {
            throw new CategoryNotFoundException("No transactions found for category ID " + categoryId);
        }
        return ResponseEntity.ok(new BaseResponse("success", transactions));
    }


}
