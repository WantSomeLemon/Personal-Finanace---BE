package com.example.personalfinance.service.impl;

import com.example.personalfinance.entity.Debt;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.DebtRepository;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.DebtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class DebtServiceImpl implements DebtService {
    private final DebtRepository debtRepository;
    private final UserRepository userRepository;
    /**
     * Creates a new debt entry for the given user.
     *
     * @param deb The debt object containing the details to be saved.
     * @param uName The email of the user for whom the debt is created.
     * @return The created Debt entity.
     */
    @Override
    public Debt debtCreate(Debt deb, String uName) {
        try {
            // Retrieve user by email and associate with the debt
            User user = userRepository.findByEmail(uName).orElseThrow();
            deb.setUser(user);
        } catch (Exception ignored) {
            // In case user is not found, no action is performed (user-related exception ignored)
        }
        return debtRepository.save(deb); // Save the debt and return the saved entity
    }

    /**
     * Updates an existing debt entry.
     *
     * @param deb The debt object containing updated details.
     * @param debtId The ID of the debt to update.
     * @return The updated Debt entity.
     */
    @Override
    public Debt debtUpdate(Debt deb, Integer debtId) {
        // Retrieve the existing debt
        Debt debt = debtRepository.findById(debtId).orElseThrow(null);

        // Update debt details if they are not null or empty
        if (!"0".equalsIgnoreCase(String.valueOf(deb.getAmount()))) {
            debt.setAmount(deb.getAmount());
        }
        if (Objects.nonNull(deb.getMoneyFrom()) && !"".equalsIgnoreCase(deb.getMoneyFrom())) {
            debt.setMoneyFrom(deb.getMoneyFrom());
        }
        if (Objects.nonNull(deb.getStatus()) && !"".equalsIgnoreCase(deb.getStatus())) {
            debt.setStatus(deb.getStatus());
        }
        if (Objects.nonNull(deb.getDueDate()) && !"".equalsIgnoreCase(deb.getDueDate())) {
            debt.setDueDate(deb.getDueDate());
        }

        return debtRepository.save(debt); // Save and return the updated debt entity
    }

    /**
     * Retrieves a debt by its ID.
     *
     * @param dId The ID of the debt.
     * @return The Debt entity corresponding to the given ID.
     */
    @Override
    public Debt debGetId(Integer dId) {
        return debtRepository.findById(dId).get(); // Fetch the debt by ID
    }

    /**
     * Deletes a debt entry by its ID.
     *
     * @param dId The ID of the debt to delete.
     * @return A string message indicating that the debt was deleted.
     */
    @Override
    public String debtDelete(Integer dId) {
        debtRepository.deleteById(dId); // Delete the debt by ID
        return "Deleted"; // Return a message confirming the deletion
    }

    /**
     * Retrieves a list of debts for a user with optional sorting based on the provided value.
     *
     * @param uName The email of the user whose debts are to be fetched.
     * @param value A value used to determine the sorting method (1 for descending by amount, 2 for sorting by due date).
     * @return A list of Debt entities.
     */
    @Override
    public List<Debt> debGet(String uName, Integer value) {
        try {
            // Retrieve user by email
            User user = userRepository.findByEmail(uName).orElseThrow();

            // Sort debts based on the value
            if (value == 1) {
                return debtRepository.findAllByUserOrderByAmountDesc(user); // Sort by amount in descending order
            } else if (value == 2) {
                // Sort by due date
                List<Debt> debts = debtRepository.findAllByUserOrderByDueDateAsc(user); // Sort by date in ascending order
                //Change from ascending to descending 
//                Collections.reverse(debts);
                return debts;

            }

            return debtRepository.findAllByUser(user); // Default: return debts without sorting
        } catch (Exception e) {
            return null; // In case of an exception, return null (could be improved to handle specific exceptions)
        }
    }

    /**
     * Retrieves all debts from the system.
     *
     * @return A list of all debts.
     */
    @Override
    public List<Debt> getAllDebts() {
        return debtRepository.findAll(); // Return all debts from the repository
    }

    /**
     * Parses the due date string and converts it to a Date object.
     *
     * @param dueDate The due date as a string in "dd/MM/yyyy" format.
     * @return The parsed Date object, or null if parsing fails.
     */
    @Override
    public Date parseDueDate(String dueDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return formatter.parse(dueDate); // Parse and return the date
        } catch (ParseException e) {
            e.printStackTrace(); // Print the stack trace in case of a parse exception
            return null; // Return null if the parsing fails
        }
    }

}
