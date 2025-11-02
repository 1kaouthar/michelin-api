package com.example.michelin_api.repository;


import com.example.michelin_api.entity.EvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EvaluationRepository extends JpaRepository<EvaluationEntity, Long> {


    @Query("""
        SELECT e FROM evaluation e
        WHERE (:kw IS NULL OR LOWER(e.comment) LIKE LOWER(CONCAT('%', :kw, '%'))
               OR LOWER(e.authorName) LIKE LOWER(CONCAT('%', :kw, '%')))
    """)
    List<EvaluationEntity> searchByKeyword(String kw);


    List<EvaluationEntity> findByAuthorTechnicalId(String authorTechnicalId);
}
