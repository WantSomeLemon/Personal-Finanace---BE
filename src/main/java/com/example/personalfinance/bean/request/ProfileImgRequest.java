package com.example.personalfinance.bean.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * The 'ProfileImgRequest' class is used to capture the data required for uploading a user's profile image.
 * It contains a single field for the image file that the user wants to upload.
 */
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods automatically
public class ProfileImgRequest {

    /**
     * The image file that the user wants to set as their profile picture.
     * This field will be used when uploading the user's new profile image.
     */
    private MultipartFile image;
}
