package com.bottari.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.fixture.MemberFixture;
import com.bottari.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberSoftDeleteTest {

    @Autowired
    private EntityManager entityManager;

    @DisplayName("멤버 생성 시 is_deleted는 false이다.")
    @Test
    void softDelete_create() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        entityManager.flush();

        // when
        final Member findMember = entityManager.find(Member.class, member.getId());

        // then
        assertThat(findMember.isDeleted()).isFalse();
    }

    @DisplayName("멤버 삭제 시, 데이터를 물리적으로 삭제하지 않고 is_deleted를 true로 변경한다.")
    @Test
    void softDelete_delete() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        entityManager.flush();

        // when
        entityManager.remove(member);
        entityManager.flush();

        // then
        final Member findMember = (Member) entityManager.createNativeQuery(
                        "select * from member where id = :id", Member.class)
                .setParameter("id", member.getId())
                .getSingleResult();

        assertAll(
                () -> assertThat(findMember).isNotNull(),
                () -> assertThat(findMember.isDeleted()).isTrue(),
                () -> assertThat(findMember.getDeletedAt()).isNotNull()
        );
    }

    @DisplayName("멤버 조회 시, 삭제된 멤버는 조회되지 않는다.")
    @Test
    void softDelete_readAfterDelete() {
        // given
        final Member deleteMember = MemberFixture.MEMBER.get();
        entityManager.persist(deleteMember);
        entityManager.flush();
        entityManager.remove(deleteMember);
        entityManager.flush();

        // when
        final Member findMember = entityManager.find(Member.class, deleteMember.getId());

        // then
        assertThat(findMember).isNull();
    }

    @DisplayName("멤버 생성 시, 삭제된 멤버의 이름이나 ssaid로 생성할 수 있다.")
    @Test
    void softDelete_createAfterDelete() {
        // given
        final Member deleteMember = MemberFixture.MEMBER.get();
        entityManager.persist(deleteMember);
        entityManager.flush();
        entityManager.remove(deleteMember);
        entityManager.flush();

        // when & then
        final Member member = MemberFixture.MEMBER.get();
        assertThatCode(() -> {
            entityManager.persist(member);
            entityManager.flush();
        }).doesNotThrowAnyException();
    }
}
