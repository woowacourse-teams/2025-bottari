package com.bottari.report.service;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.bottaritemplate.dto.ReportBottariTemplateRequest;
import com.bottari.bottaritemplate.repository.BottariTemplateRepository;
import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.member.domain.Member;
import com.bottari.member.repository.MemberRepository;
import com.bottari.report.domain.Report;
import com.bottari.report.repository.ReportRepository;
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
