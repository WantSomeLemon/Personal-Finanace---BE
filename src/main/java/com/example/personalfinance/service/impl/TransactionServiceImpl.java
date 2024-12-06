package com.example.personalfinance.service.impl;

import com.example.personalfinance.bean.request.TransactionRequest;
import com.example.personalfinance.entity.*;
import com.example.personalfinance.exception.account.AccountNotFoundException;
import com.example.personalfinance.exception.account.TransactionProcessingException;
import com.example.personalfinance.exception.categories.CategoryNotFoundException;
import com.example.personalfinance.exception.transaction.TransactionNotFoundException;
import com.example.personalfinance.exception.user.UserNotFoundException;
import com.example.personalfinance.repository.BudgetRepository;
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
    private final BudgetRepository budgetRepository;

    @Autowired
    public void setAccountService(@Lazy AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public List<Transaction> getTransactionsByUserName(String userName) {
        try {
            User user = userRepository.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            List<Transaction> transactionList = transactionRepository.findAllByUserAndIsDeletedFalse(user);
            if (transactionList.isEmpty()) {
                throw new TransactionNotFoundException("No transactions found for user " + userName);
            }
            transactionList.sort(Collections.reverseOrder());
            return transactionList;
        } catch (UsernameNotFoundException e) {
            throw new UserNotFoundException("User not found: " + userName);
        } catch (TransactionNotFoundException e) {
            throw e; // rethrow TransactionNotFoundException to be handled in the GlobalExceptionHandler
        }
    }

    @Override
    public List<Transaction> getTransactionsByAccount(Account account) {
        try {
            if (account == null) {
                throw new AccountNotFoundException("Account not found");
            }
            return transactionRepository.findAllByAccount(account);
        } catch (AccountNotFoundException e) {
            throw e; // rethrow AccountNotFoundException to be handled by GlobalExceptionHandler
        }
    }

    @Override
    public Transaction addTransaction(TransactionRequest transactionRequest, String userName) {
        try {
            Account account = accountService.getAccountById(transactionRequest.getAccountId());
            Category category = categoryService.getCategoryById(transactionRequest.getCategoryId());
            User user = userRepository.findByEmail(userName).orElseThrow(() -> new UserNotFoundException("User not found"));

            Transaction transaction = new Transaction(
                    transactionRequest.getAmount(),
                    transactionRequest.getDescription(),
                    transactionRequest.getPaymentType(),
                    transactionRequest.getDateTime(),
                    category,
                    account,
                    user);
            updateBudget(transaction);

            if (category.getType().equals("expense")) {
                accountService.debitBalance(account, transactionRequest.getAmount());
            } else if (category.getType().equals("income")) {
                accountService.creditBalance(account, transactionRequest.getAmount());
            }

            return transactionRepository.save(transaction);
        } catch (AccountNotFoundException e) {
            throw e; // rethrow AccountNotFoundException to be handled by GlobalExceptionHandler
        } catch (CategoryNotFoundException e) {
            throw e; // rethrow CategoryNotFoundException to be handled by GlobalExceptionHandler
        } catch (UserNotFoundException e) {
            throw e; // rethrow UserNotFoundException to be handled by GlobalExceptionHandler
        } catch (TransactionProcessingException e) {
            throw new TransactionProcessingException("Error processing transaction: " + e.getMessage());
        }
    }

    @Override
    public Transaction updateTransaction(TransactionRequest transactionRequest, Integer transactionId, String userName) {
        try {
            Transaction entity = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

            Account account = accountService.getAccountById(transactionRequest.getAccountId());
            Category category = categoryService.getCategoryById(transactionRequest.getCategoryId());

            entity.setAccount(account);
            entity.setCategory(category);
            entity.setDateTime(transactionRequest.getDateTime());
            entity.setPaymentType(transactionRequest.getPaymentType());
            entity.setDescription(transactionRequest.getDescription());
            entity.setAmount(transactionRequest.getAmount());

            return transactionRepository.save(entity);
        } catch (TransactionNotFoundException e) {
            throw e;
        } catch (AccountNotFoundException e) {
            throw e;
        } catch (CategoryNotFoundException e) {
            throw e;
        }
    }

    @Override
    public void deleteTransaction(int id) {
        try {
            Transaction entity = transactionRepository.findById(id)
                    .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

            entity.setDeleted(true);
            transactionRepository.save(entity);
        } catch (TransactionNotFoundException e) {
            throw e;
        }
    }

    @Override
    public boolean hasTransaction(String transactionId) {
        try {
            Transaction entity = transactionRepository.findById(Integer.valueOf(transactionId))
                    .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
            return entity.getId() == Integer.parseInt(transactionId);
        } catch (TransactionNotFoundException e) {
            throw e;
        }
    }

    @Override
    public boolean hasPermission(String userName, String transactionId) {
        try {
            User user = userRepository.findByEmail(userName).orElseThrow(() -> new UserNotFoundException("User not found"));
            Transaction entity = transactionRepository.findById(Integer.valueOf(transactionId))
                    .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
            return Objects.equals(entity.getUser().getUserId(), user.getUserId());
        } catch (UserNotFoundException | TransactionNotFoundException e) {
            throw e;
        }
    }

    @Override
    public void updateBudget(Transaction transaction) {
        try {
            Budget budget = budgetRepository.findByCategoryAndUser(transaction.getCategory(), transaction.getUser());
            if (budget != null) {
                long amount = (long) budget.getAmount();
                budget.setUsed(budget.getUsed() + transaction.getAmount());
                budget.setBalance(amount - transaction.getAmount());
                budgetRepository.save(budget);
            } else {
                throw new TransactionProcessingException("Budget not found for the transaction");
            }
        } catch (TransactionProcessingException e) {
            throw e;
        }
    }
}



