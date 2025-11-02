package com.example.michelin_api.service;

import com.example.michelin_api.dto.evaluation.CreateEvaluationDto;
import com.example.michelin_api.entity.EvaluationEntity;
import com.example.michelin_api.entity.RestaurantEntity;
import com.example.michelin_api.repository.EvaluationRepository;
import com.example.michelin_api.repository.RestaurantRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final RestaurantRepository restaurantRepository;

    public EvaluationService(EvaluationRepository evaluationRepository,
                             RestaurantRepository restaurantRepository) {
        this.evaluationRepository = evaluationRepository;
        this.restaurantRepository = restaurantRepository;
    }


    public EvaluationEntity addEvaluation(Long restaurantId, CreateEvaluationDto dto, String currentUserTechnicalId) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NoSuchElementException("Restaurant not found: " + restaurantId));

        var eval = new EvaluationEntity();
        eval.setAuthorName(dto.authorName());
        eval.setComment(dto.comment());
        eval.setNote(dto.note());
        eval.setPhotoUrls(dto.photoUrls());
        eval.setAuthorTechnicalId(currentUserTechnicalId);
        eval.setRestaurant(restaurant);

        return evaluationRepository.save(eval);
    }


    public void deleteEvaluation(Long evalId, String currentUserTechnicalId, boolean isAdmin) {
        var eval = evaluationRepository.findById(evalId)
                .orElseThrow(() -> new NoSuchElementException("Evaluation not found: " + evalId));

        if (!isAdmin && !eval.getAuthorTechnicalId().equals(currentUserTechnicalId)) {
            throw new AccessDeniedException("You cannot delete this evaluation");
        }

        evaluationRepository.delete(eval);
    }


    public List<EvaluationEntity> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return evaluationRepository.findAll();
        }
        return evaluationRepository.searchByKeyword(keyword);
    }


    public List<EvaluationEntity> findMine(String currentUserTechnicalId) {
        return evaluationRepository.findByAuthorTechnicalId(currentUserTechnicalId);
    }
}
