package com.example.personalfinance.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DashboardService {

    /**
     * Retrieves the monthly data for the user.
     *
     * @param userName the email or username of the user
     * @return a list of maps containing the user's monthly data
     */
    List<Map<String, Object>> getMonthlyData(String userName);

    /**
     * Converts the result of a query (as an array of objects) into a map representing monthly data.
     *
     * @param queryResult the raw query result as a list of object arrays
     * @return a list of maps representing the monthly data
     */
    List<Map<String, Object>> convertMonthlyData(List<Object[]> queryResult);

    /**
     * Converts the result of a query into a map representing this month's expenses.
     *
     * @param queryResult the raw query result as a list of object arrays
     * @return a list of maps representing this month's expenses
     */
    List<Map<String, Object>> convertThisMonthExpenses(List<Object[]> queryResult);

    /**
     * Converts the result of a query into a map representing this month's income.
     *
     * @param queryResult the raw query result as a list of object arrays
     * @return a list of maps representing this month's income
     */
    List<Map<String, Object>> convertThisMonthIncome(List<Object[]> queryResult);

    /**
     * Converts the result of a query into a map representing the total income and expenses for this month.
     *
     * @param queryResult the raw query result as a list of object arrays
     * @return a map representing the total income and expenses for this month
     */
    Map<String, Object> convertThisMonthTotalIncomeAndExpenses(List<Object[]> queryResult);

    /**
     * Retrieves the expenses for the current month for the specified user.
     *
     * @param userName the email or username of the user
     * @return a list of maps containing the user's expenses for this month
     */
    List<Map<String, Object>> getThisMonthExpenses(String userName);

    /**
     * Retrieves the income for the current month for the specified user.
     *
     * @param userName the email or username of the user
     * @return a list of maps containing the user's income for this month
     */
    List<Map<String, Object>> getThisMonthIncome(String userName);

    /**
     * Retrieves the total income and expenses for the current month for the specified user.
     *
     * @param userName the email or username of the user
     * @return a map representing the total income and expenses for this month
     */
    Map<String, Object> getThisMonthTotalIncomeAndExpenses(String userName);
}
