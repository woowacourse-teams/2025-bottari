package com.bottari.service;

import com.bottari.domain.BottariTemplate;
import com.bottari.domain.Member;
import com.bottari.domain.Report;
import com.bottari.dto.ReportBottariTemplateRequest;
import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.repository.BottariTemplateRepository;
import com.bottari.repository.MemberRepository;
import com.bottari.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final BottariTemplateRepository bottariTemplateRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void reportBottariTemplate(
            final String reporterSsaid,
            final Long bottariTemplateId,
            final ReportBottariTemplateRequest request
    ) {
        final BottariTemplate bottariTemplate = bottariTemplateRepository.findById(bottariTemplateId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOTTARI_TEMPLATE_NOT_FOUND));
        final Member reporter = memberRepository.findBySsaid(reporterSsaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        checkAlreadyReportedBottariTemplate(bottariTemplate, reporter);
        final Report report = new Report(bottariTemplate, reporter, request.reason());
        reportRepository.save(report);
    }

    private void checkAlreadyReportedBottariTemplate(
            final BottariTemplate bottariTemplate,
            final Member reporter
    ) {
        if (reportRepository.existsByBottariTemplateIdAndReporterId(bottariTemplate.getId(), reporter.getId())) {
            throw new BusinessException(ErrorCode.REPORT_ALREADY_EXISTS);
        }
    }
}
