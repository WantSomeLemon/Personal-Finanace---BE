package com.example.personalfinance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.personalfinance.entity.Debt;
import com.example.personalfinance.entity.User;

/**
 * Repository interface for managing 'Debt' entities.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository // Marks this interface as a Spring Data repository
public interface DebtRepository extends JpaRepository<Debt, Integer> {

    /**
     * Finds all debts associated with a specific user.
     *
     * @param user The user whose debts are to be retrieved.
     * @return A list of debts belonging to the specified user.
     */
    List<Debt> findAllByUser(User user);

    /**
     * Finds all debts associated with a specific user and orders them by due date in ascending order.
     *
     * @param user The user whose debts are to be retrieved.
     * @return A list of debts belonging to the specified user, ordered by due date.
     */
    List<Debt> findAllByUserOrderByDueDateAsc(User user);

    /**
     * Finds all debts associated with a specific user and orders them by amount in descending order.
     *
     * @param user The user whose debts are to be retrieved.
     * @return A list of debts belonging to the specified user, ordered by amount.
     */
    List<Debt> findAllByUserOrderByAmountDesc(User user);
}
