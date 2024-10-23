package com.example.personalfinance.service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import com.example.personalfinance.bean.request.ProfileEmailDto;
import com.example.personalfinance.bean.request.ProfileImgDto;
import com.example.personalfinance.bean.request.ProfileNameDto;
import com.example.personalfinance.bean.request.ProfilePasswordDto;
import com.example.personalfinance.bean.responce.BaseResponeDto;
import com.example.personalfinance.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public interface UserService {
    ResponseEntity<BaseResponeDto> regisster(User user);
    void updateUserProfileImage(ProfileImgDto profileImgDto, String userName);
    void updateUserProfileName(ProfileNameDto profileNameDto, String userName);
    void updateUserProfileEmail(ProfileEmailDto profileEmailDto, String userName);
    void sendVerificationEmail(String email) throws MessagingException, UnsupportedEncodingException;
                            //MessagingException lib can't be found in ide
         ResponseEntity<BaseResponeDto> updatePassord(ProfilePasswordDto profilePasswordDto, String userName);
         ResponseEntity<BaseResponeDto> login(loginDto login);
                            //loginDto reference to auth part the project
    void newPassword(String email, String password);
}
