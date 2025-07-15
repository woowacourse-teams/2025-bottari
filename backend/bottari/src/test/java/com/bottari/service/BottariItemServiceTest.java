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
        bottariItemService.delete(bottari.getId(), bottariItem.getId());

        // then
        assertThat(entityManager.contains(bottariItem)).isFalse();
    }

    @DisplayName("해당 보따리 내의 물품이 아니면, 예외를 던진다.")
    @Test
    void delete_Exception_AnotherBottari() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari bottari = new Bottari("title", member);
        entityManager.persist(bottari);

        final Bottari anotherBottari = new Bottari("anotherTitle", member);
        entityManager.persist(anotherBottari);

        final String duplicateItemName = "name";
        final BottariItem bottariItem = new BottariItem(duplicateItemName, bottari);
        entityManager.persist(bottariItem);

        // when & then
        assertThatThrownBy(() -> bottariItemService.delete(anotherBottari.getId(), bottariItem.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 보따리 내에 존재하는 물품이 아닙니다.");
    }

    @DisplayName("보따리 물품을 체크한다.")
    @Test
    void check() {
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
        bottariItemService.check(bottari.getId(), bottariItem.getId());

        // then
        final BottariItem actual = entityManager.find(BottariItem.class, bottariItem.getId());
        assertThat(actual.isChecked()).isTrue();
    }

    @DisplayName("물품 체크 시, 보따리 물품을 찾을 수 없다면, 예외를 던진다.")
    @Test
    void check_Exception_NotExistsItem() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari bottari = new Bottari("title", member);
        entityManager.persist(bottari);

        final Long notExistsBottariItemId = 1L;

        // when & then
        assertThatThrownBy(() -> bottariItemService.check(bottari.getId(), notExistsBottariItemId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("보따리 물품을 찾을 수 없습니다.");
    }

    @DisplayName("물품 체크 시, 해당 보따리 내의 물품이 아니면, 예외를 던진다.")
    @Test
    void check_Exception_AnotherBottari() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari bottari = new Bottari("title", member);
        entityManager.persist(bottari);

        final Bottari anotherBottari = new Bottari("anotherTitle", member);
        entityManager.persist(anotherBottari);

        final BottariItem bottariItem = new BottariItem("name", bottari);
        entityManager.persist(bottariItem);

        // when & then
        assertThatThrownBy(() -> bottariItemService.check(anotherBottari.getId(), bottariItem.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 보따리 내에 존재하는 물품이 아닙니다.");
    }

    @DisplayName("보따리 물품을 체크 해제한다.")
    @Test
    void uncheck() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari bottari = new Bottari("title", member);
        entityManager.persist(bottari);

        final String duplicateItemName = "name";
        final BottariItem bottariItem = new BottariItem(duplicateItemName, bottari);
        bottariItem.check();
        entityManager.persist(bottariItem);

        // when
        bottariItemService.uncheck(bottari.getId(), bottariItem.getId());

        // then
        final BottariItem actual = entityManager.find(BottariItem.class, bottariItem.getId());
        assertThat(actual.isChecked()).isFalse();
    }

    @DisplayName("물품 체크 해제 시, 보따리 물품을 찾을 수 없다면, 예외를 던진다.")
    @Test
    void uncheck_Exception_NotExistsItem() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari bottari = new Bottari("title", member);
        entityManager.persist(bottari);

        final Long notExistsBottariItemId = 1L;

        // when & then
        assertThatThrownBy(() -> bottariItemService.uncheck(bottari.getId(), notExistsBottariItemId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("보따리 물품을 찾을 수 없습니다.");
    }

    @DisplayName("물품 체크 해제 시, 해당 보따리 내의 물품이 아니면, 예외를 던진다.")
    @Test
    void uncheck_Exception_AnotherBottari() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari bottari = new Bottari("title", member);
        entityManager.persist(bottari);

        final Bottari anotherBottari = new Bottari("anotherTitle", member);
        entityManager.persist(anotherBottari);

        final BottariItem bottariItem = new BottariItem("name", bottari);
        bottariItem.check();
        entityManager.persist(bottariItem);

        // when & then
        assertThatThrownBy(() -> bottariItemService.uncheck(anotherBottari.getId(), bottariItem.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 보따리 내에 존재하는 물품이 아닙니다.");
    }
}
