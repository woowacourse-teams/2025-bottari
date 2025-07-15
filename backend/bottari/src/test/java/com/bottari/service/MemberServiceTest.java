package com.bottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.domain.Member;
import com.bottari.dto.CreateMemberRequest;
import com.bottari.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberServiceTest {

    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository);
    }

    @DisplayName("멤버를 생성한다")
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
        memberRepository.save(new Member(duplicateSsaid, "name"));
        final CreateMemberRequest request = new CreateMemberRequest(duplicateSsaid, "name");

        // when & then
        assertThatThrownBy(() -> memberService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 ssaid입니다.");
    }
}
