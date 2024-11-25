package com.example.personalfinance.service;

import com.example.personalfinance.bean.request.TransactionRequest;
import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface for handling transactions in the system.
 * Provides methods to retrieve, add, update, delete, and check transactions.
 */
@Service
public interface TransactionService {

    /**
     * Retrieves a list of transactions associated with a specific user.
     *
     * @param userName the username of the user whose transactions are to be retrieved
     * @return a list of transactions belonging to the specified user
     */
    List<Transaction> getTransactionsByUserName(String userName);

    /**
     * Retrieves a list of transactions associated with a specific account.
     *
     * @param account the account whose transactions are to be retrieved
     * @return a list of transactions related to the specified account
     */
    List<Transaction> getTransactionsByAccount(Account account);

    /**
     * Adds a new transaction to the system.
     *
     * @param transactionRequest the details of the transaction to be added
     * @param userName the username of the user adding the transaction
     */
    void addTransaction(TransactionRequest transactionRequest, String userName);

    /**
     * Updates an existing transaction in the system.
     *
     * @param transactionRequest the updated details of the transaction
     * @param transactionId the ID of the transaction to be updated
     * @param userName the username of the user updating the transaction
     */
    void updateTransaction(TransactionRequest transactionRequest, Integer transactionId, String userName);

    /**
     * Deletes a transaction from the system by its ID.
     *
     * @param id the ID of the transaction to be deleted
     */
    void deleteTransaction(int id);

    /**
     * Checks if a transaction with the given ID exists in the system.
     *
     * @param transactionId the ID of the transaction to check
     * @return true if the transaction exists, false otherwise
     */
    boolean hasTransaction(String transactionId);

    /**
     * Checks if the given user has permission to access the specified transaction.
     *
     * @param userName the username of the user
     * @param transactionId the ID of the transaction
     * @return true if the user has permission, false otherwise
     */
    boolean hasPermission(String userName, String transactionId);
}
