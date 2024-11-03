package com.example.personalfinance.service;

import com.example.personalfinance.bean.request.*;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    ResponseEntity<BaseResponse> register(User user);

    void updateUserProfileImage(ProfileImgRequest profileImg, String userName);

    void updateUserProfileName(ProfileNameRequest profileName, String userName);

    void updateUserProfileEmail(ProfileEmailRequest profileEmail, String userName);

    void newPassword(String email, String password);
    
    ResponseEntity<BaseResponse> updatePassord(ProfilePasswordRequest profilePassword, String userName);

    ResponseEntity<BaseResponse> login(LoginRequest user);
    
}
