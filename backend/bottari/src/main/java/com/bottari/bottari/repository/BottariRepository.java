package com.bottari.bottari.repository;

import com.bottari.bottari.domain.Bottari;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BottariRepository extends JpaRepository<Bottari, Long> {

    @Query("""
            SELECT b
            FROM Bottari b
            JOIN FETCH b.member
            WHERE b.id = :id
            """)
    Optional<Bottari> findByIdWithMember(Long id);

    List<Bottari> findAllByMemberIdOrderByCreatedAtDesc(final Long memberId);
}
