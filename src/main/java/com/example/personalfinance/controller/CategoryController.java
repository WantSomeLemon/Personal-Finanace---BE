package com.example.personalfinance.controller;


import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final JWTGenerator jwtGenerator;

    /**
     * Endpoint to fetch all categories associated with the authenticated user.
     * @param token The JWT token from the request header.
     * @return ResponseEntity containing a BaseResponse with a list of categories.
     */
    @GetMapping
    public ResponseEntity<BaseResponse> getCategories(@RequestHeader(value = "Authorization") String token) {
        // Extracting the username from the JWT token.
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        // Retrieve the categories associated with the user.
        List<Category> categories = categoryService.getCategoriesByUserName(userName);
        return ResponseEntity.ok(new BaseResponse(categories));
    }

    /**
     * Endpoint to add a new category.
     * @param token The JWT token from the request header.
     * @param category The category data to add.
     * @return ResponseEntity with a success or failure response.
     */
    @PostMapping
    public ResponseEntity<BaseResponse> addCategories(@RequestHeader(value = "Authorization") String token,
                                                      @RequestBody Category category) {
        try {
            // Extract the username from the JWT token.
            String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
            // Add the category for the user.
            categoryService.addCategories(category, userName);
            return ResponseEntity.ok().body(new BaseResponse("Add Success", category));
        } catch (UsernameNotFoundException e) {
            // Handle case when user is not found.
            return ResponseEntity.badRequest().body(new BaseResponse("User Not Found"));
        }
    }

    /**
     * Endpoint to update an existing category.
     * @param token The JWT token from the request header.
     * @param category The updated category data.
     * @return ResponseEntity with success response and updated category.
     */
    @PutMapping
    public ResponseEntity<BaseResponse> updateCategories(@RequestHeader(value = "Authorization") String token,
                                                         @RequestBody Category category) {
        // Update the category using the provided data.
        categoryService.updateCategories(category);
        return ResponseEntity.ok().body(new BaseResponse("Update Success", categoryService.getCategoryById(category.getCategoryId())));
    }

    /**
     * Endpoint to delete a category by ID.
     * @param categoryId The ID of the category to delete.
     * @return ResponseEntity with success response and the deleted category data.
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<BaseResponse> deleteCourse(@PathVariable("categoryId") int categoryId) {
        // Delete the category by ID.
        categoryService.deleteCategories(categoryId);
        return ResponseEntity.ok().body(new BaseResponse("Delete Success", categoryService.getCategoryById(categoryId)));
    }

    /**
     * Endpoint to sort transactions within a category.
     * @param token The JWT token from the request header.
     * @param categoryId The ID of the category to sort transactions for.
     * @return ResponseEntity with success message.
     */
    @GetMapping("/total-transactions/{id}")
    public ResponseEntity<BaseResponse> sortTransaction(@RequestHeader(value = "Authorization") String token,
                                                        @PathVariable("id") Integer categoryId) {
        // Sort transactions for the given category ID.
        categoryService.sortTransaction(categoryId);
        return ResponseEntity.ok().body(new BaseResponse("Sort Success"));
    }
}