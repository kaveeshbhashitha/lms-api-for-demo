package com.lms.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lms.dto.course.CourseCreateRequest;
import com.lms.dto.course.CourseResponse;
import com.lms.exception.BadRequestException;
import com.lms.exception.ResourceNotFoundException;
import com.lms.model.Course;
import com.lms.model.Role;
import com.lms.model.User;
import com.lms.repository.CourseRepository;
import com.lms.util.MapperUtil;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CurrentUserService currentUserService;

    public CourseService(CourseRepository courseRepository, CurrentUserService currentUserService) {
        this.courseRepository = courseRepository;
        this.currentUserService = currentUserService;
    }

    public CourseResponse createCourse(CourseCreateRequest request) {
        if (courseRepository.existsByCourseCode(request.courseCode())) {
            throw new BadRequestException("Course code already exists");
        }
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            throw new BadRequestException("Only admin can create courses");
        }

        Course course = new Course();
        course.setCourseCode(request.courseCode().toUpperCase());
        course.setCourseName(request.courseName());
        course.setYear(request.year());
        course.setCreatedAt(Instant.now());
        course.setCreatedBy(currentUser.getId());
        return MapperUtil.toCourseResponse(courseRepository.save(course));
    }

    public List<CourseResponse> getCoursesForCurrentUser() {
        User currentUser = currentUserService.getCurrentUser();
        List<Course> courses = currentUser.getRole() == Role.STUDENT
                ? courseRepository.findByYear(currentUser.getEnrolledYear())
                : courseRepository.findAll();
        return courses.stream().map(MapperUtil::toCourseResponse).toList();
    }

    public Course getByCourseCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseCode));
    }
}
