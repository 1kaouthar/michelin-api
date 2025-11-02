package com.example.michelin_api.controller;

import com.example.michelin_api.service.MinioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/restaurants")
public class RestaurantImageController {

    private final MinioService minioService;

    public RestaurantImageController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PutMapping("/{id}/image")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> getUploadUrl(@PathVariable Long id) {
        String objectName = "restaurant-" + id + "-image.jpg";
        String url = minioService.getUploadUrl(objectName);
        return Map.of("uploadUrl", url);
    }

    @GetMapping("/{id}/image")
    public Map<String, String> getDownloadUrl(@PathVariable Long id) {
        String objectName = "restaurant-" + id + "-image.jpg";
        String url = minioService.getDownloadUrl(objectName);
        return Map.of("downloadUrl", url);
    }
}

