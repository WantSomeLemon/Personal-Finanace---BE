package com.example.personalfinance.controller;

import com.example.personalfinance.bean.request.TransactionRequest;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final JWTGenerator jwtGenerator;

    /**
     * API endpoint to get all transactions for the authenticated user.
     * @param token Authorization token (JWT) to identify the user
     * @return A BaseResponse containing the list of transactions
     */
    @GetMapping
    public BaseResponse getTransactions(@RequestHeader(value = "Authorization") String token)
    {
        // Extract the username (email) from the JWT token
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));

        // Retrieve the list of transactions for the user
        List<Transaction> transactions = transactionService.getTransactionsByUserName(userName);

        // Return a successful response with the transaction data
        return new BaseResponse("success", transactions);
    }

    /**
     * API endpoint to add a new transaction for the authenticated user.
     * @param token Authorization token (JWT) to identify the user
     * @param transactionRequest Data for the transaction to be added
     * @return A response entity with a success message and the transaction data
     */
    @PostMapping
    public ResponseEntity<BaseResponse> addTransactions(@RequestHeader(value = "Authorization") String token,
                                                        @RequestBody TransactionRequest transactionRequest)
    {
        // Extract the username (email) from the JWT token
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));

        // Add the new transaction using the service
        transactionService.addTransaction(transactionRequest, userName);

        // Return a successful response
        return ResponseEntity.ok(new BaseResponse("Add success", transactionRequest));
    }

    /**
     * API endpoint to update an existing transaction for the authenticated user.
     * @param token Authorization token (JWT) to identify the user
     * @param transactionRequest Updated data for the transaction
     * @param transactionId The ID of the transaction to be updated
     * @return A response entity with a success message and the updated transaction data
     */
    @PutMapping("/{transactionId}")
    public ResponseEntity<BaseResponse> updateTransactions(@RequestHeader(value = "Authorization") String token,
                                                           @RequestBody TransactionRequest transactionRequest,
                                                           @PathVariable("transactionId") String transactionId)
    {
        // Extract the username (email) from the JWT token
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));

        // Update the transaction using the service
        transactionService.updateTransaction(transactionRequest, Integer.valueOf(transactionId), userName);

        // Return a successful response with the updated transaction data
        return ResponseEntity.ok(new BaseResponse("Update success", transactionRequest));
    }

    /**
     * API endpoint to delete an existing transaction for the authenticated user.
     * @param token Authorization token (JWT) to identify the user
     * @param transactionId The ID of the transaction to be deleted
     * @return A response entity with the result of the delete operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteTransactions(@RequestHeader(value = "Authorization") String token,
                                                           @PathVariable("id") String transactionId)
    {
        // Extract the username (email) from the JWT token
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));

        // Check if the transaction exists and if the user has permission to delete it
        if(transactionService.hasTransaction(transactionId)){
            if(transactionService.hasPermission(userName, transactionId)){
                // If the user has permission, delete the transaction
                transactionService.deleteTransaction(Integer.parseInt(transactionId));
                return ResponseEntity.ok(new BaseResponse("Delete success"));
            }else{
                // If the user doesn't have permission, return a bad request response
                return ResponseEntity.badRequest().body(new BaseResponse("Permission denied"));
            }
        }else{
            // If the transaction doesn't exist, return a bad request response
            return ResponseEntity.badRequest().body(new BaseResponse("Not exist transaction"));
        }
    }
}
