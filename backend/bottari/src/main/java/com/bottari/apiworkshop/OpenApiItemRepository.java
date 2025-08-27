package com.bottari.apiworkshop;

import com.bottari.bottaritemplate.domain.BottariTemplateItem;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OpenApiItemRepository extends JpaRepository<BottariTemplateItem, Long> {

    @Query("""
            SELECT i.name.name as name, COUNT(i) as includedCount
            FROM BottariTemplateItem i
            WHERE i.bottariTemplate.id IN :templateIds
            GROUP BY i.name.name
            HAVING
                (:lastCount IS NULL AND :lastName IS NULL)
                OR (COUNT(i) < :lastCount)
                OR (COUNT(i) = :lastCount AND i.name.name > :lastName)
            ORDER BY COUNT(i) DESC, i.name.name ASC
            """)
    Slice<ItemProjection> findAllWithIncludedCountByTemplateIdAndCursor(
            final List<Long> templateIds,
            final String lastName,
            final Long lastCount,
            final Pageable pageable
    );
}
