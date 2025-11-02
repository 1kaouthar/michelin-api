package com.example.michelin_api.dto.evaluation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;


public record CreateEvaluationDto(
        @NotBlank @Size(max = 50) String authorName,
        @Size(max = 255) String comment,
        @Min(0) @Max(3) Integer note,
        List<String> photoUrls
) {}
