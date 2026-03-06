package com.lms.dto.course;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CourseCreateRequest(
        @NotBlank String courseCode,
        @NotBlank String courseName,
        @Min(1) @Max(10000) Integer year) {
}
