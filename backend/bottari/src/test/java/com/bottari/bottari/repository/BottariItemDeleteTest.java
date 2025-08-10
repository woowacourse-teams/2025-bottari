package com.bottari.bottari.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.bottari.domain.Bottari;
import com.bottari.bottari.domain.BottariItem;
import com.bottari.fixture.BottariFixture;
import com.bottari.fixture.BottariItemFixture;
import com.bottari.fixture.MemberFixture;
import com.bottari.member.domain.Member;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class BottariItemDeleteTest {

    @Autowired
    private BottariItemRepository bottariItemRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("보따리 아이템 생성 시 is_deleted는 false이다.")
    @Test
    void when_create() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        final Bottari bottari = BottariFixture.BOTTARI.get(member);
        entityManager.persist(bottari);
        final BottariItem bottariItem = BottariItemFixture.BOTTARI_ITEM_1.get(bottari);
        entityManager.persist(bottariItem);
        entityManager.flush();

        // when
        final BottariItem findBottariItem = entityManager.find(BottariItem.class, bottariItem.getId());

        // then
        assertThat(findBottariItem.isDeleted()).isFalse();
    }

    @DisplayName("보따리 아이템 삭제 시, 데이터를 물리적으로 삭제한다.")
    @Test
    void when_delete() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        final Bottari bottari = BottariFixture.BOTTARI.get(member);
        entityManager.persist(bottari);
        final BottariItem bottariItem = BottariItemFixture.BOTTARI_ITEM_1.get(bottari);
        entityManager.persist(bottariItem);
        entityManager.flush();

        // when
        entityManager.remove(bottariItem);
        entityManager.flush();

        // then
        final Optional<BottariItem> findBottariItem = entityManager.createNativeQuery(
                        "SELECT * FROM bottari_item WHERE id = :id", BottariItem.class)
                .setParameter("id", bottariItem.getId())
                .getResultStream()
                .findFirst();

        assertThat(findBottariItem).isEmpty();
    }

    @DisplayName("보따리 아이디로 보따리 아이템 삭제 시, 데이터를 물리적으로 삭제하지 않고 is_deleted를 true로 변경한다.")
    @Test
    void when_deleteByBottariId() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        final Bottari bottari = BottariFixture.BOTTARI.get(member);
        entityManager.persist(bottari);
        final BottariItem bottariItem = BottariItemFixture.BOTTARI_ITEM_1.get(bottari);
        entityManager.persist(bottariItem);
        entityManager.flush();

        // when
        bottariItemRepository.deleteByBottariId(bottari.getId());
        bottariItemRepository.flush();

        // then
        final Optional<BottariItem> findBottariItem = entityManager.createNativeQuery(
                        "SELECT * FROM bottari_item WHERE id = :id", BottariItem.class)
                .setParameter("id", bottariItem.getId())
                .getResultStream()
                .findFirst();

        assertAll(
                () -> assertThat(findBottariItem).isPresent(),
                () -> assertThat(findBottariItem.get().isDeleted()).isTrue(),
                () -> assertThat(findBottariItem.get().getDeletedAt()).isNotNull()
        );
    }

    @DisplayName("보따리 아이템 삭제 후 조회 시, 삭제된 아이템은 조회되지 않는다.")
    @Test
    void when_readAfterDelete() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);
        final Bottari bottari = BottariFixture.BOTTARI.get(member);
        entityManager.persist(bottari);
        final BottariItem bottariItem = BottariItemFixture.BOTTARI_ITEM_1.get(bottari);
        entityManager.persist(bottariItem);
        entityManager.flush();

        bottariItemRepository.deleteByBottariId(bottari.getId());
        bottariItemRepository.flush();

        // when
        final BottariItem findBottariItem = entityManager.find(BottariItem.class, bottariItem.getId());

        // then
        assertThat(findBottariItem).isNull();
    }
}
