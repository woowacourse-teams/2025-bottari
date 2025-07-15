package com.bottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.domain.Member;
import com.bottari.dto.CreateBottariRequest;
import com.bottari.repository.BottariRepository;
import com.bottari.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BottariServiceTest {

    private BottariService bottariService;

    @Autowired
    private BottariRepository bottariRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        bottariService = new BottariService(bottariRepository, memberRepository);
    }

    @DisplayName("보따리를 생성한다.")
    @Test
    void create() {
        // given
        final CreateBottariRequest request = new CreateBottariRequest("title");
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        memberRepository.save(member);

        // when
        final Long actual = bottariService.create(ssaid, request);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("존재하지 않는 ssaid로 보따리를 생성할 경우, 예외를 던진다.")
    @Test
    void create_Exception_NotExistsSsaid() {
        // given
        final CreateBottariRequest request = new CreateBottariRequest("title");
        final String ssaid = "ssaid";

        // when & then
        assertThatThrownBy(() -> bottariService.create(ssaid, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 ssaid로 가입된 사용자가 없습니다.");
    }
}
