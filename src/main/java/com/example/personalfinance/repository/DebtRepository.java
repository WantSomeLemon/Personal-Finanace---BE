package com.example.personalfinance.repository;

import com.example.personalfinance.entity.Debt;
import com.example.personalfinance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Integer> {
    List<Debt> findAllByUser(User user);
    List<Debt> findAllByUserOrderByDueDateAsc(User user);
    List<Debt> findAllByUserOrderByAmountDesc(User user);
}
