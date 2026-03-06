package com.lms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.service.EnrollmentService;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/{courseCode}")
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER')")
    public ResponseEntity<Map<String, String>> enroll(@PathVariable String courseCode) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", enrollmentService.enroll(courseCode)));
    }

    @GetMapping("/my-courses")
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER','ADMIN')")
    public ResponseEntity<List<String>> myCourses() {
        return ResponseEntity.ok(enrollmentService.myEnrolledCourseCodes());
    }
}
