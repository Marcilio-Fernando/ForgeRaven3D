package com.exemplo.forgeraven3d.repository;

import com.exemplo.forgeraven3d.model.Impressao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImpressaoRepository extends JpaRepository<Impressao, Long> {

    @Query("""
            SELECT i FROM Impressao i
            WHERE (:material = '' OR LOWER(i.material) = LOWER(:material))
              AND (:status = '' OR LOWER(i.status) = LOWER(:status))
              AND (:cor = '' OR LOWER(i.cor) LIKE LOWER(CONCAT('%', :cor, '%')))
              AND COALESCE(i.tempoMinutos, 0) <= :tempoMax
              AND COALESCE(i.pesoGramas, 0) >= :pesoMin
            """)
    List<Impressao> filtrar(@Param("material") String material,
                            @Param("status") String status,
                            @Param("cor") String cor,
                            @Param("tempoMax") Integer tempoMax,
                            @Param("pesoMin") Double pesoMin);
}