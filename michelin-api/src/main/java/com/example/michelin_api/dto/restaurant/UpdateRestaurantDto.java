package com.example.michelin_api.dto.restaurant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UpdateRestaurantDto(
        @NotBlank @Size(max = 90) String name,
        @NotBlank @Size(max = 255) String address
) {}
