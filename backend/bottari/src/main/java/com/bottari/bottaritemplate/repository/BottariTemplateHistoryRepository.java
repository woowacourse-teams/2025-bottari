package com.bottari.bottaritemplate.repository;

import com.bottari.bottaritemplate.domain.BottariTemplateHistory;
import com.bottari.bottaritemplate.domain.BottariTemplateHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BottariTemplateHistoryRepository extends JpaRepository<BottariTemplateHistory, BottariTemplateHistoryId> {

    @Query("""
            SELECT COUNT(b) > 0
            FROM BottariTemplateHistory b
            WHERE b.id.bottariTemplateId = :bottariTemplateId
            AND b.id.memberId = :memberId
            """)
    boolean existsByBottariTemplateIdAndMemberId(
            final Long bottariTemplateId,
            final Long memberId
    );
}
