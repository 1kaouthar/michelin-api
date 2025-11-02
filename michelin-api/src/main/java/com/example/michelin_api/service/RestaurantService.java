package com.example.michelin_api.service;

import com.example.michelin_api.dto.restaurant.CreateRestaurantDto;
import com.example.michelin_api.dto.restaurant.UpdateRestaurantDto;
import com.example.michelin_api.entity.RestaurantEntity;
import com.example.michelin_api.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }


    public List<RestaurantEntity> findAll() {
        return restaurantRepository.findAll();
    }


    public RestaurantEntity findById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Restaurant not found: " + id));
    }


    public RestaurantEntity create(CreateRestaurantDto dto) {
        var entity = new RestaurantEntity();
        entity.setName(dto.name());
        entity.setAddress(dto.address());
        entity.setImageUrl(dto.imageUrl());
        return restaurantRepository.save(entity);
    }


    public RestaurantEntity update(Long id, UpdateRestaurantDto dto) {
        var entity = findById(id);
        entity.setName(dto.name());
        entity.setAddress(dto.address());
        return restaurantRepository.save(entity);
    }


    public double computeMoyenne(RestaurantEntity restaurant) {
        if (restaurant.getEvaluations() == null || restaurant.getEvaluations().isEmpty()) {
            return -1;
        }
        return restaurant.getEvaluations().stream()
                .filter(e -> e.getNote() != null)
                .mapToInt(e -> e.getNote())
                .average()
                .orElse(-1);
    }
}

