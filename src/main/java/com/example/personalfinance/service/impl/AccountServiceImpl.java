package com.example.personalfinance.service.impl;

import com.example.personalfinance.bean.response.AccountResponse;
import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.exception.InvalidInputException;
import com.example.personalfinance.exception.UnauthorizedAccessException;
import com.example.personalfinance.exception.user.UserNotFoundException;
import com.example.personalfinance.exception.account.*;
import com.example.personalfinance.repository.AccountRepository;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.AccountService;
import com.example.personalfinance.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    @Lazy
    private TransactionService transactionService;
    
    @Autowired
    public void setTransactionService(@Lazy TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public boolean hasAccount(String accountId) {
        try {
            Account entity = accountRepository.findById(Integer.valueOf(accountId))
                    .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found"));
            return entity.getAccountId() == Integer.parseInt(accountId);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid account ID format: " + accountId);
        }
    }

    @Override
    public boolean hasPermission(String username, String accountId) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User with email " + username + " not found"));
        Account entity = accountRepository.findById(Integer.valueOf(accountId))
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found"));
        if (!Objects.equals(entity.getUser().getUserId(), user.getUserId())) {
            throw new UnauthorizedAccessException("User does not have permission for account ID " + accountId);
        }
        return true;
    }

    @Override
    public void debitBalance(Account account, double amount) {
        account.setCurrentBalance(account.getCurrentBalance() - amount);
        accountRepository.save(account);
    }

    @Override
    public void creditBalance(Account account, double amount) {
        account.setCurrentBalance(account.getCurrentBalance() + amount);
        accountRepository.save(account);
    }

    @Override
    public void updateAccount(Account account, Integer accountId) {
        try {
            Account acc = accountRepository.findById(accountId)
                    .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found"));

            // Validate the input fields (if necessary)
            if (account.getCurrentBalance() < 0) {
                throw new InvalidInputException("Current balance cannot be negative");
            }

            acc.setCurrentBalance(account.getCurrentBalance());
            acc.setName(account.getName());
            acc.setPaymentTypes(account.getPaymentTypes());

            accountRepository.save(acc);
        } catch (AccountNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AccountUpdateException("Failed to update account with ID " + accountId + ": " + ex.getMessage());
        }
    }


    @Override
    public void addAccount(Account account, String userName) {
        try{
            User user = userRepository.findByEmail(userName).orElseThrow();
            account.setUser(user);
            accountRepository.save(account);
        }catch(Exception ignored){

        }
    }

    @Override
    public void deleteAccount(String accountId) {
        Account entity = accountRepository.findById(Integer.valueOf(accountId))
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found"));
        entity.setDeleted(true);
        accountRepository.save(entity);
    }

    @Override
    public List<AccountResponse> getAccountsByUsername(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User with email " + username + " not found"));

        List<Account> accountList = accountRepository.findAllByUserAndIsDeletedFalse(user);
        if (accountList.isEmpty()) {
            throw new AccountNotFoundException("No accounts found for user " + username);
        }

        List<AccountResponse> accountResponseList = new ArrayList<>();
        for (Account account : accountList) {
            double totalExpenses = 0;
            double totalIncome = 0;
            List<Transaction> transactionList;
            try {
                transactionList = transactionService.getTransactionsByAccount(account);
            } catch (Exception e) {
                throw new TransactionProcessingException("Error fetching transactions for account ID " + account.getAccountId());
            }
            for (Transaction transaction : transactionList) {
                if ("expense".equals(transaction.getCategory().getType())) {
                    totalExpenses += transaction.getAmount();
                } else if ("income".equals(transaction.getCategory().getType())) {
                    totalIncome += transaction.getAmount();
                }
            }
            accountResponseList.add(new AccountResponse(
                    account.getAccountId(),
                    account.getName(),
                    account.getCurrentBalance(),
                    account.getPaymentTypes(),
                    totalExpenses,
                    totalIncome
            ));
        }
        return accountResponseList;
    }

    @Override
    public Account getAccountById(Integer id) {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accountList = accountRepository.findAll();
        return accountList;
    }
}
