package com.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lms.model.Enrollment;

public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {
    boolean existsByUserIdAndCourseCode(String userId, String courseCode);

    List<Enrollment> findByUserId(String userId);

    Optional<Enrollment> findByUserIdAndCourseCode(String userId, String courseCode);
}
