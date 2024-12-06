package com.example.personalfinance.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.example.personalfinance.exception.goals.GoalCreationException;
import com.example.personalfinance.exception.goals.GoalDeletionException;
import com.example.personalfinance.exception.goals.GoalNotFoundException;
import com.example.personalfinance.exception.goals.GoalUpdateException;
import org.springframework.stereotype.Service;

import com.example.personalfinance.entity.Goals;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.GoalsRepository;
import com.example.personalfinance.service.GoalsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoalsServiceImpl implements GoalsService {
    private final GoalsRepository goalsRepository;

    @Override
    public Goals createGoal(Goals goals) {
        try {
            return goalsRepository.save(goals);
        } catch (Exception ex) {
            throw new GoalCreationException("Failed to create goal: " + ex.getMessage());
        }
    }
    @Override
    public Goals updateGoal(Long id, Goals updatedGoal) {
        try {
            Goals existingGoal = goalsRepository.findById(id).orElseThrow(() -> new GoalNotFoundException("Goal not found for id: " + id));
            existingGoal.setName(updatedGoal.getName());
            existingGoal.setDescription(updatedGoal.getDescription());
            existingGoal.setStatus(updatedGoal.getStatus());
            existingGoal.setTargetAmount(updatedGoal.getTargetAmount());
            existingGoal.setTargetDate(updatedGoal.getTargetDate());
            return goalsRepository.save(existingGoal);
        } catch (GoalNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new GoalUpdateException("Failed to update goal: " + ex.getMessage());
        }
    }

    @Override
    public void deleteGoal(Long id) {
        try {
            Goals existingGoal = goalsRepository.findById(id).orElseThrow(() -> new GoalNotFoundException("Goal not found for id: " + id));
            existingGoal.setDeleted(true);
            goalsRepository.save(existingGoal);
        } catch (GoalNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new GoalDeletionException("Failed to delete goal: " + ex.getMessage());
        }
    }

    @Override
    public Optional<Goals> getGoal(Long id) {
        return goalsRepository.findById(id);
    }

    @Override
    public List<Goals> getAllGoalsByUser(User user) {
        List<Goals> goalsList = goalsRepository.findAllByUserAndIsDeletedFalse(user);
        for (Goals goal : goalsList) {
            if (goal.getTargetDate() != null) {
                LocalDate date = Instant.ofEpochMilli(Long.parseLong(goal.getTargetDate()))
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate();

                // Format date to yyyy-MM-dd
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = date.format(formatter);
                goal.setTargetDate(formattedDate);
            }
        }
        return goalsList;
    }
}
