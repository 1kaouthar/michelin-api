package com.example.michelin_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;


@Entity(name = "evaluation")
@Data
public class EvaluationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String authorName;

    @Column(length = 255)
    private String comment;

    @Column(nullable = false)
    private Integer note;

    @Column(length = 120, nullable = false)
    private String authorTechnicalId;

    @ElementCollection
    @CollectionTable(name = "evaluation_photos", joinColumns = @JoinColumn(name = "evaluation_id"))
    @Column(name = "photo_url", length = 255)
    private List<String> photoUrls;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;
}

