package com.example.personalfinance.service;

import com.example.personalfinance.bean.response.AccountResponse;
import com.example.personalfinance.entity.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {

    /**
     * Checks if an account with the given ID exists in the system.
     *
     * @param accountId the ID of the account to check
     * @return true if the account exists, false otherwise
     */
    boolean hasAccount(Integer accountId);

    /**
     * Checks if the given user has permission to access the account with the specified ID.
     *
     * @param username the username of the user
     * @param accountId the ID of the account to check permission for
     * @return true if the user has permission, false otherwise
     */
    boolean hasPermission(String username, Integer accountId);

    /**
     * Debits a specified amount from the given account's balance.
     *
     * @param account the account to debit from
     * @param amount the amount to debit
     */
    void debitBalance(Account account, double amount);

    /**
     * Credits a specified amount to the given account's balance.
     *
     * @param account the account to credit
     * @param amount the amount to credit
     */
    void creditBalance(Account account, double amount);

    /**
     * Updates the details of the account with the given account ID.
     *
     * @param account the account object containing the updated details
     * @param accountId the ID of the account to update
     */
    void updateAccount(Account account, Integer accountId);

    /**
     * Adds a new account to the system for the specified user.
     *
     * @param account the account to add
     * @param userName the username of the user to associate with the account
     */
    void addAccount(Account account, String userName);

    /**
     * Deletes the account with the specified ID from the system.
     *
     * @param accountId the ID of the account to delete
     */
    void deleteAccount(Integer accountId);

    /**
     * Retrieves a list of accounts associated with the given username.
     *
     * @param username the username of the user whose accounts are to be retrieved
     * @return a list of AccountResponse objects representing the user's accounts
     */
    List<AccountResponse> getAccountsByUsername(String username);

    /**
     * Retrieves the account with the specified ID.
     *
     * @param id the ID of the account to retrieve
     * @return the Account object with the specified ID
     */
    Account getAccountById(Integer id);

    /**
     * Retrieves all accounts in the system.
     *
     * @return a list of all Account objects in the system
     */
    List<Account> getAllAccounts();
}
