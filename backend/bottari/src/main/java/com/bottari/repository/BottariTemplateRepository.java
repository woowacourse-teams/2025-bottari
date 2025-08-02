package com.bottari.repository;

import com.bottari.domain.BottariTemplate;
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

    @Query("""
            SELECT bt
            FROM BottariTemplate bt
            JOIN FETCH bt.member m
            WHERE bt.id = :id
            """)
    Optional<BottariTemplate> findByIdWithMember(final Long id);

    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE BottariTemplate bt
            SET bt.takenCount = bt.takenCount + 1
            WHERE bt.id = :id
            """)
    void plusTakenCountById(final Long id);
}
