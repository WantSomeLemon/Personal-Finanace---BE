package com.example.personalfinance.service.impl;

import com.example.personalfinance.bean.response.AccountResponse;
import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.entity.User;
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
    private TransactionService transactionService; // Lazy-loaded dependency for transaction operations

    // Setter for lazy-loading transactionService (avoids circular dependency)
    @Autowired
    public void setTransactionService(@Lazy TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Check if an account exists in the repository by account ID
    @Override
    public boolean hasAccount(Integer accountId) {
        return accountRepository.existsById(accountId);  // Use existsById for checking existence
    }

    // Check if the user has permission to access or modify the specified account
    @Override
    public boolean hasPermission(String username, Integer accountId) {
        try {
            // Find the user by email (username)
            User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
            // Find the account by accountId
            Account entity = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
            // Check if the user is the owner of the account
            return Objects.equals(entity.getUser().getUserId(), user.getUserId());
        } catch (Exception e) {
            // Log the exception details for easier debugging
            System.out.println("Error in hasPermission: " + e.getMessage());
            e.printStackTrace();
            throw e;  // Rethrow the exception for higher-level handling
        }
    }

    // Debit (subtract) the specified amount from the account's balance
    @Override
    public void debitBalance(Account account, double amount) {
        account.setCurrentBalance(account.getCurrentBalance() - amount);
        accountRepository.save(account);  // Save the updated account
    }

    // Credit (add) the specified amount to the account's balance
    @Override
    public void creditBalance(Account account, double amount) {
        account.setCurrentBalance(account.getCurrentBalance() + amount);
        accountRepository.save(account);  // Save the updated account
    }

    // Update the details of an existing account
    @Override
    public void updateAccount(Account account, Integer accountId) {
        // Find the account by accountId
        Account acc = accountRepository.findById(accountId).orElseThrow();
        // Update the account fields
        acc.setCurrentBalance(account.getCurrentBalance());
        acc.setName(account.getName());
        acc.setPaymentTypes(account.getPaymentTypes());
        accountRepository.save(acc);  // Save the updated account
    }

    // Add a new account and associate it with the specified user
    @Override
    public void addAccount(Account account, String userName) {
        try {
            // Find the user by their email (userName)
            User user = userRepository.findByEmail(userName)
                    .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + userName));

            // Associate the user with the new account
            account.setUser(user);

            // Save the new account to the repository
            accountRepository.save(account);
        } catch (Exception e) {
            // Log the exception and rethrow a more meaningful exception
            System.err.println("Failed to add account: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error occurred while adding account", e);  // Rethrow exception for higher-level handling
        }
    }

    // Mark an account as deleted (soft delete by setting 'deleted' flag to true)
    @Override
    public void deleteAccount(Integer accountId) {
        try {
            // Find the account by accountId
            Account entity = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
            // Set the 'deleted' flag to true (soft delete)
            entity.setDeleted(true);
            accountRepository.save(entity);  // Save the updated account
        } catch (Exception e) {
            // Log the exception details
            System.out.println("Error deleting account: " + e.getMessage());  // Use a logger like SLF4J for production code
            e.printStackTrace();  // Log stack trace for debugging
            throw e;  // Rethrow exception for higher-level handling
        }
    }

    // Get a list of account responses for a specific user
    @Override
    public List<AccountResponse> getAccountsByUsername(String username) {
        try {
            // Find the user by email
            User user = userRepository.findByEmail(username).orElseThrow();
            // Get all accounts associated with the user and that are not deleted
            List<Account> accountList = accountRepository.findAllByUserAndIsDeletedFalse(user);
            List<AccountResponse> accountResponseList = new ArrayList<>();

            // Process each account to calculate total expenses and income
            for (Account account : accountList) {
                double totalExpenses = 0;
                double totalIncome = 0;
                List<Transaction> transactionList = transactionService.getTransactionsByAccount(account);

                // Iterate over transactions and calculate total expenses and income
                for (Transaction transaction : transactionList) {
                    if (transaction.getCategory().getType().equals("expense")) {
                        totalExpenses += transaction.getAmount();
                    } else if (transaction.getCategory().getType().equals("income")) {
                        totalIncome += transaction.getAmount();
                    }
                }

                // Create an AccountResponse with the necessary data
                AccountResponse accountResponse = new AccountResponse(
                        account.getAccountId(),
                        account.getName(),
                        account.getCurrentBalance(),
                        account.getPaymentTypes(),
                        totalExpenses,
                        totalIncome
                );
                accountResponseList.add(accountResponse);  // Add response to the list
            }

            return accountResponseList;
        } catch (Exception e) {
            // Return null in case of any errors (consider logging or handling the exception more gracefully)
            return null;
        }
    }

    // Get an account by its ID
    @Override
    public Account getAccountById(Integer id) {
        return accountRepository.findById(id).orElseThrow();  // Throw an exception if account is not found
    }

    // Get all accounts from the repository (used for administrative purposes)
    @Override
    public List<Account> getAllAccounts() {
        List<Account> accountList = accountRepository.findAll();
        return accountList;  // Return the list of all accounts
    }
}
