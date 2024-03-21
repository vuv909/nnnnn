package com.swp.server.dto;

import org.springframework.web.multipart.MultipartFile;

public class UpdateProfileDTO {
    private String email;
    private MultipartFile CV;
    private  MultipartFile avatar;
    public UpdateProfileDTO() {
    }
    public UpdateProfileDTO(String email, MultipartFile CV, MultipartFile avatar) {
        this.email = email;
        this.CV = CV;
        this.avatar = avatar;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public MultipartFile getCV() {
        return CV;
    }
    public void setCV(MultipartFile CV) {
        this.CV = CV;
    }
    public MultipartFile getAvatar() {
        return avatar;
    }
    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }
}
