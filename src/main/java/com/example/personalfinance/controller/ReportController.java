package com.example.personalfinance.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.service.TransactionService;
import com.example.personalfinance.util.TransactionExcelExporter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {
    private final JWTGenerator jwtGenerator;
    private final TransactionService transactionService;

    /**
     * API Endpoint to generate an Excel report of transactions for the authenticated user.
     * @param token Authorization token (JWT) to identify the user
     * @param httpServletResponse The response object to write the Excel file
     * @throws IOException If there is an error while writing the file
     */
    @GetMapping("/transaction/excel")
    public void transactionReportExcel(@RequestHeader(value = "Authorization") String token, HttpServletResponse httpServletResponse) throws IOException
    {
        // Extract the username (email) from the JWT token
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));

        // Set the response content type to indicate it's an Excel file download
        httpServletResponse.setContentType("application/octet-stream");

        // Format the current date and time to include it in the filename
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        // Set the content disposition header to prompt file download with a dynamic filename
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        httpServletResponse.setHeader(headerKey, headerValue);

        // Retrieve the list of transactions for the user
        List<Transaction> transactionList = transactionService.getTransactionsByUserName(userName);

        // Instantiate the Excel exporter and generate the Excel file
        TransactionExcelExporter transactionExcelExporter = new TransactionExcelExporter(transactionList);
        transactionExcelExporter.export(httpServletResponse);  // Write the Excel file to the response stream
    }
}
