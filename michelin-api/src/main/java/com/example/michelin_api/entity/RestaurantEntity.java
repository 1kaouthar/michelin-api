package com.example.michelin_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;


@Entity(name = "restaurant")
@Data
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 90, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(length = 255)
    private String imageUrl;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvaluationEntity> evaluations;
}

