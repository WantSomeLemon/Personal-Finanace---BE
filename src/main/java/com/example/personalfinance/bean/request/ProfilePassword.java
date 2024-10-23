package com.example.personalfinance.bean.request;

import lombok.Data;

@Data
public class ProfilePassword {
    private String Password;
    private String oldPassword;
}
