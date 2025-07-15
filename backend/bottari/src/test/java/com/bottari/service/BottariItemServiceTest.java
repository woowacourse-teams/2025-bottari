package com.bottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.domain.Bottari;
import com.bottari.domain.BottariItem;
import com.bottari.domain.Member;
import com.bottari.dto.CreateBottariItemRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(BottariItemService.class)
class BottariItemServiceTest {

    @Autowired
    private BottariItemService bottariItemService;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("보따리 물품을 생성한다.")
    @Test
    void create() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari bottari = new Bottari("title", member);
        entityManager.persist(bottari);

        final CreateBottariItemRequest request = new CreateBottariItemRequest("itemName");

        // when
        final Long actual = bottariItemService.create(bottari.getId(), request);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("존재하지 않는 보따리에 물품을 생성할 경우, 예외를 던진다.")
    @Test
    void create_Exception_NotFoundBottari() {
        // given
        final CreateBottariItemRequest request = new CreateBottariItemRequest("itemName");
        final Long notFoundBottariId = 1L;

        // when & then
        assertThatThrownBy(() -> bottariItemService.create(notFoundBottariId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("보따리를 찾을 수 없습니다.");
    }

    @DisplayName("같은 보따리 내에 중복되는 물품명이 존재할 경우, 예외를 던진다.")
    @Test
    void create_Exception_DuplicateName() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari bottari = new Bottari("title", member);
        entityManager.persist(bottari);

        final String duplicateItemName = "name";
        final BottariItem bottariItem = new BottariItem(duplicateItemName, bottari);
        entityManager.persist(bottariItem);

        final CreateBottariItemRequest request = new CreateBottariItemRequest(duplicateItemName);

        // when & then
        assertThatThrownBy(() -> bottariItemService.create(bottari.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 보따리 물품명입니다.");
    }

    @DisplayName("보따리 물품을 삭제한다.")
    @Test
    void delete() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari bottari = new Bottari("title", member);
        entityManager.persist(bottari);

        final String duplicateItemName = "name";
        final BottariItem bottariItem = new BottariItem(duplicateItemName, bottari);
        entityManager.persist(bottariItem);

        // when
        bottariItemService.delete(bottariItem.getId());

        // then
        assertThat(entityManager.contains(bottariItem)).isFalse();
    }
}
