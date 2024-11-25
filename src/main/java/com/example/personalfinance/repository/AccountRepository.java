package com.example.personalfinance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.User;

/**
 * The repository interface for managing 'Account' entities.
 * It extends JpaRepository to provide CRUD operations and query methods.
 */
@Repository // Marks this interface as a Spring Data repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    /**
     * Finds all accounts associated with a specific user.
     *
     * @param user The user whose accounts are to be retrieved.
     * @return A list of accounts belonging to the given user.
     */
    List<Account> findAllByUser(User user);

    /**
     * Finds all non-deleted accounts associated with a specific user.
     *
     * @param user The user whose non-deleted accounts are to be retrieved.
     * @return A list of non-deleted accounts belonging to the given user.
     */
    List<Account> findAllByUserAndIsDeletedFalse(User user);
}
