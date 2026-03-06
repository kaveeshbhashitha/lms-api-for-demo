package com.lms.dto.course;

import java.time.Instant;

public record CourseResponse(
        String id,
        String courseCode,
        String courseName,
        Integer year,
        String createdBy,
        Instant createdAt) {
}
