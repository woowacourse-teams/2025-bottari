package com.bottari.repository;

import com.bottari.domain.BottariTemplateHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BottariTemplateHistoryRepository extends JpaRepository<BottariTemplateHistory, Long> {

    boolean existsByBottariTemplateIdAndMemberId(
            final Long bottariTemplateId,
            final Long memberId
    );
}
