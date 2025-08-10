package com.bottari.bottaritemplate.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.bottaritemplate.domain.BottariTemplateItem;
import com.bottari.fixture.BottariTemplateFixture;
import com.bottari.fixture.BottariTemplateItemFixture;
import com.bottari.fixture.MemberFixture;
import com.bottari.member.domain.Member;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class BottariTemplateItemDeleteTest {

    @Autowired
    private BottariTemplateItemRepository bottariTemplateItemRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("보따리 템플릿 아이템 생성 시 is_deleted는 false이다.")
    @Test
    void when_create() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        final BottariTemplate bottariTemplate = BottariTemplateFixture.BOTTARI_TEMPLATE.get(member);
        entityManager.persist(bottariTemplate);
        final BottariTemplateItem bottariTemplateItem = BottariTemplateItemFixture.BOTTARI_TEMPLATE_ITEM_1.get(
                bottariTemplate);
        entityManager.persist(bottariTemplateItem);
        entityManager.flush();

        // when
        final BottariTemplateItem findBottariTemplateItem = entityManager.find(BottariTemplateItem.class,
                bottariTemplateItem.getId());

        // then
        assertThat(findBottariTemplateItem.isDeleted()).isFalse();
    }

    @DisplayName("보따리 템플릿 아이템 삭제 시, 데이터를 물리적으로 삭제한다.")
    @Test
    void when_delete() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        final BottariTemplate bottariTemplate = BottariTemplateFixture.BOTTARI_TEMPLATE.get(member);
        entityManager.persist(bottariTemplate);
        final BottariTemplateItem bottariTemplateItem = BottariTemplateItemFixture.BOTTARI_TEMPLATE_ITEM_1
                .get(bottariTemplate);
        entityManager.persist(bottariTemplateItem);
        entityManager.flush();

        // when
        entityManager.remove(bottariTemplateItem);
        entityManager.flush();

        // then
        final Optional<BottariTemplateItem> findBottariTemplateItem = entityManager.createNativeQuery(
                        "SELECT * FROM bottari_template_item WHERE id = :id", BottariTemplateItem.class)
                .setParameter("id", bottariTemplateItem.getId())
                .getResultStream()
                .findFirst();

        assertThat(findBottariTemplateItem).isEmpty();
    }

    @DisplayName("보따리 템플릿 아이디로 보따리 템플릿 아이템 삭제 시, 데이터를 물리적으로 삭제하지 않고 is_deleted를 true로 변경한다.")
    @Test
    void when_deleteByBottariTemplateId() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        final BottariTemplate bottariTemplate = BottariTemplateFixture.BOTTARI_TEMPLATE.get(member);
        entityManager.persist(bottariTemplate);
        final BottariTemplateItem bottariTemplateItem = BottariTemplateItemFixture.BOTTARI_TEMPLATE_ITEM_1
                .get(bottariTemplate);
        entityManager.persist(bottariTemplateItem);
        entityManager.flush();

        // when
        bottariTemplateItemRepository.deleteByBottariTemplateId(bottariTemplate.getId());
        bottariTemplateItemRepository.flush();

        // then
        final Optional<BottariTemplateItem> findBottariTemplateItem = entityManager.createNativeQuery(
                        "SELECT * FROM bottari_template_item WHERE id = :id", BottariTemplateItem.class)
                .setParameter("id", bottariTemplate.getId())
                .getResultStream()
                .findFirst();

        assertAll(
                () -> assertThat(findBottariTemplateItem).isPresent(),
                () -> assertThat(findBottariTemplateItem.get().isDeleted()).isTrue(),
                () -> assertThat(findBottariTemplateItem.get().getDeletedAt()).isNotNull()
        );
    }

    @DisplayName("보따리 템플릿 아이템 삭제 후 조회 시, 삭제된 아이템은 조회되지 않는다.")
    @Test
    void when_readAfterDelete() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        final BottariTemplate bottariTemplate = BottariTemplateFixture.BOTTARI_TEMPLATE.get(member);
        entityManager.persist(bottariTemplate);
        final BottariTemplateItem bottariTemplateItem = BottariTemplateItemFixture.BOTTARI_TEMPLATE_ITEM_1
                .get(bottariTemplate);
        entityManager.persist(bottariTemplateItem);
        entityManager.flush();

        bottariTemplateItemRepository.deleteByBottariTemplateId(bottariTemplate.getId());
        bottariTemplateItemRepository.flush();

        // when
        final BottariTemplateItem findBottariTemplateItem = entityManager.find(
                BottariTemplateItem.class,
                bottariTemplateItem.getId()
        );

        // then
        assertThat(findBottariTemplateItem).isNull();
    }
}
