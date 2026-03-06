package com.lms.dto.user;

import com.lms.model.Role;

import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequest(@NotNull Role role) {
}
