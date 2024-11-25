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

@Service // Marks this class as a Spring service component for dependency injection.
@RequiredArgsConstructor // Generates constructor with required dependencies for the service.
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    @Lazy
    private TransactionService transactionService; // Lazy-loaded dependency for transaction operations

    /**
     * Setter for lazy-loading the transactionService to avoid circular dependency.
     *
     * @param transactionService The transaction service instance.
     */
    @Autowired
    public void setTransactionService(@Lazy TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Checks if an account exists in the repository by its account ID.
     *
     * @param accountId The ID of the account to check.
     * @return True if the account exists, otherwise false.
     */
    @Override
    public boolean hasAccount(Integer accountId) {
        return accountRepository.existsById(accountId); // Checks existence using existsById method from repository
    }

    /**
     * Checks if a user has permission to access or modify the specified account.
     *
     * @param username  The email of the user to check.
     * @param accountId The ID of the account to check.
     * @return True if the user has permission, otherwise false.
     */
    @Override
    public boolean hasPermission(String username, Integer accountId) {
        try {
            // Find the user by their email (username)
            User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
            // Find the account by its accountId
            Account entity = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
            // Check if the user is the owner of the account
            return Objects.equals(entity.getUser().getUserId(), user.getUserId());
        } catch (Exception e) {
            // Log the exception for debugging
            System.out.println("Error in hasPermission: " + e.getMessage());
            e.printStackTrace();
            throw e;  // Rethrow the exception for higher-level handling
        }
    }

    /**
     * Debits (subtracts) the specified amount from the account's balance.
     *
     * @param account The account to debit from.
     * @param amount  The amount to subtract from the balance.
     */
    @Override
    public void debitBalance(Account account, double amount) {
        account.setCurrentBalance(account.getCurrentBalance() - amount); // Subtract the amount from current balance
        accountRepository.save(account); // Save the updated account to the repository
    }

    /**
     * Credits (adds) the specified amount to the account's balance.
     *
     * @param account The account to credit to.
     * @param amount  The amount to add to the balance.
     */
    @Override
    public void creditBalance(Account account, double amount) {
        account.setCurrentBalance(account.getCurrentBalance() + amount); // Add the amount to current balance
        accountRepository.save(account); // Save the updated account to the repository
    }

    /**
     * Updates the details of an existing account based on the provided account ID.
     *
     * @param account   The account with updated details.
     * @param accountId The ID of the account to update.
     */
    @Override
    public void updateAccount(Account account, Integer accountId) {
        // Find the account by its accountId
        Account acc = accountRepository.findById(accountId).orElseThrow();
        // Update the account's details
        acc.setCurrentBalance(account.getCurrentBalance());
        acc.setName(account.getName());
        acc.setPaymentTypes(account.getPaymentTypes());
        accountRepository.save(acc); // Save the updated account to the repository
    }

    /**
     * Adds a new account and associates it with the specified user.
     *
     * @param account  The account to add.
     * @param userName The email of the user to associate the account with.
     */
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

    /**
     * Marks an account as deleted (soft delete by setting the 'deleted' flag to true).
     *
     * @param accountId The ID of the account to delete.
     */
    @Override
    public void deleteAccount(Integer accountId) {
        try {
            // Find the account by its accountId
            Account entity = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
            // Set the 'deleted' flag to true (soft delete)
            entity.setDeleted(true);
            accountRepository.save(entity); // Save the updated account to the repository
        } catch (Exception e) {
            // Log the exception details for debugging
            System.out.println("Error deleting account: " + e.getMessage());
            e.printStackTrace(); // Log stack trace for debugging
            throw e;  // Rethrow exception for higher-level handling
        }
    }

    /**
     * Retrieves a list of account responses for a specific user.
     * Calculates total expenses and income based on transactions.
     *
     * @param username The email of the user to get accounts for.
     * @return A list of AccountResponse containing account details.
     */
    @Override
    public List<AccountResponse> getAccountsByUsername(String username) {
        try {
            // Find the user by email
            User user = userRepository.findByEmail(username).orElseThrow();
            // Get all accounts associated with the user and not marked as deleted
            List<Account> accountList = accountRepository.findAllByUserAndIsDeletedFalse(user);
            List<AccountResponse> accountResponseList = new ArrayList<>();

            // Iterate over accounts and calculate total expenses and income
            for (Account account : accountList) {
                double totalExpenses = 0;
                double totalIncome = 0;
                List<Transaction> transactionList = transactionService.getTransactionsByAccount(account);

                // Calculate expenses and income from transactions
                for (Transaction transaction : transactionList) {
                    if (transaction.getCategory().getType().equals("expense")) {
                        totalExpenses += transaction.getAmount();
                    } else if (transaction.getCategory().getType().equals("income")) {
                        totalIncome += transaction.getAmount();
                    }
                }

                // Create AccountResponse with calculated totals
                AccountResponse accountResponse = new AccountResponse(
                        account.getAccountId(),
                        account.getName(),
                        account.getCurrentBalance(),
                        account.getPaymentTypes(),
                        totalExpenses,
                        totalIncome
                );
                accountResponseList.add(accountResponse);  // Add the response to the list
            }

            return accountResponseList;
        } catch (Exception e) {
            // Return null in case of any errors (consider logging or handling the exception more gracefully)
            return null;
        }
    }

    /**
     * Retrieves an account by its ID.
     *
     * @param id The ID of the account to retrieve.
     * @return The account if found.
     */
    @Override
    public Account getAccountById(Integer id) {
        return accountRepository.findById(id).orElseThrow(); // Throws exception if account is not found
    }

    /**
     * Retrieves all accounts from the repository, typically used for administrative purposes.
     *
     * @return A list of all accounts.
     */
    @Override
    public List<Account> getAllAccounts() {
        List<Account> accountList = accountRepository.findAll();
        return accountList; // Return the list of all accounts
    }
}
