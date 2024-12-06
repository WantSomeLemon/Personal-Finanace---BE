package com.example.personalfinance.service.impl;

import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.entity.User;
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
            User user = userRepository.findByEmail(userName).orElseThrow();
            return categoryRepository.findAllByUserIdAndIsDeletedFalse(user);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void addCategories(Category category, String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow();
        category.setUserId(user);
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategories(int category_TD) {
        Category entity = categoryRepository.getById(category_TD);
        entity.setDeleted(true);
        categoryRepository.save(entity);
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    @Override
    public ResponseEntity<BaseResponse> sortTransaction(Integer categoryId) {
        List<Transaction> transactions = transactionRepository.findByCategory(categoryId);
        return ResponseEntity.ok(new BaseResponse("success", transactions));
    }


}
