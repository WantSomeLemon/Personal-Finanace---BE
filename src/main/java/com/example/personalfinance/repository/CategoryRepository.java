package com.example.personalfinance.repository;

import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository {
    public List<Category> findAllByUserId(User userId);
}
