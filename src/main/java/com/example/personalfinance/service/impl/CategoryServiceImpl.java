package com.example.personalfinance.service.impl;

import com.example.personalfinance.bean.responce.BaseResponeDto;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.CategoryRepository;
import com.example.personalfinance.repository.TransactionRepository;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
            return categoryRepository.findAllByUserId(user);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String addCategories(Category category, String userName) {
        try {
            User user = userRepository.findByEmail(userName).orElseThrow();
            category.setUserId(user);
            categoryRepository.save(category);
            return "success";
        } catch (UsernameNotFoundException e) {
            return e.getMessage();
        }
    }

    @Override
    public String deleteCategories(int category_TD) {
        try {
            Category entity = (Category) categoryRepository.getById(category_TD);
            categoryRepository.delete(entity);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }
     // !!!!!!!!!!!!!!!!NOTICING !!!!!!!!!!!!!!!!!!!!
     // deleteCategories and getCategoryById have to cast Object to Category to be functional
    @Override
    public Category getCategoryById(Integer id) {
        return (Category) categoryRepository.findById(id).orElseThrow();
    }

    @Override
    public ResponseEntity<BaseResponeDto> sortTransaction(Integer categoryId) {
        List<Transaction> transactions = transactionRepository.findByCategory(categoryId);
        return ResponseEntity.ok(new BaseResponeDto("success", transactions));
    }


}
