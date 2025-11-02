package com.example.michelin_api.dto.evaluation;


import com.example.michelin_api.entity.EvaluationEntity;

import java.util.List;

public record EvaluationDto(
        Long id,
        String authorName,
        String comment,
        Integer note,
        List<String> photoUrls,
        Long restaurantId
) {
    public static EvaluationDto fromEntity(EvaluationEntity e) {
        return new EvaluationDto(
                e.getId(),
                e.getAuthorName(),
                e.getComment(),
                e.getNote(),
                e.getPhotoUrls(),
                e.getRestaurant() != null ? e.getRestaurant().getId() : null
        );
    }
}

