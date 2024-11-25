package com.example.personalfinance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.entity.User;

@Repository // Marks the interface as a Spring Data repository, enabling it for dependency injection
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    /**
     * Finds all transactions for a specific user.
     *
     * @param user The user whose transactions are to be retrieved.
     * @return A list of transactions belonging to the specified user.
     */
    List<Transaction> findAllByUser(User user);

    /**
     * Finds all transactions associated with a specific account.
     *
     * @param account The account whose transactions are to be retrieved.
     * @return A list of transactions associated with the specified account.
     */
    List<Transaction> findAllByAccount(Account account);

    /**
     * Finds all transactions related to a specific category.
     *
     * @param id The category ID whose transactions are to be retrieved.
     * @return A list of transactions belonging to the specified category.
     */
    @Query(value = "select * from transactions where category_id =?1", nativeQuery = true)
    List<Transaction> findByCategory(Integer id);

    /**
     * Retrieves monthly income and expenses data for a specific user.
     * This query retrieves data for the last 5 months (starting from the current month).
     *
     * @param userId The user ID whose monthly data is to be retrieved.
     * @return A list of Object arrays containing the month, expenses, and income for the past 5 months.
     */
    @Query(value = """
        SELECT
            subquery.month,
            COALESCE(expenses, 0) AS expenses,
            COALESCE(income, 0) AS income
        FROM (
            SELECT
                MONTHNAME(DATE_SUB(DATE_FORMAT(NOW(), '%Y-%m-01'), INTERVAL n.num MONTH)) AS month,
                ROW_NUMBER() OVER (ORDER BY n.num DESC) AS rn
            FROM
                (SELECT 0 AS num UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5) AS n
        ) AS subquery
        LEFT JOIN (
            SELECT
                MONTHNAME(FROM_UNIXTIME(t.date_time/1000)) AS month,
                SUM(CASE WHEN c.category_type = 'expense' THEN amount ELSE 0 END) AS expenses,
                SUM(CASE WHEN c.category_type = 'income' THEN amount ELSE 0 END) AS income
            FROM transactions t
            JOIN categories c ON t.category_id = c.category_id
            WHERE
                t.user_id = ?1 AND
                FROM_UNIXTIME(t.date_time/1000) >= DATE_SUB(DATE_FORMAT(NOW(), '%Y-%m-01'), INTERVAL 5 MONTH)
            GROUP BY month
        ) AS data ON subquery.month = data.month
        ORDER BY subquery.rn DESC;
        """, nativeQuery = true)
    List<Object[]> getMonthlyData(Integer userId);

    /**
     * Retrieves this month's expenses, grouped by category, for a specific user.
     *
     * @param userId The user ID whose expenses are to be retrieved.
     * @return A list of Object arrays containing the category name and expenses for the current month.
     */
    @Query(value = """
        SELECT
            c.category_name AS category,
            COALESCE(SUM(t.amount), 0) AS expenses
        FROM transactions t
        JOIN categories c ON t.category_id = c.category_id
        WHERE t.user_id = ?1
        AND c.category_type = 'expense'
        AND MONTH(FROM_UNIXTIME(t.date_time/1000)) = MONTH(NOW())
        AND YEAR(FROM_UNIXTIME(t.date_time/1000)) = YEAR(NOW())
        GROUP BY c.category_name
        ORDER BY expenses DESC;
        """, nativeQuery = true)
    List<Object[]> getThisMonthExpenses(Integer userId);

    /**
     * Retrieves this month's income, grouped by category, for a specific user.
     *
     * @param userId The user ID whose income is to be retrieved.
     * @return A list of Object arrays containing the category name and income for the current month.
     */
    @Query(value = """
        SELECT
            c.category_name AS category,
            COALESCE(SUM(t.amount), 0) AS income
        FROM transactions t
        JOIN categories c ON t.category_id = c.category_id
        WHERE t.user_id = ?1
        AND c.category_type = 'income'
        AND MONTH(FROM_UNIXTIME(t.date_time/1000)) = MONTH(NOW())
        AND YEAR(FROM_UNIXTIME(t.date_time/1000)) = YEAR(NOW())
        GROUP BY c.category_name
        ORDER BY income DESC;
        """, nativeQuery = true)
    List<Object[]> getThisMonthIncome(Integer userId);

    /**
     * Retrieves the total income and expenses for the current month for a specific user.
     *
     * @param userId The user ID whose total income and expenses are to be retrieved.
     * @return A list of Object arrays containing the total expenses and income for the current month.
     */
    @Query(value = """
        SELECT
            COALESCE(SUM(CASE WHEN c.category_type = 'expense' THEN t.amount END), 0) AS total_expenses,
            COALESCE(SUM(CASE WHEN c.category_type = 'income' THEN t.amount END), 0) AS total_income
        FROM transactions t
        JOIN categories c ON t.category_id = c.category_id
        WHERE t.user_id = ?1
        AND MONTH(FROM_UNIXTIME(t.date_time/1000)) = MONTH(NOW())
        AND YEAR(FROM_UNIXTIME(t.date_time/1000)) = YEAR(NOW());
        """, nativeQuery = true)
    List<Object[]> getThisMonthTotalIncomeAndExpenses(Integer userId);
}
