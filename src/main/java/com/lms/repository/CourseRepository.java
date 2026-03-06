package com.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lms.model.Course;

public interface CourseRepository extends MongoRepository<Course, String> {
    Optional<Course> findByCourseCode(String courseCode);

    boolean existsByCourseCode(String courseCode);

    List<Course> findByYear(Integer year);
}
