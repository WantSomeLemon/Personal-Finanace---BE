package com.example.personalfinance.bean.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private double amount;
    private String description;
    private String paymentType;
    private Integer categoryId;
    private Integer accountId;
    private Long dateTime;
}
