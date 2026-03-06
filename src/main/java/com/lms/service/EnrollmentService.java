package com.lms.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lms.exception.BadRequestException;
import com.lms.model.Course;
import com.lms.model.Enrollment;
import com.lms.model.User;
import com.lms.repository.EnrollmentRepository;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseService courseService;
    private final CurrentUserService currentUserService;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
            CourseService courseService,
            CurrentUserService currentUserService) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseService = courseService;
        this.currentUserService = currentUserService;
    }

    public String enroll(String courseCode) {
        User user = currentUserService.getCurrentUser();
        Course course = courseService.getByCourseCode(courseCode);

        if (!course.getYear().equals(user.getEnrolledYear())) {
            throw new BadRequestException("User can only enroll to courses in enrolled year");
        }
        if (enrollmentRepository.existsByUserIdAndCourseCode(user.getId(), course.getCourseCode())) {
            throw new BadRequestException("Already enrolled to this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(user.getId());
        enrollment.setCourseCode(course.getCourseCode());
        enrollment.setEnrolledAt(Instant.now());
        enrollmentRepository.save(enrollment);
        return "Enrolled successfully";
    }

    public List<String> myEnrolledCourseCodes() {
        User user = currentUserService.getCurrentUser();
        return enrollmentRepository.findByUserId(user.getId())
                .stream()
                .map(Enrollment::getCourseCode)
                .toList();
    }
}
