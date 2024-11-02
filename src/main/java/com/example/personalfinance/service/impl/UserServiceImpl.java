package com.example.personalfinance.service.impl;

import com.example.personalfinance.bean.request.*;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.SecurityConfig;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.EmailService;
import com.example.personalfinance.service.UserService;
import com.example.personalfinance.util.OTPStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;
//    private final JavaMailSender mailSender;
    private final OTPStorage otpStorage;

    @Override
    public ResponseEntity<BaseResponse> register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new BaseResponse("Email Already Exists", null));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(new BaseResponse("User Registered Successfully", user));
    }

    @Override
    public void updateUserProfileImage(ProfileImgRequest profileImg, String userName) {
        try {
            User user = userRepository.findByEmail(userName).orElseThrow();
            user.setProfileImage(profileImg.getImage().getBytes());
            userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUserProfileName(ProfileNameRequest profileName, String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow();
        user.setFirstName(profileName.getFirstName());
        user.setLastName(profileName.getLastName());
        userRepository.save(user);
    }

    @Override
    public void updateUserProfileEmail(ProfileEmailRequest profileEmail, String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow();
        user.setEmail(profileEmail.getEmail());
        userRepository.save(user);
    }

    @Override
    public void sendVerificationEmail(String email) throws MailException, UnsupportedEncodingException {
        String fromAddress = "bach.nt.2150@aptechlearning.edu.vn";
        String senderName = "Personal Finance Team";
        String subject = "Verification Email";
        String content = "<div>\n" +
                "    <span style=\"color:#808080;padding: 2px;font-family: sans-serif;\">Paymint Account</span><br>\n" +
                "    <span style=\"color:#5C6AC4;padding: 2px;font-size:32px;font-family: sans-serif;\"><b>Security code</b></span><br><br>\n" +
                "    <span style=\"font-family: sans-serif;\">Please use the following security code for the Paymint account.</span><br><br><br>\n" +
                "    <span style=\"font-family: sans-serif;\">Security code: <b>[[CODE]]</b></span><br><br><br>\n" +
                "    <span style=\"font-family: sans-serif;\">Thanks,</span><br>\n" +
                "    <span style=\"font-family: sans-serif;\">The Paymint Team</span>\n" +
                "</div>";

//        MimeMessageHelper helper = new MimeMessageHelper()
//        not done
    }

    @Override
    public void newPassword(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
    
    @Override
    public ResponseEntity<BaseResponse> updatePassord(ProfilePasswordRequest profilePassword, String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow();
        if(new BCryptPasswordEncoder().matches(profilePassword.getOldPassword(), user.getPassword())) {
            if(new BCryptPasswordEncoder().matches(profilePassword.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body(new BaseResponse("New Password can't be same as Old Password!", null));
            }
            user.setPassword(passwordEncoder.encode(profilePassword.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok(new BaseResponse("Password Updated Successfully", null));
        }
        return ResponseEntity.badRequest().body(new BaseResponse("Old Password does not match", null));
    }

    @Override
    public ResponseEntity<BaseResponse> login(LoginRequest user) {
        User userEntity = userRepository.findByEmail(user.getEmail()).orElse(null);
        if(!userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new BaseResponse("Incorrect Email or Password...", null));
        }
        if(!new BCryptPasswordEncoder().matches(user.getPassword(), userEntity.getPassword())) {
            return ResponseEntity.badRequest().body(new BaseResponse("Incorrect Email or Password...", null));
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        Map<Object, Object> data = new HashMap<>();
        data.put("token", token);
        return ResponseEntity.ok(new BaseResponse("Login Success", data));
    }
}
