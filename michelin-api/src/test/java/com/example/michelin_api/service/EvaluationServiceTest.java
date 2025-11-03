package com.example.michelin_api.service;

import com.example.michelin_api.dto.evaluation.CreateEvaluationDto;
import com.example.michelin_api.entity.EvaluationEntity;
import com.example.michelin_api.entity.RestaurantEntity;
import com.example.michelin_api.repository.EvaluationRepository;
import com.example.michelin_api.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EvaluationServiceTest {

    private EvaluationRepository evaluationRepository;
    private RestaurantRepository restaurantRepository;
    private EvaluationService evaluationService;

    @BeforeEach
    void setUp() {
        evaluationRepository = Mockito.mock(EvaluationRepository.class);
        restaurantRepository = Mockito.mock(RestaurantRepository.class);
        evaluationService = new EvaluationService(evaluationRepository, restaurantRepository);
    }

    @Test
    void addEvaluation_shouldSaveEvaluation() {
        var dto = new CreateEvaluationDto("Lucien", "Très bon", 3, List.of("photo.jpg"));
        var restaurant = new RestaurantEntity();
        restaurant.setId(1L);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(evaluationRepository.save(any(EvaluationEntity.class))).thenAnswer(i -> i.getArgument(0));

        var saved = evaluationService.addEvaluation(1L, dto, "user-123");

        assertThat(saved.getAuthorName()).isEqualTo("Lucien");
        assertThat(saved.getComment()).isEqualTo("Très bon");
        assertThat(saved.getAuthorTechnicalId()).isEqualTo("user-123");
        assertThat(saved.getRestaurant()).isEqualTo(restaurant);
    }

    @Test
    void addEvaluation_shouldThrowIfRestaurantNotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        var dto = new CreateEvaluationDto("Lucien", "null", 2, List.of());

        assertThatThrownBy(() -> evaluationService.addEvaluation(99L, dto, "user-123"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Restaurant not found");
    }

    @Test
    void deleteEvaluation_shouldDeleteIfAuthor() {
        var eval = new EvaluationEntity();
        eval.setId(10L);
        eval.setAuthorTechnicalId("user-123");

        when(evaluationRepository.findById(10L)).thenReturn(Optional.of(eval));

        evaluationService.deleteEvaluation(10L, "user-123", false);

        verify(evaluationRepository).delete(eval);
    }

    @Test
    void deleteEvaluation_shouldThrowIfNotOwnerAndNotAdmin() {
        var eval = new EvaluationEntity();
        eval.setId(10L);
        eval.setAuthorTechnicalId("owner-xyz");

        when(evaluationRepository.findById(10L)).thenReturn(Optional.of(eval));

        assertThatThrownBy(() -> evaluationService.deleteEvaluation(10L, "other-user", false))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("You cannot delete this evaluation");

        verify(evaluationRepository, never()).delete(any());
    }

    @Test
    void deleteEvaluation_shouldAllowAdmin() {
        var eval = new EvaluationEntity();
        eval.setId(10L);
        eval.setAuthorTechnicalId("owner-xyz");

        when(evaluationRepository.findById(10L)).thenReturn(Optional.of(eval));

        evaluationService.deleteEvaluation(10L, "admin-user", true);

        verify(evaluationRepository).delete(eval);
    }

    @Test
    void search_shouldReturnAllIfKeywordBlank() {
        var list = List.of(new EvaluationEntity(), new EvaluationEntity());
        when(evaluationRepository.findAll()).thenReturn(list);

        var result = evaluationService.search("  ");
        assertThat(result).hasSize(2);
    }

    @Test
    void search_shouldUseCustomQueryWhenKeywordProvided() {
        var list = List.of(new EvaluationEntity());
        when(evaluationRepository.searchByKeyword("pizza")).thenReturn(list);

        var result = evaluationService.search("pizza");
        assertThat(result).hasSize(1);
    }

    @Test
    void findMine_shouldReturnUserEvaluations() {
        var eval1 = new EvaluationEntity();
        eval1.setAuthorTechnicalId("user-123");
        var eval2 = new EvaluationEntity();
        eval2.setAuthorTechnicalId("user-123");

        when(evaluationRepository.findByAuthorTechnicalId("user-123"))
                .thenReturn(List.of(eval1, eval2));

        var result = evaluationService.findMine("user-123");
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(e -> e.getAuthorTechnicalId().equals("user-123"));
    }
}
