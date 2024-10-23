package com.example.personalfinance.service;

import org.springframework.mail.MailException;
import java.io.UnsupportedEncodingException;
import com.example.personalfinance.bean.request.ProfileEmail;
import com.example.personalfinance.bean.request.ProfileImg;
import com.example.personalfinance.bean.request.ProfileName;
import com.example.personalfinance.bean.request.ProfilePassword;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    ResponseEntity<BaseResponse> regisster(User user);

    void updateUserProfileImage(ProfileImg profileImg, String userName);

    void updateUserProfileName(ProfileName profileName, String userName);

    void updateUserProfileEmail(ProfileEmail profileEmail, String userName);

    void sendVerificationEmail(String email) throws MailException, UnsupportedEncodingException;

    ResponseEntity<BaseResponse> updatePassord(ProfilePassword profilePassword, String userName);

    ResponseEntity<BaseResponse> login(login login);

    // loginDto reference to auth part the project
    void newPassword(String email, String password);
}
