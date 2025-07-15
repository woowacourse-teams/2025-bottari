package com.bottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.domain.Member;
import com.bottari.dto.CreateMemberRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(MemberService.class)
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("사용자를 생성한다.")
    @Test
    void create() {
        // given
        final CreateMemberRequest request = new CreateMemberRequest("ssaid", "name");

        // when
        final Long actual = memberService.create(request);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("중복된 ssaid로 사용자를 생성할 경우, 예외를 던진다.")
    @Test
    void create_Exception_DuplicateSsaid() {
        // given
        final String duplicateSsaid = "duplicateSsaid";
        entityManager.persist(new Member(duplicateSsaid, "name"));
        final CreateMemberRequest request = new CreateMemberRequest(duplicateSsaid, "name");

        // when & then
        assertThatThrownBy(() -> memberService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 ssaid입니다.");
    }
}
