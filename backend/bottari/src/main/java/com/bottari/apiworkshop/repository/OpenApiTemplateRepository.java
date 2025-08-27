package com.bottari.apiworkshop.repository;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OpenApiTemplateRepository extends JpaRepository<BottariTemplate, Long> {

    @Query("""
            SELECT t
            FROM BottariTemplate t
            WHERE (:query IS NULL OR t.title LIKE CONCAT('%', :query, '%') )
                AND (:start IS NULL OR t.createdAt >= :start)
                AND (:end IS NULL OR t.createdAt <= :end)
            """)
    List<BottariTemplate> findAllByTitleContainingAndCreatedAtBetween(
            final String query,
            final LocalDateTime start,
            final LocalDateTime end
    );
}
