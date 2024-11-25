package com.example.personalfinance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse getTransactions(@RequestHeader(value = "Authorization") String token)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        List<Transaction> transactions = transactionService.getTransactionsByUserName(userName);
        return new BaseResponse("success", transactions);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> addTransactions(@RequestHeader(value = "Authorization") String token,
                                          @RequestBody TransactionRequest transactionRequest)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        transactionService.addTransaction(transactionRequest, userName);
        return ResponseEntity.ok(new BaseResponse("Add success", transactionRequest));
    }
    
    @PutMapping("/{transactionId}")
    public ResponseEntity<BaseResponse> updateTransactions(@RequestHeader(value = "Authorization") String token,
                                           @RequestBody TransactionRequest transactionRequest,
                                           @PathVariable("transactionId") String transactionId)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        transactionService.updateTransaction(transactionRequest, Integer.valueOf(transactionId), userName);
        return ResponseEntity.ok(new BaseResponse("Update success", transactionRequest));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteTransactions(@RequestHeader(value = "Authorization") String token,
                                           @PathVariable("id") String transactionId)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        if(transactionService.hasTransaction(transactionId)){
            if(transactionService.hasPermission(userName, transactionId)){
                transactionService.deleteTransaction(Integer.parseInt(transactionId));
                return ResponseEntity.ok(new BaseResponse("Delete success"));
            }else{
                return ResponseEntity.badRequest().body(new BaseResponse("Permission denied"));
            }
        }else{
            return ResponseEntity.badRequest().body(new BaseResponse("Not exist transaction"));
        }
    }
}
