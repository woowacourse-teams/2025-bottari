package com.bottari.repository;

import com.bottari.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByBottariTemplateIdAndReporterId(
            final Long bottariTemplateId,
            final Long reportedId
    );
}
