package com.bottari.report.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.fixture.BottariTemplateFixture;
import com.bottari.fixture.MemberFixture;
import com.bottari.member.domain.Member;
import com.bottari.report.domain.Report;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ReportDeleteTest {

    @Autowired
    private EntityManager entityManager;

    @DisplayName("신고 생성 시 deleted_at은 null이다.")
    @Test
    void when_create() {
        // given
        final Member memer = MemberFixture.MEMBER.get();
        entityManager.persist(memer);
        final BottariTemplate bottariTemplate = BottariTemplateFixture.BOTTARI_TEMPLATE.get(memer);
        entityManager.persist(bottariTemplate);
        final Report report = new Report(
                bottariTemplate,
                memer,
                "reason"
        );
        entityManager.persist(report);
        entityManager.flush();

        // when
        final Report findReport = entityManager.find(Report.class, report.getId());

        // then
        assertThat(findReport.getDeletedAt()).isNull();
    }

    @DisplayName("신고 삭제 시, 데이터를 물리적으로 삭제하지 않고 deleted_at에 삭제된 시간을 추가한다.")
    @Test
    void when_delete() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        final BottariTemplate bottariTemplate = BottariTemplateFixture.BOTTARI_TEMPLATE.get(member);
        entityManager.persist(bottariTemplate);
        final Report report = new Report(
                bottariTemplate,
                member,
                "reason"
        );
        entityManager.persist(report);
        entityManager.flush();

        // when
        entityManager.remove(report);
        entityManager.flush();

        // then
        final Report findReport = (Report) entityManager.createNativeQuery(
                        "select * from report where id = :id", Report.class)
                .setParameter("id", report.getId())
                .getSingleResult();

        assertAll(
                () -> assertThat(findReport).isNotNull(),
                () -> assertThat(findReport.getDeletedAt()).isNotNull()
        );
    }

    @DisplayName("신고 조회 시, 삭제된 신고는 조회되지 않는다.")
    @Test
    void when_readAfterDelete() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        final BottariTemplate bottariTemplate = BottariTemplateFixture.BOTTARI_TEMPLATE.get(member);
        entityManager.persist(bottariTemplate);
        final Report deleteReport = new Report(
                bottariTemplate,
                member,
                "reason"
        );
        entityManager.persist(deleteReport);
        entityManager.flush();
        entityManager.remove(deleteReport);
        entityManager.flush();

        // when
        final Report findReport = entityManager.find(Report.class, deleteReport.getId());

        // then
        assertThat(findReport).isNull();
    }
}
