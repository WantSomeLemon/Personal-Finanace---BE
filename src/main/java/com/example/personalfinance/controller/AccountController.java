package com.example.personalfinance.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.personalfinance.bean.response.AccountResponse;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Account;
import com.example.personalfinance.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    // Dependencies injected via constructor
    private final JWTGenerator jwtGenerator;
    private final AccountService accountService;

    /**
     * Endpoint for creating a new account.
     *
     * @param token Authorization token
     * @param account Account object to be created
     * @return ResponseEntity containing success or error message
     */
    @PostMapping
    public ResponseEntity<BaseResponse> createAccount(@RequestHeader(value = "Authorization") String token,
                                                      @RequestBody Account account) {
        System.out.println("Received Account: " + account);  // Log the received account object
        try {
            // Extract username from the JWT token
            String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));

            // Add the account by associating it with the user
            accountService.addAccount(account, userName);

            // Return success response with the created account data
            return ResponseEntity.ok(new BaseResponse("success", account));
        } catch (IllegalArgumentException e) {
            // Handle case where the user is not found or invalid data is provided
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage()));
        } catch (Exception e) {
            // Handle any other unexpected errors
            return ResponseEntity.internalServerError().body(new BaseResponse("An error occurred while creating the account."));
        }
    }

    /**
     * Endpoint for updating an existing account.
     *
     * @param token Authorization token
     * @param account Account object with updated data
     * @return BaseResponse indicating success or failure
     */
    @PutMapping
    public BaseResponse updateAccount(@RequestHeader(value = "Authorization") String token,
                                      @RequestBody Account account) {
        // Update the account using the accountId passed in the request body
        accountService.updateAccount(account, account.getAccountId());
        // Return success response with the updated account data
        return new BaseResponse("update success", account);
    }

    /**
     * Endpoint for getting accounts related to the authenticated user.
     *
     * @param token Authorization token
     * @return BaseResponse with list of accounts related to the user
     */
    @GetMapping
    public BaseResponse getAccount(@RequestHeader(value = "Authorization", defaultValue ="") String token) {
        // Extract username from the token
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        // Retrieve the accounts associated with the user
        List<AccountResponse> accounts = accountService.getAccountsByUsername(userName);
        // Return success response with the list of accounts
        return new BaseResponse("success", accounts);
    }

    /**
     * Endpoint for deleting an account.
     *
     * @param token Authorization token
     * @param accountId Account ID to be deleted
     * @return BaseResponse indicating success or failure
     */
    @DeleteMapping("/{accountId}")
    public BaseResponse deleteAccount(@RequestHeader(value = "Authorization") String token,
                                      @PathVariable("accountId") Integer accountId) {
        // Extract username from the token
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        // Check if the account exists
        if(accountService.hasAccount(accountId)){
            // Check if the user has permission to delete the account
            if(accountService.hasPermission(userName, accountId)){
                // Delete the account
                accountService.deleteAccount(accountId);
                // Return success response along with the deleted account data
                return new BaseResponse("delete success", accountService.getAccountById(accountId));
            } else {
                // Return error response if the user doesn't have permission
                return new BaseResponse("couldn't delete account");
            }
        } else {
            // Return error response if the account doesn't exist
            return new BaseResponse("account not found");
        }
    }
}