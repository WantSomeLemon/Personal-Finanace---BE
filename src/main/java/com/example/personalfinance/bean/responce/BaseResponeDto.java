package com.example.personalfinance.bean.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponeDto {
    private Object message;
    private Object data;
}
