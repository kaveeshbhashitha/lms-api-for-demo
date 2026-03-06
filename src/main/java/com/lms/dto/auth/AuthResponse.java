package com.lms.dto.auth;

import com.lms.model.Role;

public record AuthResponse(String token, Role role, String userId) {
}
