package com.example.personalfinance.bean.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileNameDto {
    private String firstName;
    private String lastName;
}
