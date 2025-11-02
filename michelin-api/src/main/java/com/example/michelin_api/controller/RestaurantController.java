package com.example.michelin_api.controller;

import com.example.michelin_api.dto.restaurant.CreateRestaurantDto;
import com.example.michelin_api.dto.restaurant.RestaurantDto;
import com.example.michelin_api.dto.restaurant.UpdateRestaurantDto;
import com.example.michelin_api.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<RestaurantDto> getAll() {
        return restaurantService.findAll().stream()
                .map(r -> RestaurantDto.fromEntity(r, restaurantService.computeMoyenne(r)))
                .toList();
    }

    @GetMapping("/{id}")
    public RestaurantDto getOne(@PathVariable Long id) {
        var r = restaurantService.findById(id);
        return RestaurantDto.fromEntity(r, restaurantService.computeMoyenne(r));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public RestaurantDto create(@Valid @RequestBody CreateRestaurantDto dto) {
        var created = restaurantService.create(dto);
        return RestaurantDto.fromEntity(created, -1);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RestaurantDto update(@PathVariable Long id,
                                @Valid @RequestBody UpdateRestaurantDto dto) {
        var updated = restaurantService.update(id, dto);
        return RestaurantDto.fromEntity(updated, restaurantService.computeMoyenne(updated));
    }
}

