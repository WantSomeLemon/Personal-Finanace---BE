package com.example.personalfinance.controller;

import com.example.personalfinance.bean.response.AccountResponse;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Account;
import com.example.personalfinance.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {
    private final JWTGenerator jwtGenerator;
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<BaseResponse> createAccount(@RequestHeader(value = "Authorization") String token,
                                                      @RequestBody Account account) {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        Account addedAccount = accountService.addAccount(account, userName);
        return ResponseEntity.ok(new BaseResponse("Account created successfully", addedAccount));
    }

    @PutMapping
    public ResponseEntity<BaseResponse> updateAccount(@RequestHeader(value = "Authorization") String token,
                                                      @RequestBody Account account,
                                                      @RequestParam String accountId) {
        Account updatedAccount = accountService.updateAccount(account, Integer.valueOf(accountId));
        return ResponseEntity.ok(new BaseResponse("Account updated successfully", updatedAccount));
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAllAccount(@RequestHeader(value = "Authorization") String token) {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        List<AccountResponse> accounts = accountService.getAccountsByUsername(userName);
        return ResponseEntity.ok(new BaseResponse(accounts));
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse> deleteAccount(@RequestHeader(value = "Authorization") String token,
                                                      @RequestParam String accountId) {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        Account account = accountService.getAccountById(Integer.valueOf(accountId));
        if (accountService.hasAccount(accountId)) {
            if (accountService.hasPermission(userName, accountId)) {
                accountService.deleteAccount(accountId);
                return ResponseEntity.ok(new BaseResponse("Account deleted successfully", account));
            } else {
                return ResponseEntity.badRequest().body(new BaseResponse("You don't have permission to delete this account"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse("Not found this account"));
        }
    }
}
