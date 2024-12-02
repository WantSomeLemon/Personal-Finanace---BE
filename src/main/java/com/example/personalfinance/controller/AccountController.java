package com.example.personalfinance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.personalfinance.bean.response.AccountResponse;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Account;
import com.example.personalfinance.exception.AccountNotFoundException;
import com.example.personalfinance.exception.UnauthorizedActionException;
import com.example.personalfinance.service.AccountService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
@Validated
public class AccountController {

    private final JWTGenerator jwtGenerator;
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<BaseResponse> createAccount(
            @RequestHeader(value = "Authorization") String token,
            @Valid @RequestBody Account account) {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        accountService.addAccount(account, userName);
        return ResponseEntity.ok(new BaseResponse("success", accountService.getAccountById(account.getAccountId())));
    }

    @PutMapping
    public ResponseEntity<BaseResponse> updateAccount(
            @RequestHeader(value = "Authorization") String token,
            @Valid @RequestBody Account account,
            @RequestParam @Positive(message = "Account ID must be a positive integer.") int accountId) {
        accountService.updateAccount(account, accountId);
        return ResponseEntity.ok(new BaseResponse("success", accountService.getAccountById(accountId)));
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAccount(
            @RequestHeader(value = "Authorization", defaultValue = "") String token) {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        List<AccountResponse> accounts = accountService.getAccountsByUsername(userName);
        return ResponseEntity.ok(new BaseResponse("success", accounts));
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse> deleteAccount(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam @Positive(message = "Account ID must be a positive integer.") int accountId) {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));

        if (accountService.hasAccount(String.valueOf(accountId))) {
            if (accountService.hasPermission(userName, String.valueOf(accountId))) {
                accountService.deleteAccount(String.valueOf(accountId));
                return ResponseEntity.ok(new BaseResponse("success", "Account deleted successfully."));
            } else {
                throw new UnauthorizedActionException("You do not have permission to delete this account.");
            }
        } else {
            throw new AccountNotFoundException("Account not found with ID: " + accountId);
        }
    }
}
