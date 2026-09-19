package com.example.incident_management.entity;

import lombok.Data;

@Data
public class LoginRequest {

    private String userName;
    private String password;
}