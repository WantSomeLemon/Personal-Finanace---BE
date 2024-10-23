package com.example.personalfinance.bean.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
@Data
public class ProfileImg {
    private MultipartFile image;
}
