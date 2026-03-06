package com.lms.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lms.model.CourseMaterial;

public interface CourseMaterialRepository extends MongoRepository<CourseMaterial, String> {
    List<CourseMaterial> findByCourseCode(String courseCode);
}
