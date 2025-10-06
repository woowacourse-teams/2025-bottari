package com.bottari.bottaritemplate.repository;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.bottaritemplate.domain.BottariTemplateHashtag;
import com.bottari.bottaritemplate.repository.dto.HashtagPopularityProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BottariTemplateHashtagRepository extends JpaRepository<BottariTemplateHashtag, Long> {

    @Query("""
             SELECT bth
             FROM BottariTemplateHashtag bth
             JOIN FETCH bth.hashtag h
             WHERE bth.bottariTemplate.id = :bottariTemplateId
            """)
    List<BottariTemplateHashtag> findAllByBottariTemplateId(final Long bottariTemplateId);

    List<BottariTemplateHashtag> findAllByBottariTemplateIn(final List<BottariTemplate> bottariTemplateItems);

    @Query("""
            SELECT bth
            FROM BottariTemplateHashtag bth
            JOIN FETCH bth.hashtag h
            WHERE bth.bottariTemplate.id IN :templateIds
            """)
    List<BottariTemplateHashtag> findAllByBottariTemplateIds(final List<Long> templateIds);

    @Query(value = """
            SELECT
                h.id AS hashtagId,
                h.name AS hashtagName,
                COUNT(bth.id) AS usageCount
            FROM hashtag h
            INNER JOIN bottari_template_hashtag bth ON bth.hashtag = h.id
            WHERE bth.deleted_at IS NULL
            GROUP BY h.id, h.name
            ORDER BY usageCount DESC, h.id ASC
            LIMIT :limit
            """, nativeQuery = true)
    List<HashtagPopularityProjection> findTopNByUsageCount(final int limit);
}
