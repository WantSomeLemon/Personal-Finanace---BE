package com.example.personalfinance.controller;

import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Debt;
import com.example.personalfinance.service.DebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/debts")
public class DebtController {
    private final JWTGenerator jwtGenerator;
    private final DebtService debtService;

    @PostMapping
    public ResponseEntity<BaseResponse> createDebts(@RequestHeader(value = "Authorization") String token,
                                                    @RequestBody Debt debt)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        Debt debt1 = debtService.debtCreate(debt, userName);
        return ResponseEntity.ok(new BaseResponse("success", debt1));
    }

    @GetMapping("/user")
    public ResponseEntity<List<Debt>> getDebts(@RequestHeader(value = "Authorization") String token,
                                               @RequestParam("value") Integer value)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        List<Debt> Response = debtService.debGet(userName, value);
        HttpHeaders httpHead = new HttpHeaders();
        httpHead.add("info", "getting the list of Debt Values");
//        return ResponseEntity.status(HttpStatus.OK).headers(httpHead).body(Response);
        return ResponseEntity.ok().headers(httpHead).body(Response);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteDebt(@RequestHeader(value = "Authorization") String token,
                                             @RequestParam("debtId") Integer id)
    {
        String Resp = debtService.debtDelete(id);
        HttpHeaders httpHead = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.ACCEPTED).headers(httpHead).body(Resp);
    }

    @PutMapping
    public ResponseEntity<Debt> updateDebt(@RequestHeader(value = "Authorization") String token,
                                           @RequestBody Debt debt)
    {
       String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
       Integer id = debt.getDebtId();
       Debt debt1 = debtService.debtUpdate(debt, id);
       HttpHeaders httpHead = new HttpHeaders();
       httpHead.add("info","updated the Debt Value of" + id);
       return ResponseEntity.status(HttpStatus.ACCEPTED).headers(httpHead).body(debt1);
    }

    @GetMapping
    public ResponseEntity<List<Debt>> debtofAll()
    {
        List<Debt> Response = debtService.getAllDebts();
        HttpHeaders httpHead = new HttpHeaders();
        httpHead.add("info", "getting the list of Debt Values");
//        return ResponseEntity.status(HttpStatus.OK).headers(httpHead).body(Response);
        return ResponseEntity.ok().headers(httpHead).body(Response);
    }
}
