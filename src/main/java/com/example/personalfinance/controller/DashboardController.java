package com.example.personalfinance.controller;

import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    private final JWTGenerator jwtGenerator;

    /**
     * Endpoint to fetch monthly data (e.g., budget overview, financial stats).
     * @param token The JWT token from the request header.
     * @return ResponseEntity containing a BaseResponse with the monthly data.
     */
    @GetMapping("/monthly-data")
    public ResponseEntity<BaseResponse> getMonthlyData(@RequestHeader(value ="Authorization") String token) {
        // Extract the username from the JWT token.
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        // Fetch the monthly data for the user.
        List<Map<String, Object>> data = dashboardService.getMonthlyData(userName);
        return ResponseEntity.ok(new BaseResponse("success", data));
    }

    /**
     * Endpoint to fetch this month's expenses.
     * @param token The JWT token from the request header.
     * @return ResponseEntity containing a BaseResponse with the expenses data.
     */
    @GetMapping("/this-month/expenses")
    public ResponseEntity<BaseResponse> getThisMonthExpenses(@RequestHeader(value = "Authorization") String token) {
        // Extract the username from the JWT token.
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        // Fetch this month's expenses data for the user.
        List<Map<String, Object>> data = dashboardService.getThisMonthExpenses(userName);
        return ResponseEntity.ok(new BaseResponse("success", data));
    }

    /**
     * Endpoint to fetch this month's income.
     * @param token The JWT token from the request header.
     * @return ResponseEntity containing a BaseResponse with the income data.
     */
    @GetMapping("/this-month/income")
    public ResponseEntity<BaseResponse> getThisMonthIncome(@RequestHeader(value = "Authorization") String token) {
        // Extract the username from the JWT token.
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        // Fetch this month's income data for the user.
        List<Map<String, Object>> data = dashboardService.getThisMonthIncome(userName);
        return ResponseEntity.ok(new BaseResponse("success", data));
    }

    /**
     * Endpoint to fetch the total income and expenses for this month.
     * @param token The JWT token from the request header.
     * @return ResponseEntity containing a BaseResponse with the total income and expenses data.
     */
    @GetMapping("/this-month/total/income-and-expenses")
    public ResponseEntity<BaseResponse> getThisMonthTotalIncomeAndExpenses(@RequestHeader(value = "Authorization") String token) {
        // Extract the username from the JWT token.
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        // Fetch the total income and expenses for the user for this month.
        Map<String, Object> data = dashboardService.getThisMonthTotalIncomeAndExpenses(userName);
        return ResponseEntity.ok(new BaseResponse("success", data));
    }
}