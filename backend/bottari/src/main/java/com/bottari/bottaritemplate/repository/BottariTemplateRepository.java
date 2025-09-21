package com.bottari.bottaritemplate.repository;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.bottaritemplate.repository.dto.BottariTemplateProjection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BottariTemplateRepository extends JpaRepository<BottariTemplate, Long> {

    @Query("""
            SELECT bt
            FROM BottariTemplate bt
            JOIN FETCH bt.member m
            WHERE m.id = :memberId
            ORDER BY bt.createdAt DESC
            """)
    List<BottariTemplate> findAllByMemberIdWithMember(final Long memberId);

    @Query("""
            SELECT bt
            FROM BottariTemplate bt
            JOIN FETCH bt.member m
            WHERE bt.title LIKE CONCAT('%', :query, '%')
            ORDER BY bt.createdAt DESC
            """)
    List<BottariTemplate> findAllWithMember(final String query);

    @Query(value = """
            SELECT STRAIGHT_JOIN
                       bt.id AS bottariTemplateId,
                       bt.title AS title,
                       bt.taken_count AS takenCount,
                       bt.created_at AS bottariTemplateCreatedAt,
                       m.id AS memberId,
                       m.name AS memberName
            FROM bottari_template bt
            JOIN member m ON m.id = bt.member_id 
            WHERE (:query = '' OR MATCH(bt.title) AGAINST(:query IN BOOLEAN MODE))
                AND(
                        bt.created_at < :lastCreatedAt
                            OR (bt.created_at = :lastCreatedAt AND bt.id < :lastId)
                        )
            ORDER BY bt.created_at DESC, bt.id DESC 
            LIMIT :limit
        """ ,nativeQuery = true)
    List<BottariTemplateProjection> findNextByCreatedAt(
            final String query,
            final LocalDateTime lastCreatedAt,
            final Long lastId,
            final int limit
    );

    @Query(value = """
            SELECT STRAIGHT_JOIN
                  bt.id AS bottariTemplateId,
                  bt.title AS title,
                  bt.taken_count AS takenCount,
                  bt.created_at AS bottariTemplateCreatedAt,
                  m.id AS memberId,
                  m.name AS memberName
            FROM bottari_template bt
            JOIN member m ON m.id = bt.member_id
            WHERE (:query = '' OR MATCH(bt.title) AGAINST(:query IN BOOLEAN MODE))
                AND (
                    bt.taken_count < :lastTakenCount
                        OR (bt.taken_count = :lastTakenCount AND bt.id < :lastId)
                    )
            ORDER BY bt.taken_count DESC, bt.id DESC
            LIMIT :limit
            """ ,nativeQuery = true)
    List<BottariTemplateProjection> findNextByTakenCount(
            final String query,
            final Long lastTakenCount,
            final Long lastId,
            final int limit
    );

    @Query("""
            SELECT bt
            FROM BottariTemplate bt
            JOIN FETCH bt.member m
            WHERE bt.id = :id
            """)
    Optional<BottariTemplate> findByIdWithMember(final Long id);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            UPDATE BottariTemplate bt
            SET bt.takenCount = bt.takenCount + 1
            WHERE bt.id = :id
            """)
    void plusTakenCountById(final Long id);
}
