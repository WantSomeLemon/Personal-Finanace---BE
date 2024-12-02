package com.example.personalfinance.service.impl;

import com.example.personalfinance.bean.response.AccountResponse;
import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.exception.*;
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
            Account entity = accountRepository.getOne(Integer.valueOf(accountId));
            return entity.getAccountId() == Integer.parseInt(accountId);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid account ID format: " + accountId);
        }
    }

    @Override
    public boolean hasPermission(String username, String accountId) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        Account entity = accountRepository.findById(Integer.valueOf(accountId))
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        return Objects.equals(entity.getUser().getUserId(), user.getUserId());
    }

    @Override
    public void debitBalance(Account account, double amount) {
        if (amount < 0) {
            throw new InvalidInputException("Amount cannot be negative: " + amount);
        }
        if (account.getCurrentBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance for the transaction.");
        }
        account.setCurrentBalance(account.getCurrentBalance() - amount);
        accountRepository.save(account);
    }

    @Override
    public void creditBalance(Account account, double amount) {
        if (amount < 0) {
            throw new InvalidInputException("Amount cannot be negative: " + amount);
        }
        account.setCurrentBalance(account.getCurrentBalance() + amount);
        accountRepository.save(account);
    }

    @Override
    public void updateAccount(Account account, Integer accountId) {
        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        acc.setCurrentBalance(account.getCurrentBalance());
        acc.setName(account.getName());
        acc.setPaymentTypes(account.getPaymentTypes());
        accountRepository.save(acc);
    }

    @Override
    public void addAccount(Account account, String userName) {
        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + userName));
        // Check for duplicate account name
        List<Account> userAccounts = accountRepository.findAllByUser(user);
        boolean accountExists = userAccounts.stream()
                .anyMatch(existingAccount -> existingAccount.getName().equalsIgnoreCase(account.getName()));

        if (accountExists) {
            throw new DuplicateException("An account with the name '" + account.getName() + "' already exists for the user.");
        }

        account.setUser(user);
        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(String accountId) {
        Account entity = accountRepository.findById(Integer.valueOf(accountId))
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        entity.setDeleted(true);
        accountRepository.save(entity);
    }

    @Override
    public List<AccountResponse> getAccountsByUsername(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        List<Account> accountList = accountRepository.findAllByUserAndIsDeletedFalse(user);
        if (accountList.isEmpty()) {
            throw new EmptyListException("No accounts found for the user: " + username);
        }
        List<AccountResponse> accountResponseList = new ArrayList<>();
        for (Account account : accountList) {
            double totalExpenses = 0;
            double totalIncome = 0;
            List<Transaction> transactionList = transactionService.getTransactionsByAccount(account);
            for (Transaction transaction : transactionList) {
                if (transaction.getCategory().getType().equals("expense")) {
                    totalExpenses += transaction.getAmount();
                } else if (transaction.getCategory().getType().equals("income")) {
                    totalIncome += transaction.getAmount();
                }
            }
            AccountResponse accountResponse = new AccountResponse(
                    account.getAccountId(),
                    account.getName(),
                    account.getCurrentBalance(),
                    account.getPaymentTypes(),
                    totalExpenses,
                    totalIncome
            );
            accountResponseList.add(accountResponse);
        }
        return accountResponseList;
    }

    @Override
    public Account getAccountById(Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + id));
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accountList = accountRepository.findAll();
        if (accountList.isEmpty()) {
            throw new EmptyListException("No accounts found.");
        }
        return accountList;
    }
}
