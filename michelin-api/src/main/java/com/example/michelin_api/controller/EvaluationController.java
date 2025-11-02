package com.example.michelin_api.controller;

import com.example.michelin_api.dto.evaluation.CreateEvaluationDto;
import com.example.michelin_api.dto.evaluation.EvaluationDto;
import com.example.michelin_api.service.EvaluationService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping("/restaurants/{id}/evaluations")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public EvaluationDto addEvaluation(@PathVariable Long id,
                                       @Valid @RequestBody CreateEvaluationDto dto,
                                       Authentication authentication) {
        String userId = authentication.getName();
        var saved = evaluationService.addEvaluation(id, dto, userId);
        return EvaluationDto.fromEntity(saved);
    }

    @DeleteMapping("/evaluations/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void deleteEvaluation(@PathVariable Long id, Authentication authentication) {
        String userId = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        evaluationService.deleteEvaluation(id, userId, isAdmin);
    }

    @GetMapping("/evaluations")
    public List<EvaluationDto> search(@RequestParam(required = false) String keywords) {
        return evaluationService.search(keywords).stream()
                .map(EvaluationDto::fromEntity)
                .toList();
    }

    @GetMapping("/me/evaluations")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<EvaluationDto> myEvaluations(Authentication authentication) {
        String userId = authentication.getName();
        return evaluationService.findMine(userId).stream()
                .map(EvaluationDto::fromEntity)
                .toList();
    }
}
