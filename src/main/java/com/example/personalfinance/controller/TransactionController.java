package com.example.personalfinance.controller;

import com.example.personalfinance.bean.request.TransactionRequest;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final JWTGenerator jwtGenerator;

    @GetMapping
    public BaseResponse getTransactions(@RequestHeader(value = "Authorization", defaultValue = "") String token)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        List<Transaction> transactions = transactionService.getTransactionsByUserName(userName);
        return new BaseResponse("success", transactions);
    }

    @PostMapping
    public BaseResponse addTransactions(@RequestHeader(value = "Authorization", defaultValue = "") String token,
                                        @RequestBody TransactionRequest transactionRequest)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        transactionService.addTransaction(transactionRequest, userName);
        return new BaseResponse("success", null);
    }
    @PutMapping
    public BaseResponse updateTransactions(@RequestHeader(value = "Authorization", defaultValue = "") String token,
                                           @RequestBody TransactionRequest transactionRequest,
                                           @RequestParam String transactionId)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        transactionService.updateTransaction(transactionRequest, Integer.valueOf(transactionId), userName);
        return new BaseResponse("success", null);
    }
    @DeleteMapping
    public BaseResponse deleteTransactions(@RequestHeader(value = "Authorization", defaultValue = "") String token,
                                           @RequestParam String transactionId)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        if(transactionService.hasTransaction(transactionId)){
            if(transactionService.hasPermission(userName, transactionId)){
                transactionService.deleteTransaction(Integer.parseInt(transactionId));
                return new BaseResponse("success");
            }else{
                return new BaseResponse("couldn't delete transaction");
            }
        }else{
            return new BaseResponse("transaction not found");
        }
    }
}
