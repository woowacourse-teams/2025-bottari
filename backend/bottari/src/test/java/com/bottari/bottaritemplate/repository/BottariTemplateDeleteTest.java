package com.bottari.bottaritemplate.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.fixture.BottariTemplateFixture;
import com.bottari.fixture.MemberFixture;
import com.bottari.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class BottariTemplateDeleteTest {

    @Autowired
    private EntityManager entityManager;

    @DisplayName("보따리 템플릿 생성 시 deleted_at은 null이다.")
    @Test
    void when_create() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        final BottariTemplate bottariTemplate = BottariTemplateFixture.BOTTARI_TEMPLATE.get(member);
        entityManager.persist(bottariTemplate);
        entityManager.flush();

        // when
        final BottariTemplate findBottariTemplate = entityManager.find(BottariTemplate.class, bottariTemplate.getId());

        // then
        assertThat(findBottariTemplate.getDeletedAt()).isNull();
    }

    @DisplayName("보따리 템플릿 삭제 시, 데이터를 물리적으로 삭제하지 않고 deleted_at에 삭제된 시간을 추가한다.")
    @Test
    void when_delete() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        final BottariTemplate bottariTemplate = BottariTemplateFixture.BOTTARI_TEMPLATE.get(member);
        entityManager.persist(bottariTemplate);
        entityManager.flush();

        // when
        entityManager.remove(bottariTemplate);
        entityManager.flush();

        // then
        final BottariTemplate findBottariTemplate = (BottariTemplate) entityManager.createNativeQuery(
                        "SELECT * FROM bottari_template WHERE id = :id", BottariTemplate.class)
                .setParameter("id", bottariTemplate.getId())
                .getSingleResult();

        assertAll(
                () -> assertThat(findBottariTemplate).isNotNull(),
                () -> assertThat(findBottariTemplate.getDeletedAt()).isNotNull()
        );
    }

    @DisplayName("보따리 템플릿 조회 시, 삭제된 템플릿은 조회되지 않는다.")
    @Test
    void when_readAfterDelete() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        final BottariTemplate bottariTemplate = BottariTemplateFixture.BOTTARI_TEMPLATE.get(member);
        entityManager.persist(bottariTemplate);
        entityManager.flush();
        entityManager.remove(bottariTemplate);
        entityManager.flush();

        // when
        final BottariTemplate findBottariTemplate = entityManager.find(BottariTemplate.class, bottariTemplate.getId());

        // then
        assertThat(findBottariTemplate).isNull();
    }
}
