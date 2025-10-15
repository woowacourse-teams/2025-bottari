package com.bottari.bottaritemplate.repository;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.bottaritemplate.domain.BottariTemplateHashtag;
import com.bottari.bottaritemplate.repository.dto.BottariTemplateProjection;
import com.bottari.bottaritemplate.repository.dto.HashtagPopularityProjection;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    @Query("""
            SELECT
                   bt.id AS bottariTemplateId,
                   bt.title AS title,
                   bt.description AS description,
                   bt.takenCount AS takenCount,
                   bt.createdAt AS bottariTemplateCreatedAt,
                   m.id AS memberId,
                   m.name AS memberName
            FROM BottariTemplateHashtag bth
            JOIN bth.bottariTemplate bt
            JOIN bt.member m
            WHERE bth.hashtag.id = :hashtagId
                AND (
                        bt.createdAt < :lastCreatedAt
                    OR (bt.createdAt = :lastCreatedAt AND bt.id < :lastId)
                )
            ORDER BY bt.createdAt DESC, bt.id DESC
            """)
    Slice<BottariTemplateProjection> findNextByCreatedAt(
            final Long hashtagId,
            final LocalDateTime lastCreatedAt,
            final Long lastId,
            final Pageable pageable
    );

    @Query("""
            SELECT
                 bt.id AS bottariTemplateId,
                 bt.title AS title,
                 bt.description AS description,
                 bt.takenCount AS takenCount,
                 bt.createdAt AS bottariTemplateCreatedAt,
                 m.id AS memberId,
                 m.name AS memberName
            FROM BottariTemplateHashtag bth
            JOIN bth.bottariTemplate bt
            JOIN bt.member m
            WHERE bth.hashtag.id = :hashtagId
                AND (
                        bt.takenCount < :lastTakenCount
                    OR (bt.takenCount = :lastTakenCount AND bt.id < :lastId)
                )
            ORDER BY bt.takenCount DESC, bt.id DESC
            """
    )
    Slice<BottariTemplateProjection> findNextByTakenCount(
            final Long hashtagId,
            final Integer lastTakenCount,
            final Long lastId,
            final Pageable pageable
    );

    @Query(value = """
            SELECT
                h.id AS hashtagId,
                h.name AS hashtagName,
                COUNT(bth.id) AS usageCount
            FROM hashtag h
            INNER JOIN bottari_template_hashtag bth ON bth.hashtag_id = h.id
            WHERE bth.deleted_at IS NULL
            GROUP BY h.id, h.name
            ORDER BY usageCount DESC, h.id ASC
            LIMIT :limit
            """, nativeQuery = true)
    List<HashtagPopularityProjection> findTopNByUsageCount(final int limit);
}
