package com.example.personalfinance.bean.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponce {
    private int accountId;
    private String name;
    private double currentBalance;
    private String paymentTypes;
    private double totalExpense;
    private double totalIncome;

    public AccountResponce(int accountId, String name, double currentBalance, List<String> paymentTypes, double totalExpense, double totalIncome){
        this.accountId = accountId;
        this.name = name;
        this.currentBalance = currentBalance;
        this.paymentTypes = String.join(", ", paymentTypes);
        this.totalExpense = totalExpense;
        this.totalIncome = totalIncome;
    }
}
