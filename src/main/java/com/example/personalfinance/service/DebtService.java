package com.example.personalfinance.service;

import com.example.personalfinance.entity.Debt;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional  // This annotation ensures that all methods in this service are wrapped in a transaction.
                // If any operation within the transaction fails, all changes are rolled back to maintain data consistency.
public interface DebtService {

    /**
     * Creates a new debt entry for a user.
     *
     * @param deb the debt entity to be created
     * @param uName the username of the user who is associated with the debt
     * @return the created debt entity
     */
    Debt debtCreate(Debt deb, String uName);

    /**
     * Updates an existing debt entry.
     *
     * @param deb the updated debt entity
     * @param debtId the ID of the debt to be updated
     * @return the updated debt entity
     */
    Debt debtUpdate(Debt deb, Integer debtId);

    /**
     * Retrieves a debt entry by its ID.
     *
     * @param dId the ID of the debt
     * @return the debt entity with the given ID
     */
    Debt debGetId(Integer dId);

    /**
     * Deletes a debt entry by its ID.
     *
     * @param dId the ID of the debt to be deleted
     * @return a string message indicating the result of the deletion
     */
    String debtDelete(Integer dId);

    /**
     * Retrieves a list of debts for a specific user, possibly filtered by value.
     *
     * @param uName the username of the user whose debts are to be retrieved
     * @param value a value to filter the debts by (e.g., threshold for debt amount)
     * @return a list of debt entities that match the criteria
     */
    List<Debt> debGet(String uName, Integer value);

    /**
     * Retrieves all debts in the system.
     *
     * @return a list of all debt entities
     */
    List<Debt> getAllDebts();

    /**
     * Parses a due date string into a Date object.
     *
     * @param dueDate the string representation of the due date
     * @return the corresponding Date object
     */
    Date parseDueDate(String dueDate);
}
