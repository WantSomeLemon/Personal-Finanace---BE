package com.example.personalfinance.controller;

import com.example.personalfinance.bean.response.AccountResponse;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Account;
import com.example.personalfinance.exception.account.AccountNotFoundException;
import com.example.personalfinance.exception.account.AccountUpdateException;
import com.example.personalfinance.exception.InvalidInputException;
import com.example.personalfinance.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {
    private final JWTGenerator jwtGenerator;
    private final AccountService accountService;

    /**
     * Creates a new account for the authenticated user.
     */
    @PostMapping
    public ResponseEntity<BaseResponse> createAccount(@RequestHeader(value = "Authorization") String token,
                                                      @RequestBody Account account) {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        accountService.addAccount(account, userName);
        return ResponseEntity.ok(new BaseResponse("success", accountService.getAccountById(account.getAccountId())));
    }

    @PutMapping
    public ResponseEntity<BaseResponse> updateAccount(@RequestHeader(value = "Authorization") String token,
                                                      @RequestBody Account account,
                                                      @RequestParam String accountId) {
        try {
            accountService.updateAccount(account, Integer.valueOf(accountId));
            return ResponseEntity.ok(new BaseResponse("success", accountService.getAccountById(Integer.valueOf(accountId))));
        } catch (AccountNotFoundException ex) {
            throw ex;
        } catch (InvalidInputException ex) {
            throw ex;
        } catch (AccountUpdateException ex) {
            throw ex;
        }
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAccount(@RequestHeader(value = "Authorization") String token) {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        List<AccountResponse> accounts = accountService.getAccountsByUsername(userName);
        return ResponseEntity.ok(new BaseResponse("success", accounts));
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse> deleteAccount(@RequestHeader(value = "Authorization") String token,
                                                      @RequestParam String accountId) {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        if (accountService.hasAccount(accountId) && accountService.hasPermission(userName, accountId)) {
            accountService.deleteAccount(accountId);
            return ResponseEntity.ok(new BaseResponse("success"));
        }
        throw new AccountNotFoundException("Account with ID " + accountId + " not found or unauthorized access");
    }
}
