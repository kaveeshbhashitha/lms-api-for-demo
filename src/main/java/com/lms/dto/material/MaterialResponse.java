package com.lms.dto.material;

import java.time.Instant;

public record MaterialResponse(
        String id,
        String courseCode,
        String fileName,
        String fileUrl,
        String uploadedBy,
        Instant uploadDate) {
}
