package com.referralhub.dto;

import com.referralhub.model.User;

public record UserResponse(Long id, String name, String email, User.UserRole role) {}
