package com.example.personalfinance.controller;


import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.personalfinance.service.CategoryService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final JWTGenerator jwtGenerator;

    @GetMapping
    public BaseResponse getCategories(@RequestHeader(value = "Authorization") String token)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        List<Category> categories = categoryService.getCategoriesByUserName(userName);
        return new BaseResponse("success", categories);
    }

    @PostMapping
    public BaseResponse addCategories(@RequestHeader(value = "Authorization") String token,
                                    @RequestBody Category category)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        categoryService.addCategories(category, userName);
        return new BaseResponse(categoryService.addCategories(category, userName), null);
    }

    @DeleteMapping("/{category_id}")
    public BaseResponse deleteCourse(@PathVariable String category_id)
    {
        return new BaseResponse(categoryService.deleteCategories(Integer.parseInt(category_id)));
    }

    @GetMapping("/total-transactions/{id}")
    public ResponseEntity<BaseResponse> sortTransaction(@RequestHeader(value = "Authorization") String token,
                                                        @PathVariable("id") Integer categorId)
    {
        return categoryService.sortTransaction(categorId);
    }
}
