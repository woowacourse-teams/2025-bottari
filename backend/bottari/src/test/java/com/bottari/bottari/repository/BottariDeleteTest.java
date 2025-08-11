package com.bottari.bottari.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.bottari.domain.Bottari;
import com.bottari.fixture.BottariFixture;
import com.bottari.fixture.MemberFixture;
import com.bottari.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class BottariDeleteTest {

    @Autowired
    private EntityManager entityManager;

    @DisplayName("보따리 생성 시 deleted_at은 null이다.")
    @Test
    void when_create() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        final Bottari bottari = BottariFixture.BOTTARI.get(member);
        entityManager.persist(bottari);
        entityManager.flush();

        // when
        final Bottari findBottari = entityManager.find(Bottari.class, bottari.getId());

        // then
        assertThat(findBottari.getDeletedAt()).isNull();
    }

    @DisplayName("보따리 삭제 시, 데이터를 물리적으로 삭제하지 않고 deleted_at에 삭제된 시간을 추가한다.")
    @Test
    void when_delete() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        final Bottari bottari = BottariFixture.BOTTARI.get(member);
        entityManager.persist(bottari);
        entityManager.flush();

        // when
        entityManager.remove(bottari);
        entityManager.flush();

        // then
        final Bottari findBottari = (Bottari) entityManager.createNativeQuery(
                        "SELECT * FROM bottari WHERE id = :id", Bottari.class)
                .setParameter("id", bottari.getId())
                .getSingleResult();

        assertAll(
                () -> assertThat(findBottari).isNotNull(),
                () -> assertThat(findBottari.getDeletedAt()).isNotNull()
        );
    }

    @DisplayName("보따리 조회 시, 삭제된 보따리는 조회되지 않는다.")
    @Test
    void when_readAfterDelete() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        final Bottari deleteBottari = BottariFixture.BOTTARI.get(member);
        entityManager.persist(deleteBottari);
        entityManager.flush();
        entityManager.remove(deleteBottari);

        // when
        final Bottari findBottari = entityManager.find(Bottari.class, deleteBottari.getId());

        // then
        assertThat(findBottari).isNull();
    }
}
