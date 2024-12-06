package com.example.personalfinance.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.personalfinance.bean.request.TransactionRequest;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final JWTGenerator jwtGenerator;

    @GetMapping
    public ResponseEntity<BaseResponse> getTransactions(@RequestHeader(value = "Authorization") String token)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        List<Transaction> transactions = transactionService.getTransactionsByUserName(userName);
        return ResponseEntity.ok(new BaseResponse("success", transactions));
    }

    @PostMapping
    public ResponseEntity<BaseResponse> addTransactions(@RequestHeader(value = "Authorization") String token,
                                        @RequestBody TransactionRequest transactionRequest)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        Transaction transaction = transactionService.addTransaction(transactionRequest, userName);
        return ResponseEntity.ok(new BaseResponse("success", transaction));
    }
    
    @PutMapping
    public ResponseEntity<BaseResponse> updateTransactions(@RequestHeader(value = "Authorization") String token,
                                           @RequestBody TransactionRequest transactionRequest,
                                           @RequestParam String transactionId)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        Transaction transaction = transactionService.updateTransaction(transactionRequest, Integer.valueOf(transactionId), userName);
        return ResponseEntity.ok(new BaseResponse("success", transaction));
    }
    
    @DeleteMapping
    public ResponseEntity<BaseResponse> deleteTransactions(@RequestHeader(value = "Authorization") String token,
                                           @RequestParam String transactionId)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        if(transactionService.hasTransaction(transactionId)){
            if(transactionService.hasPermission(userName, transactionId)){
                transactionService.deleteTransaction(Integer.parseInt(transactionId));
                return ResponseEntity.ok(new BaseResponse("success"));
            }else{
                return ResponseEntity.ok(new BaseResponse("couldn't delete transaction"));
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse("transaction not found"));
        }
    }
}
