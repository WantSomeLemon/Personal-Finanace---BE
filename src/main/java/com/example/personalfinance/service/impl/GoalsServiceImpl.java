package com.example.personalfinance.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
        return goalsRepository.save(goals);
    }
    @Override
    public Goals updateGoal(Long id, Goals updatedGoal) {
        Goals existingGoal = goalsRepository.findById(id).orElseThrow();
        existingGoal.setName(updatedGoal.getName());
        existingGoal.setDescription(updatedGoal.getDescription());
        existingGoal.setStatus(updatedGoal.getStatus());
        existingGoal.setTargetAmount(updatedGoal.getTargetAmount());
        existingGoal.setTargetDate(updatedGoal.getTargetDate());
        return goalsRepository.save(existingGoal);
    }

    @Override
    public void deleteGoal(Long id) {
       Goals existingGoal = goalsRepository.findById(id).orElseThrow(null);
       existingGoal.setDeleted(true);
       goalsRepository.save(existingGoal);
    }

    @Override
    public Optional<Goals> getGoal(Long id) {
        return goalsRepository.findById(id);
    }

    @Override
    public List<Goals> getAllGoalsByUser(User user) {
        List<Goals> goalsList = goalsRepository.findAllByUser(user);
        for (Goals goal : goalsList) {
            if (goal.getTargetDate() != null) {
                LocalDate date = Instant.ofEpochMilli(Long.parseLong(goal.getTargetDate()))
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate();

                // Định dạng ngày theo kiểu yyyy-MM-dd
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = date.format(formatter);
                goal.setTargetDate(formattedDate);
            }
        }
        return goalsList;
    }
}
