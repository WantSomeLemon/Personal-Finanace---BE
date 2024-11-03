package com.example.personalfinance.controller;

import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Goals;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.GoalsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goals")
public class GoalsController {
    private final GoalsService goalsService;
    private final JWTGenerator jwtGenerator;
    private final UserRepository userRepository;

    @PostMapping("/api/goals")
    public ResponseEntity<BaseResponse> createGoal(@RequestHeader(value = "Authorization", defaultValue = "") String token,
                                                   @RequestBody Goals goal)
    {
        User user = userRepository.findByEmail(jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token))).orElseThrow();
        goal.setUser(user);
        Goals createdGoal = goalsService.createGoal(goal);
        return ResponseEntity.ok(new BaseResponse("success"));
    }

    @PutMapping("/api/goals/{id}")
    public ResponseEntity<BaseResponse> updateGoal(@PathVariable("id") Long id,
                                                   @RequestBody Goals goal)
    {
        Goals updatedGoal = goalsService.updateGoal(id, goal);
        return ResponseEntity.ok(new BaseResponse("success", updatedGoal));
    }

    @DeleteMapping("/api/goals/{id}")
    public ResponseEntity<BaseResponse> deleteGoal(@PathVariable("id") Long id)
    {
        goalsService.deleteGoal(id);
        return ResponseEntity.ok(new BaseResponse("success"));
    }

    @GetMapping("/api/goals/{id}")
    public ResponseEntity<Goals> getGoal(@PathVariable("id") Long id)
    {
        Optional<Goals> goal = goalsService.getGoal(id);
        if(goal.isPresent()){
            return new ResponseEntity<>(goal.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/goals")
    public ResponseEntity<BaseResponse> getAllGoals(@RequestHeader(value = "Authorization", defaultValue ="") String token)
    {
        User user = userRepository.findByEmail(jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token))).orElseThrow();
        List<Goals> goals = goalsService.getAllGoalsByUser(user);
        return ResponseEntity.ok(new BaseResponse("success", goals));
    }
    @GetMapping("/goals")
    public ResponseEntity<List<Goals>> getAllTesGoals(){
        List<Goals> goals = new ArrayList<>();
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/goals/{id}")
    public ResponseEntity<List<Goals>> getTestGoalsById(@PathVariable Integer id){
        List<Goals> goals = new ArrayList<>();
        return ResponseEntity.ok(goals);
    }
}
