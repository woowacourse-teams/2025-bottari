package com.bottari.service;

import com.bottari.domain.BottariTemplate;
import com.bottari.domain.Member;
import com.bottari.domain.Report;
import com.bottari.dto.ReportBottariTemplateRequest;
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
                .orElseThrow(() -> new IllegalArgumentException("보따리 템플릿을 찾을 수 없습니다."));
        final Member reporter = memberRepository.findBySsaid(reporterSsaid)
                .orElseThrow(() -> new IllegalArgumentException("해당 ssaid로 가입된 사용자가 없습니다."));
        checkAlreadyReportedBottariTemplate(bottariTemplate, reporter);
        final Report report = new Report(bottariTemplate, reporter, request.reason());
        reportRepository.save(report);
    }

    private void checkAlreadyReportedBottariTemplate(
            final BottariTemplate bottariTemplate,
            final Member reporter
    ) {
        if (reportRepository.existsByBottariTemplateIdAndReporterId(bottariTemplate.getId(), reporter.getId())) {
            throw new IllegalArgumentException("이미 해당 템플릿에 대한 신고 기록이 있습니다.");
        }
    }
}
