package com.referralhub.dto;

import com.referralhub.model.User;

public record RegisterRequest(String name, String email, String password, User.UserRole role) {}
