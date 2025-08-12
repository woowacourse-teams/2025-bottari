package com.bottari.teambottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.config.JpaAuditingConfig;
import com.bottari.error.BusinessException;
import com.bottari.fixture.MemberFixture;
import com.bottari.member.domain.Member;
import com.bottari.teambottari.dto.CreateTeamBottariRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({TeamBottariService.class, JpaAuditingConfig.class})
class TeamBottariServiceTest {

    @Autowired
    private TeamBottariService teamBottariService;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class CreateTest {

        @DisplayName("팀 보따리를 생성한다.")
        @Test
        void create() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final CreateTeamBottariRequest request = new CreateTeamBottariRequest("새로운 팀 보따리");

            // when
            final Long teamBottariId = teamBottariService.create(member.getSsaid(), request);

            // then
            assertThat(teamBottariId).isNotNull();
        }

        @DisplayName("존재하지 않는 사용자로 팀 보따리를 생성할 경우, 예외를 던진다.")
        @Test
        void create_WithNonExistentMember_ThrowsException() {
            // given
            final String nonExistentSsaid = "non-existent-ssaid";
            final CreateTeamBottariRequest request = new CreateTeamBottariRequest("새로운 팀 보따리");

            // when & then
            assertThatThrownBy(() -> teamBottariService.create(nonExistentSsaid, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("사용자를 찾을 수 없습니다. - 등록되지 않은 ssaid입니다.");
        }
    }
}
