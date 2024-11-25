package com.example.personalfinance.service.impl;

import com.example.personalfinance.entity.Goals;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.GoalsRepository;
import com.example.personalfinance.service.GoalsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoalsServiceImpl implements GoalsService {
    private final GoalsRepository goalsRepository;

    /**
     * Creates a new goal and saves it to the database.
     *
     * @param goals The goal object to be saved.
     * @return The saved goal object.
     */
    @Override
    public Goals createGoal(Goals goals) {
        return goalsRepository.save(goals);
    }

    /**
     * Updates an existing goal with the provided updated data.
     *
     * @param id The ID of the goal to be updated.
     * @param updatedGoal The updated goal data.
     * @return The updated goal object.
     */
    @Override
    public Goals updateGoal(Long id, Goals updatedGoal) {
        // Fetch the existing goal by ID
        Goals existingGoal = goalsRepository.findById(id).orElseThrow();

        // Update the fields of the existing goal
        existingGoal.setName(updatedGoal.getName());
        existingGoal.setDescription(updatedGoal.getDescription());
        existingGoal.setStatus(updatedGoal.getStatus());
        existingGoal.setTargetAmount(updatedGoal.getTargetAmount());
        existingGoal.setTargetDate(updatedGoal.getTargetDate());

        // Save the updated goal back to the repository
        return goalsRepository.save(existingGoal);
    }

    /**
     * Deletes a goal based on the provided ID.
     *
     * @param id The ID of the goal to be deleted.
     * @throws IllegalArgumentException if the goal with the provided ID doesn't exist.
     */
    @Override
    public void deleteGoal(Long id) {
        // Check if the goal exists before deleting
        Optional<Goals> existingGoal = goalsRepository.findById(id);
        if(existingGoal.isPresent()) {
            goalsRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Goal with id " + id + " not exist.");
        }
    }

    /**
     * Retrieves a goal by its ID.
     *
     * @param id The ID of the goal to retrieve.
     * @return An Optional containing the goal, or empty if not found.
     */
    @Override
    public Optional<Goals> getGoal(Long id) {
        return goalsRepository.findById(id);
    }

    /**
     * Retrieves all goals associated with a specific user.
     *
     * @param user The user whose goals to retrieve.
     * @return A list of goals associated with the provided user.
     */
    @Override
    public List<Goals> getAllGoalsByUser(User user) {
        return goalsRepository.findAllByUser(user);
    }
}
