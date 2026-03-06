package com.lms.dto.user;

import java.time.Instant;

import com.lms.model.Role;

public record UserResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        Integer enrolledYear,
        Instant createdAt,
        Role role) {
}
