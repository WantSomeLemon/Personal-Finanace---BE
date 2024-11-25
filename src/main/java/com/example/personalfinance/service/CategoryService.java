package com.example.personalfinance.service;

import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.entity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    /**
     * Retrieves all categories associated with a specific user.
     *
     * @param userName the username whose categories are to be retrieved
     * @return a list of Category objects associated with the user
     */
    List<Category> getCategoriesByUserName(String userName);

    /**
     * Adds a new category to the system and associates it with a specific user.
     *
     * @param category the category to be added
     * @param userName the username of the user who owns the category
     */
    void addCategories(Category category, String userName);

    /**
     * Updates the details of an existing category.
     *
     * @param category the category object containing the updated details
     */
    void updateCategories(Category category);

    /**
     * Deletes a category from the system based on its ID.
     *
     * @param category_TD the ID of the category to delete
     */
    void deleteCategories(int category_TD);

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category to retrieve
     * @return the Category object with the specified ID
     */
    Category getCategoryById(Integer id);

    /**
     * Sorts transactions based on the category ID.
     *
     * @param categoryId the ID of the category to sort transactions by
     * @return a ResponseEntity containing the result of the sorting operation
     */
    ResponseEntity<BaseResponse> sortTransaction(Integer categoryId);
}
