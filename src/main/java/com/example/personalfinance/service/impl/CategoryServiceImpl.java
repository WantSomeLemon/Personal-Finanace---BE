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
    
    /**
     * Fetches all categories associated with the user based on their email.
     *
     * @param userName The email of the user whose categories are to be fetched.
     * @return A list of Category entities associated with the specified user.
     */
    @Override
    public List<Category> getCategoriesByUserName(String userName) {
        try {
            // Fetch the user by email
            User user = userRepository.findByEmail(userName).orElseThrow();

            // Fetch all categories associated with the user
            return categoryRepository.findAllByUserId(user);
        } catch (Exception e) {
            // Return null if any error occurs while fetching categories
            return null;
        }
    }

    /**
     * Adds a new category for the user.
     *
     * @param category The category to be added.
     * @param userName The email of the user adding the category.
     */
    @Override
    public void addCategories(Category category, String userName) {
        // Fetch the user by their email
        User user = userRepository.findByEmail(userName).orElseThrow();

        // Set the user for the category and save it
        category.setUserId(user);
        categoryRepository.save(category);
    }

    /**
     * Updates an existing category.
     *
     * @param category The updated Category entity.
     */
    @Override
    public void updateCategories(Category category) {
        // Fetch the category by its ID
        Category cate = categoryRepository.findById(category.getCategoryId()).orElseThrow();

        // Update the category's properties
        cate.setName(category.getName());
        cate.setDescription(category.getDescription());
        cate.setType(category.getType());

        // Save the updated category
        categoryRepository.save(cate);
    }

    /**
     * Deletes a category by its ID.
     *
     * @param categoryId The ID of the category to be deleted.
     */
    @Override
    public void deleteCategories(int categoryId) {
        // Fetch the category by its ID
        Category entity = categoryRepository.getById(categoryId);

        // Delete the category from the repository
        categoryRepository.delete(entity);
    }

    /**
     * Fetches a category by its ID.
     *
     * @param id The ID of the category to be fetched.
     * @return The Category entity corresponding to the provided ID.
     */
    @Override
    public Category getCategoryById(Integer id) {
        // Fetch and return the category by its ID
        return categoryRepository.findById(id).orElseThrow();
    }

    /**
     * Sorts transactions based on the given category ID.
     *
     * @param categoryId The ID of the category used to filter transactions.
     * @return A ResponseEntity containing the sorted list of transactions.
     */
    @Override
    public ResponseEntity<BaseResponse> sortTransaction(Integer categoryId) {
        // Fetch the transactions associated with the given category ID
        List<Transaction> transactions = transactionRepository.findByCategory(categoryId);

        // Return the sorted transactions in the response
        return ResponseEntity.ok(new BaseResponse("Sort success", transactions));
    }
}
