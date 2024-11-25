package com.example.personalfinance.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Debt;
import com.example.personalfinance.service.DebtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/debts")
public class DebtController {
    private final JWTGenerator jwtGenerator;
    private final DebtService debtService;

    /**
     * Create a new debt entry for the user.
     * @param token Authorization token (JWT)
     * @param debt The debt data to be created
     * @return Response with the created debt information
     */
    @PostMapping
    public ResponseEntity<BaseResponse> createDebts(@RequestHeader(value = "Authorization") String token,
                                                    @RequestBody Debt debt)
    {
        // Extract the username from the JWT token
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));

        // Create the debt entry using the debt service
        Debt debt1 = debtService.debtCreate(debt, userName);

        // Return a successful response with the created debt
        return ResponseEntity.ok(new BaseResponse("success", debt1));
    }

    /**
     * Get the list of debts for the user based on a filter value.
     * @param token Authorization token (JWT)
     * @param value Filter parameter for debt values
     * @return Response with a list of debts
     */
    @GetMapping("/user")
    public ResponseEntity<List<Debt>> getDebts(@RequestHeader(value = "Authorization") String token,
                                               @RequestParam("value") Integer value)
    {
        // Extract the username from the JWT token
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));

        // Fetch the debts based on the user's username and the filter value
        List<Debt> Response = debtService.debGet(userName, value);

        // Create custom HTTP headers for additional information
        HttpHeaders httpHead = new HttpHeaders();
        httpHead.add("info", "getting the list of Debt Values");

        // Return the list of debts with additional headers
        return ResponseEntity.ok().headers(httpHead).body(Response);
    }

    /**
     * Delete a specific debt entry by its ID.
     * @param token Authorization token (JWT)
     * @param id The ID of the debt to be deleted
     * @return Response with a success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDebt(@RequestHeader(value = "Authorization") String token,
                                             @PathVariable("id") Integer id)
    {
        // Delete the debt entry by its ID
        String Response = debtService.debtDelete(id);

        // Return the response indicating the debt has been deleted
        return ResponseEntity.ok().body(Response);
    }

    /**
     * Update an existing debt entry.
     * @param token Authorization token (JWT)
     * @param debt The debt data to be updated
     * @return Response with the updated debt information
     */
    @PutMapping
    public ResponseEntity<Debt> updateDebt(@RequestHeader(value = "Authorization") String token,
                                           @RequestBody Debt debt)
    {
        // Log the incoming debt for debugging purposes
        System.out.println("DebtController.updateDebt");
        System.out.println("debt = " + debt.toString());

        // Extract the username from the JWT token
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));

        // Fetch the debt ID from the request body and update the debt
        Integer id = debt.getDebtId();
        Debt debt1 = debtService.debtUpdate(debt, id);

        // Create custom HTTP headers with information about the updated debt
        HttpHeaders httpHead = new HttpHeaders();
        httpHead.add("info", "updated the Debt Value of " + id);

        // Return the updated debt along with custom headers
        return ResponseEntity.ok().headers(httpHead).body(debt1);
    }

    /**
     * Get a list of all debts.
     * @return Response with the list of all debts
     */
    @GetMapping
    public ResponseEntity<List<Debt>> debtofAll()
    {
        // Fetch all debts
        List<Debt> Response = debtService.getAllDebts();

        // Create custom HTTP headers with information about the request
        HttpHeaders httpHead = new HttpHeaders();
        httpHead.add("info", "getting the list of Debt Values");

        // Return the list of all debts with additional headers
        return ResponseEntity.ok().headers(httpHead).body(Response);
    }
}
