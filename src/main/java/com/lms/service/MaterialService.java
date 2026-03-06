package com.lms.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lms.dto.material.MaterialResponse;
import com.lms.dto.material.MaterialUploadRequest;
import com.lms.exception.BadRequestException;
import com.lms.model.Course;
import com.lms.model.CourseMaterial;
import com.lms.model.Role;
import com.lms.model.User;
import com.lms.repository.CourseMaterialRepository;
import com.lms.util.MapperUtil;

@Service
public class MaterialService {

    private final CourseMaterialRepository materialRepository;
    private final CourseService courseService;
    private final CurrentUserService currentUserService;

    public MaterialService(CourseMaterialRepository materialRepository,
            CourseService courseService,
            CurrentUserService currentUserService) {
        this.materialRepository = materialRepository;
        this.courseService = courseService;
        this.currentUserService = currentUserService;
    }

    public MaterialResponse uploadMaterial(MaterialUploadRequest request) {
        User user = currentUserService.getCurrentUser();
        if (user.getRole() == Role.STUDENT) {
            throw new BadRequestException("Students cannot upload materials");
        }
        Course course = courseService.getByCourseCode(request.courseCode());

        CourseMaterial material = new CourseMaterial();
        material.setCourseCode(course.getCourseCode());
        material.setFileName(request.fileName());
        material.setFileUrl(request.fileUrl());
        material.setUploadedBy(user.getId());
        material.setUploadDate(Instant.now());
        return MapperUtil.toMaterialResponse(materialRepository.save(material));
    }

    public List<MaterialResponse> getByCourseCode(String courseCode) {
        Course course = courseService.getByCourseCode(courseCode);
        return materialRepository.findByCourseCode(course.getCourseCode())
                .stream()
                .map(MapperUtil::toMaterialResponse)
                .toList();
    }
}
