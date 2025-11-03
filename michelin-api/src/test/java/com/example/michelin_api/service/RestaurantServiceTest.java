package com.example.michelin_api.service;

import com.example.michelin_api.dto.restaurant.CreateRestaurantDto;
import com.example.michelin_api.dto.restaurant.UpdateRestaurantDto;
import com.example.michelin_api.entity.EvaluationEntity;
import com.example.michelin_api.entity.RestaurantEntity;
import com.example.michelin_api.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RestaurantServiceTest {

    private RestaurantRepository restaurantRepository;
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        restaurantRepository = Mockito.mock(RestaurantRepository.class);
        restaurantService = new RestaurantService(restaurantRepository);
    }

    @Test
    void create_shouldSaveRestaurant() {
        var dto = new CreateRestaurantDto("Chez Luigi", "5 rue de Rome", "https://img.jpg");
        var entity = new RestaurantEntity();
        entity.setId(1L);
        entity.setName(dto.name());
        entity.setAddress(dto.address());
        entity.setImageUrl(dto.imageUrl());

        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(entity);

        var result = restaurantService.create(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Chez Luigi");
        assertThat(result.getAddress()).isEqualTo("5 rue de Rome");
    }

    @Test
    void findById_shouldReturnRestaurant() {
        var restaurant = new RestaurantEntity();
        restaurant.setId(10L);
        restaurant.setName("Le Gourmet");

        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));

        var found = restaurantService.findById(10L);

        assertThat(found.getName()).isEqualTo("Le Gourmet");
    }

    @Test
    void findById_shouldThrowIfNotFound() {
        when(restaurantRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantService.findById(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Restaurant not found");
    }

    @Test
    void update_shouldModifyNameAndAddress() {
        var existing = new RestaurantEntity();
        existing.setId(5L);
        existing.setName("Old Name");
        existing.setAddress("Old Address");

        var dto = new UpdateRestaurantDto("New Name", "New Address");

        when(restaurantRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenAnswer(i -> i.getArgument(0));

        var updated = restaurantService.update(5L, dto);

        assertThat(updated.getName()).isEqualTo("New Name");
        assertThat(updated.getAddress()).isEqualTo("New Address");
    }

    @Test
    void computeMoyenne_shouldReturnMinusOneWhenNoEvaluations() {
        var restaurant = new RestaurantEntity();
        restaurant.setEvaluations(List.of());
        double result = restaurantService.computeMoyenne(restaurant);
        assertThat(result).isEqualTo(-1);
    }

    @Test
    void computeMoyenne_shouldReturnAverage() {
        var eval1 = new EvaluationEntity();
        eval1.setNote(3);
        var eval2 = new EvaluationEntity();
        eval2.setNote(2);

        var restaurant = new RestaurantEntity();
        restaurant.setEvaluations(List.of(eval1, eval2));

        double result = restaurantService.computeMoyenne(restaurant);
        assertThat(result).isEqualTo(2.5);
    }

    @Test
    void computeMoyenne_shouldIgnoreNullNotes() {
        var eval1 = new EvaluationEntity();
        eval1.setNote(null);
        var eval2 = new EvaluationEntity();
        eval2.setNote(3);

        var restaurant = new RestaurantEntity();
        restaurant.setEvaluations(List.of(eval1, eval2));

        double result = restaurantService.computeMoyenne(restaurant);
        assertThat(result).isEqualTo(3.0);
    }
}
