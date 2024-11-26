package com.example.personalfinance.controller;

import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Goals;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.GoalsService;
import lombok.RequiredArgsConstructor;
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

    /**
     * API Endpoint for creating a new goal.
     * @param token Authorization token (JWT)
     * @param goal The goal data to be created
     * @return Response with the created goal information
     */
    @PostMapping
    public ResponseEntity<BaseResponse> createGoal(@RequestHeader(value = "Authorization") String token,
                                                   @RequestBody Goals goal)
    {
        // Fetch the user based on the JWT token (email stored in the token)
        User user = userRepository.findByEmail(jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token)))
                .orElseThrow(); // Throw an exception if user not found

        // Set the user to the goal entity
        goal.setUser(user);

        // Create the goal using the service
        Goals createdGoal = goalsService.createGoal(goal);

        // Return a success response with the created goal
        return ResponseEntity.ok(new BaseResponse("success", createdGoal));
    }

    /**
     * API Endpoint for updating an existing goal.
     * @param id The ID of the goal to be updated
     * @param goal The goal data to be updated
     * @return Response with the updated goal information
     */
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateGoal(@PathVariable("id") Long id,
                                                   @RequestBody Goals goal)
    {
        // Update the goal using the service
        Goals updatedGoal = goalsService.updateGoal(id, goal);

        // Return a success response with the updated goal
        return ResponseEntity.ok(new BaseResponse("success", updatedGoal));
    }

    /**
     * API Endpoint for deleting an existing goal by ID.
     * @param id The ID of the goal to be deleted
     * @return Response indicating successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteGoal(@PathVariable("id") Long id)
    {
        Goals deletedGoal = goalsService.getGoal(id).orElseThrow(null);
        // Delete the goal using the service
        goalsService.deleteGoal(id);

        // Return a success response indicating the goal was deleted
        return ResponseEntity.ok(new BaseResponse("success", deletedGoal));
    }

    /**
     * API Endpoint for fetching a specific goal by ID.
     * @param id The ID of the goal to retrieve
     * @return Response with the requested goal or a "Not Found" status if not present
     */
    @GetMapping("/{id}")
    public ResponseEntity<Goals> getGoal(@PathVariable("id") Long id)
    {
        // Retrieve the goal by ID from the service
        Optional<Goals> goal = goalsService.getGoal(id);

        // Check if the goal is present and return the appropriate response
        if(goal.isPresent()){
            return ResponseEntity.ok(goal.get());
        } else {
            return ResponseEntity.notFound().build(); // Return "Not Found" if goal doesn't exist
        }
    }

    /**
     * API Endpoint for fetching all goals associated with a user.
     * @param token Authorization token (JWT)
     * @return Response with the list of all goals for the user
     */
    @GetMapping
    public ResponseEntity<BaseResponse> getAllGoals(@RequestHeader(value = "Authorization", defaultValue ="") String token)
    {
        // Fetch the user based on the JWT token (email stored in the token)
        User user = userRepository.findByEmail(jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token)))
                .orElseThrow();

        // Fetch all goals associated with the user
        List<Goals> goals = goalsService.getAllGoalsByUser(user);

        // Return a success response with the list of goals
        return ResponseEntity.ok(new BaseResponse("success", goals));
    }

    /**
     * A test endpoint for fetching a static list of goals.
     * @return Response with an empty list of goals (test case)
     */
    @GetMapping("/test")
    public ResponseEntity<List<Goals>> getAllTesGoals(){
        List<Goals> goals = new ArrayList<>();
        return ResponseEntity.ok(goals); // Return an empty list for testing
    }

    /**
     * A test endpoint for fetching goals by ID.
     * @param id The ID of the goals to retrieve
     * @return Response with an empty list of goals (test case)
     */
    @GetMapping("/test/{id}")
    public ResponseEntity<List<Goals>> getTestGoalsById(@PathVariable Integer id){
        List<Goals> goals = new ArrayList<>();
        return ResponseEntity.ok(goals); // Return an empty list for testing
    }
}
