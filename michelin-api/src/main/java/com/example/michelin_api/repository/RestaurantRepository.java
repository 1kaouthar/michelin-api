package com.example.michelin_api.repository;


import com.example.michelin_api.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
}

