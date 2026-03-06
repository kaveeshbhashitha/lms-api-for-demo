package com.lms.dto.material;

import jakarta.validation.constraints.NotBlank;

public record MaterialUploadRequest(
        @NotBlank String courseCode,
        @NotBlank String fileName,
        @NotBlank String fileUrl) {
}
