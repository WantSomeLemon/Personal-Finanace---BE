package com.example.personalfinance.service.impl;

import com.example.personalfinance.bean.request.TransactionRequest;
import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.TransactionRepository;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.AccountService;
import com.example.personalfinance.service.CategoryService;
import com.example.personalfinance.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private AccountService accountService;

    @Autowired
    public void setAccountService(@Lazy AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Retrieves a list of all transactions made by a specific user.
     *
     * @param userName The email of the user whose transactions need to be fetched.
     * @return A sorted list of transactions for the user.
     */
    @Override
    public List<Transaction> getTransactionsByUserName(String userName) {
        try {
            User user = userRepository.findByEmail(userName).orElseThrow();
            List<Transaction> transactionList = transactionRepository.findAllByUser(user);
            transactionList.sort(Collections.reverseOrder());  // Sort transactions in reverse order (latest first)
            return transactionList;
        } catch (UsernameNotFoundException e) {
            return null;  // Return null if user is not found
        }
    }

    /**
     * Retrieves all transactions associated with a specific account.
     *
     * @param account The account whose transactions are to be fetched.
     * @return A list of transactions associated with the given account.
     */
    @Override
    public List<Transaction> getTransactionsByAccount(Account account) {
        try {
            return transactionRepository.findAllByAccount(account);
        } catch (UsernameNotFoundException e) {
            return null;  // Return null if account is not found
        }
    }

    /**
     * Adds a new transaction to the system and updates the related account's balance.
     *
     * @param transactionRequest The details of the transaction to be added.
     * @param userName The email of the user making the transaction.
     */
    @Override
    public void addTransaction(TransactionRequest transactionRequest, String userName) {
        Account account = accountService.getAccountById(transactionRequest.getAccountId());
        Category category = categoryService.getCategoryById(transactionRequest.getCategoryId());
        User user = userRepository.findByEmail(userName).orElseThrow();

        // Create the new transaction
        Transaction transaction = new Transaction(
                transactionRequest.getAmount(),
                transactionRequest.getDescription(),
                transactionRequest.getPaymentType(),
                transactionRequest.getDateTime(),
                category,
                account,
                user
        );

        // Save the transaction
        transactionRepository.save(transaction);

        // Adjust account balance based on the transaction type (expense or income)
        if (category.getType().equals("expense")) {
            accountService.debitBalance(account, transactionRequest.getAmount());  // Debit for expenses
        } else if (category.getType().equals("income")) {
            accountService.creditBalance(account, transactionRequest.getAmount());  // Credit for income
        }
    }

    /**
     * Updates an existing transaction based on the provided details.
     *
     * @param transactionRequest The updated transaction details.
     * @param transactionId The ID of the transaction to be updated.
     * @param userName The email of the user updating the transaction.
     */
    @Override
    public void updateTransaction(TransactionRequest transactionRequest, Integer transactionId, String userName) {
        try {
            Transaction entity = transactionRepository.findById(transactionId).orElseThrow();
            Account account = accountService.getAccountById(transactionRequest.getAccountId());
            Category category = categoryService.getCategoryById(transactionRequest.getCategoryId());

            // Update transaction details
            entity.setAccount(account);
            entity.setCategory(category);
            entity.setDateTime(transactionRequest.getDateTime());
            entity.setPaymentType(transactionRequest.getPaymentType());
            entity.setDescription(transactionRequest.getDescription());
            entity.setAmount(transactionRequest.getAmount());

            // Save updated transaction
            transactionRepository.save(entity);
        } catch (Exception ignore) {
            // Handle exceptions silently (can be improved for better error handling)
        }
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param id The ID of the transaction to be deleted.
     */
    @Override
    public void deleteTransaction(int id) {
        try {
            Transaction entity = transactionRepository.findById(id).orElseThrow();
            transactionRepository.delete(entity);
        } catch (Exception ignored) {
            // Handle exception silently (can be improved for better error handling)
        }
    }

    /**
     * Checks if a transaction with a specific ID exists.
     *
     * @param transactionId The ID of the transaction to check.
     * @return True if the transaction exists, otherwise false.
     */
    @Override
    public boolean hasTransaction(String transactionId) {
        try {
            Transaction entity = transactionRepository.findById(Integer.valueOf(transactionId)).orElseThrow();
            return entity.getId() == Integer.parseInt(transactionId);
        } catch (Exception ignored) {
            return false;  // Return false if the transaction doesn't exist
        }
    }

    /**
     * Checks if the user has permission to access a specific transaction.
     *
     * @param userName The email of the user.
     * @param transactionId The ID of the transaction to check for permission.
     * @return True if the user has permission to access the transaction, otherwise false.
     */
    @Override
    public boolean hasPermission(String userName, String transactionId) {
        try {
            User user = userRepository.findByEmail(userName).orElseThrow();
            Transaction entity = transactionRepository.findById(Integer.valueOf(transactionId)).orElseThrow();
            return Objects.equals(entity.getUser().getUserId(), user.getUserId());  // Check if the user is the owner of the transaction
        } catch (Exception ignored) {
            return false;  // Return false if permission check fails
        }
    }
}
