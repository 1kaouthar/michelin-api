package com.example.michelin_api.controller;

import com.example.michelin_api.service.MinioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/minio")
public class MinioController {

    private final MinioService minioService;

    public MinioController(MinioService minioService) {
        this.minioService = minioService;
    }

    @GetMapping("/upload-url")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String generateUploadUrl(@RequestParam String filename) {
        return minioService.getUploadUrl(filename);
    }

    @GetMapping("/download-url")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String generateDownloadUrl(@RequestParam String filename) {
        return minioService.getDownloadUrl(filename);
    }
}
