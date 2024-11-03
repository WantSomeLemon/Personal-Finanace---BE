package com.example.personalfinance.controller;

import com.example.personalfinance.bean.response.AccountResponse;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Account;
import com.example.personalfinance.service.AccountService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/api/accounts")
    public BaseResponse createAccount(@RequestHeader(value = "Authorization", defaultValue = "") String token,
                                      @RequestBody Account account)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        accountService.addAccount(account, userName);
        return new BaseResponse("success");
    }

    @PutMapping("api/accounts")
    public BaseResponse updateAccount(@RequestHeader(value = "Authorization", defaultValue = "") String token,
                                      @RequestBody Account account,
                                      @RequestParam String accountId)
    {
        accountService.updateAccount(account, Integer.valueOf(accountId));
        return new BaseResponse("success");
    }

    @GetMapping("api/accounts")
    public BaseResponse getAccount(@RequestHeader(value = "Authorization", defaultValue ="") String token)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        List<AccountResponse> accounts = accountService.getAccountsByUsername(userName);
        return new BaseResponse("success", accounts);
    }
    
    @DeleteMapping("api/accounts")
    public BaseResponse deleteAccount(@RequestHeader(value = "Authorization", defaultValue = "") String token,
                                      @RequestParam String accountId)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        if(accountService.hasAccount(accountId)){
            if(accountService.hasPermission(userName, accountId)){
                accountService.deleteAccount(accountId);
                return new BaseResponse("success");
            }else{
                return new BaseResponse("couldn't delete account");
            }
        }else{
            return new BaseResponse("account not found");
        }
    }
//Test case ?
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccount(){
        List<Account> accounts = new ArrayList<>();
        return ResponseEntity.ok(accounts);
    }
//Test case ?
    @GetMapping("/accounts/{id}")
    public ResponseEntity<List<Account>> getAccountById(@PathVariable Integer id){
        List<Account> accounts = new ArrayList<>();
        return ResponseEntity.ok(accounts);
    }
}
