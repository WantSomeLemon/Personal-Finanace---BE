package com.example.personalfinance.bean.response;

import lombok.Data;

@Data
public class BaseResponse {
    private Object message;
    private Object data;
}