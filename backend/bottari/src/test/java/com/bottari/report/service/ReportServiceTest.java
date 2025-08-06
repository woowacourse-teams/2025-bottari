package com.bottari.report.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.bottaritemplate.dto.ReportBottariTemplateRequest;
import com.bottari.error.BusinessException;
import com.bottari.member.domain.Member;
import com.bottari.report.domain.Report;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(ReportService.class)
class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class ReportBottariTemplateTest {

        @DisplayName("보따리 템플릿을 신고할 경우, 신고 기록이 저장된다.")
        @Test
        void reportBottariTemplate() {
            // given
            final Member owner = new Member("owner_ssaid", "owner");
            entityManager.persist(owner);

            final Member reporter = new Member("reporter_ssaid", "reporter");
            entityManager.persist(reporter);

            final BottariTemplate bottariTemplateToReport = new BottariTemplate("title", owner);
            entityManager.persist(bottariTemplateToReport);
            final BottariTemplate AnotherBottariTemplate = new BottariTemplate("title", owner);
            entityManager.persist(AnotherBottariTemplate);

            final String reportReason = "reason";
            final ReportBottariTemplateRequest request = new ReportBottariTemplateRequest(reportReason);

            // when
            reportService.reportBottariTemplate(reporter.getSsaid(), bottariTemplateToReport.getId(), request);

            // then
            final Report actual = entityManager.createQuery(
                            """
                                         SELECT r
                                         FROM Report r
                                         WHERE r.reporter.id = :reporterId
                                         AND r.bottariTemplate.id = : bottariTemplateId
                                    """, Report.class)
                    .setParameter("reporterId", reporter.getId())
                    .setParameter("bottariTemplateId", bottariTemplateToReport.getId())
                    .getSingleResult();

            assertAll(
                    () -> assertThat(actual.getBottariTemplate().getId()).isEqualTo(bottariTemplateToReport.getId()),
                    () -> assertThat(actual.getReporter().getId()).isEqualTo(reporter.getId()),
                    () -> assertThat(actual.getReason()).isEqualTo(reportReason)
            );
        }

        @DisplayName("보따리 템플릿을 신고할 경우, 신고자 ssaid가 존재하지 않으면 예외가 발생한다.")
        @Test
        void reportBottariTemplate_Exception_NotExistReporter() {
            // given
            final Member owner = new Member("ssaid", "name");
            entityManager.persist(owner);

            final BottariTemplate bottariTemplate = new BottariTemplate("title", owner);
            entityManager.persist(bottariTemplate);

            final String invalidSSsaid = "invalid_ssaid";
            final ReportBottariTemplateRequest request = new ReportBottariTemplateRequest("reason");

            // when & then
            assertThatThrownBy(
                    () -> reportService.reportBottariTemplate(invalidSSsaid, bottariTemplate.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("사용자를 찾을 수 없습니다. - 등록되지 않은 ssaid입니다.");
        }

        @DisplayName("보따리 템플릿을 신고할 경우, 신고하려는 보따리 템플릿이 존재하지 않으면 예외가 발생한다.")
        @Test
        void reportBottariTemplate_Exception_NotExistBottariTemplate() {
            // given
            final Member reporter = new Member("ssaid", "name");
            entityManager.persist(reporter);

            final Long invalidBottariTemplateId = 1L;
            final ReportBottariTemplateRequest request = new ReportBottariTemplateRequest("reason");

            // when & then
            assertThatThrownBy(
                    () -> reportService.reportBottariTemplate(reporter.getSsaid(), invalidBottariTemplateId, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("보따리 템플릿을 찾을 수 없습니다.");
        }

        @DisplayName("보따리 템플릿을 신고할 경우, 보따리 템플릿에 대해 같은 사용자가 한번 더 신고하는 경우 예외가 발생한다.")
        @Test
        void reportBottariTemplate_Exception_AlreadyReported() {
            // given
            final Member owner = new Member("owner_ssaid", "owner");
            entityManager.persist(owner);

            final Member reporter = new Member("reporter_ssaid", "reporter");
            entityManager.persist(reporter);

            final BottariTemplate bottariTemplate = new BottariTemplate("title", owner);
            entityManager.persist(bottariTemplate);

            final Report report = new Report(bottariTemplate, reporter, "reason");
            entityManager.persist(report);

            final ReportBottariTemplateRequest request = new ReportBottariTemplateRequest("reason2");

            // when & then
            assertThatThrownBy(
                    () -> reportService.reportBottariTemplate(reporter.getSsaid(), bottariTemplate.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("이미 해당 템플릿에 대한 신고 기록이 있습니다.");
        }
    }
}
