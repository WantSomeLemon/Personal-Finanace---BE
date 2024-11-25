package com.example.personalfinance.service;

import com.example.personalfinance.entity.Goals;
import com.example.personalfinance.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GoalsService {

    /**
     * Creates a new goal for the user.
     *
     * @param goals the goal entity to be created
     * @return the created goal entity
     */
    Goals createGoal(Goals goals);

    /**
     * Updates an existing goal for the user.
     *
     * @param id the ID of the goal to be updated
     * @param goals the updated goal entity
     * @return the updated goal entity
     */
    Goals updateGoal(Long id, Goals goals);

    /**
     * Deletes a goal by its ID.
     *
     * @param id the ID of the goal to be deleted
     */
    void deleteGoal(Long id);

    /**
     * Retrieves a goal by its ID.
     *
     * @param id the ID of the goal
     * @return an Optional containing the goal if found, otherwise empty
     */
    Optional<Goals> getGoal(Long id);

    /**
     * Retrieves all goals associated with a specific user.
     *
     * @param user the user whose goals are to be retrieved
     * @return a list of goals for the specified user
     */
    List<Goals> getAllGoalsByUser(User user);
}
