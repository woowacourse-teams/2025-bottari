package com.bottari.repository;

import com.bottari.domain.BottariTemplate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BottariTemplateRepository extends JpaRepository<BottariTemplate, Long> {

    @Query("""
            SELECT bt
            FROM BottariTemplate bt
            JOIN FETCH Member m
            ON bt.member.id = m.id
            """)
    List<BottariTemplate> findAllWithMember();

    @Query("""
            SELECT bt
            FROM BottariTemplate bt
            JOIN FETCH Member m
            ON bt.member.id = m.id
            WHERE bt.id = :id
            """)
    Optional<BottariTemplate> findByIdWithMember(final Long id);
}
