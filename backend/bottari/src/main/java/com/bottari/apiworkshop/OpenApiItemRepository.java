package com.bottari.apiworkshop;

import com.bottari.bottaritemplate.domain.BottariTemplateItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OpenApiItemRepository extends JpaRepository<BottariTemplateItem, Long> {

    @Query("""
            SELECT i.name as name, COUNT(i.name) as includedCount
            FROM BottariTemplateItem i
            WHERE i.bottariTemplate.id IN :templateIds
            GROUP BY i.name
            ORDER BY COUNT (i.name) DESC
            """)
    List<ItemProjection> findAllWithIncludedCountByTemplateIdIn(List<Long> templateIds);
}
