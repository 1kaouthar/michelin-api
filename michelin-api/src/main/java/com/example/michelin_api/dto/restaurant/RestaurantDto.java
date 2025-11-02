package com.example.michelin_api.dto.restaurant;

import com.example.michelin_api.entity.RestaurantEntity;
import lombok.Builder;


@Builder
public record RestaurantDto(
        Long id,
        String name,
        String address,
        String imageUrl,
        double moyenne
) {
    public static RestaurantDto fromEntity(RestaurantEntity entity, double moyenne) {
        return RestaurantDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddress())
                .imageUrl(entity.getImageUrl())
                .moyenne(moyenne)
                .build();
    }
}

