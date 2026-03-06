package com.lms.util;

import com.lms.dto.course.CourseResponse;
import com.lms.dto.material.MaterialResponse;
import com.lms.dto.user.UserResponse;
import com.lms.model.Course;
import com.lms.model.CourseMaterial;
import com.lms.model.User;

public final class MapperUtil {

    private MapperUtil() {
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getEnrolledYear(),
                user.getCreatedAt(),
                user.getRole());
    }

    public static CourseResponse toCourseResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getCourseCode(),
                course.getCourseName(),
                course.getYear(),
                course.getCreatedBy(),
                course.getCreatedAt());
    }

    public static MaterialResponse toMaterialResponse(CourseMaterial material) {
        return new MaterialResponse(
                material.getId(),
                material.getCourseCode(),
                material.getFileName(),
                material.getFileUrl(),
                material.getUploadedBy(),
                material.getUploadDate());
    }
}
