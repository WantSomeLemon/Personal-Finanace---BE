package com.example.personalfinance.service.impl;

import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.TransactionRepository;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServeImpl implements DashboardService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public List<Map<String, Object>> getMonthlyData(String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow();
        return convertMonthlyData(transactionRepository.getMonthlyData(user.getUserId()));
    }

    @Override
    public List<Map<String, Object>> convertMonthlyData(List<Object[]> queryResult) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> convertThisMonthExpenses(List<Object[]> queryResult) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> convertThisMonthIncome(List<Object[]> queryResult) {
        return List.of();
    }

    @Override
    public Map<String, Object> convertThisMonthTotalIncomeAndExpenses(List<Object[]> queryResult) {
        return Map.of();
    }

    @Override
    public List<Map<String, Object>> getThisMonthExpenses(String userName) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> getThisMonthIncome(String userName) {
        return List.of();
    }

    @Override
    public Map<String, Object> getThisMonthTotalIncomeAndExpenses(String userName) {
        return Map.of();
    }
}
