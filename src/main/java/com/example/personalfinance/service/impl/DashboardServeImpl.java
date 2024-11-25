package com.example.personalfinance.service.impl;

import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.TransactionRepository;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServeImpl implements DashboardService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Retrieves monthly transaction data for the given user.
     *
     * @param userName The email of the user whose monthly data is to be fetched.
     * @return A list of maps containing monthly data (expenses and income) for the user.
     */
    @Override
    public List<Map<String, Object>> getMonthlyData(String userName) {
        // Fetch the user by their email
        User user = userRepository.findByEmail(userName).orElseThrow();

        // Fetch the monthly data for the user from the transaction repository
        return convertMonthlyData(transactionRepository.getMonthlyData(user.getUserId()));
    }

    /**
     * Converts query results into a list of maps representing monthly data (expenses and income).
     *
     * @param queryResult Raw query result from the repository (list of Object[]).
     * @return A list of maps where each map contains a month, expenses, and income.
     */
    @Override
    public List<Map<String, Object>> convertMonthlyData(List<Object[]> queryResult) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] o : queryResult) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("month", o[0]); // Month of the data
            temp.put("expenses", o[1]); // Expenses for the month
            temp.put("income", o[2]); // Income for the month
            result.add(temp);
        }
        return result;
    }

    /**
     * Converts query results into a list of maps representing expenses for this month by category.
     *
     * @param queryResult Raw query result from the repository (list of Object[]).
     * @return A list of maps where each map contains a category and the corresponding expenses.
     */
    @Override
    public List<Map<String, Object>> convertThisMonthExpenses(List<Object[]> queryResult) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] o : queryResult) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("category", o[0]); // Category of the expenses
            temp.put("expenses", o[1]); // Expenses for the category
            result.add(temp);
        }
        return result;
    }

    /**
     * Converts query results into a list of maps representing income for this month by category.
     *
     * @param queryResult Raw query result from the repository (list of Object[]).
     * @return A list of maps where each map contains a category and the corresponding income.
     */
    @Override
    public List<Map<String, Object>> convertThisMonthIncome(List<Object[]> queryResult) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] o : queryResult) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("category", o[0]); // Category of the income
            temp.put("income", o[1]); // Income for the category
            result.add(temp);
        }
        return result;
    }

    /**
     * Converts query results into a map representing total income and expenses for the current month.
     *
     * @param queryResult Raw query result from the repository (list of Object[]).
     * @return A map with total expenses and total income for the current month.
     */
    @Override
    public Map<String, Object> convertThisMonthTotalIncomeAndExpenses(List<Object[]> queryResult) {
        Object[] row = queryResult.get(0); // Get the first row of the result (since there is only one row for totals)
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total expenses", row[0]); // Total expenses for the month
        result.put("total income", row[1]); // Total income for the month
        return result;
    }

    /**
     * Retrieves the expenses for the current month for the given user.
     *
     * @param userName The email of the user whose current month's expenses are to be fetched.
     * @return A list of maps representing the expenses for each category.
     */
    @Override
    public List<Map<String, Object>> getThisMonthExpenses(String userName) {
        // Fetch the user by their email
        User user = userRepository.findByEmail(userName).orElseThrow();

        // Fetch the expenses for the current month for the user
        return convertThisMonthExpenses(transactionRepository.getThisMonthExpenses(user.getUserId()));
    }

    /**
     * Retrieves the income for the current month for the given user.
     *
     * @param userName The email of the user whose current month's income is to be fetched.
     * @return A list of maps representing the income for each category.
     */
    @Override
    public List<Map<String, Object>> getThisMonthIncome(String userName) {
        // Fetch the user by their email
        User user = userRepository.findByEmail(userName).orElseThrow();

        // Fetch the income for the current month for the user
        return convertThisMonthIncome(transactionRepository.getThisMonthIncome(user.getUserId()));
    }

    /**
     * Retrieves the total income and expenses for the current month for the given user.
     *
     * @param userName The email of the user whose total income and expenses for the current month are to be fetched.
     * @return A map with the total income and total expenses for the current month.
     */
    @Override
    public Map<String, Object> getThisMonthTotalIncomeAndExpenses(String userName) {
        // Fetch the user by their email
        User user = userRepository.findByEmail(userName).orElseThrow();

        // Fetch the total income and expenses for the current month for the user
        return convertThisMonthTotalIncomeAndExpenses(transactionRepository.getThisMonthTotalIncomeAndExpenses(user.getUserId()));
    }
}
