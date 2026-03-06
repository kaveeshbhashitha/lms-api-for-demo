package com.lms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.dto.material.MaterialResponse;
import com.lms.dto.material.MaterialUploadRequest;
import com.lms.service.MaterialService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ResponseEntity<MaterialResponse> upload(@Valid @RequestBody MaterialUploadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(materialService.uploadMaterial(request));
    }

    @GetMapping("/{courseCode}")
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER','ADMIN')")
    public ResponseEntity<List<MaterialResponse>> getMaterials(@PathVariable String courseCode) {
        return ResponseEntity.ok(materialService.getByCourseCode(courseCode));
    }
}
