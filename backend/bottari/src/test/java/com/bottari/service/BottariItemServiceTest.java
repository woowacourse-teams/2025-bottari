package com.bottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.domain.Bottari;
import com.bottari.domain.BottariItem;
import com.bottari.domain.Member;
import com.bottari.dto.CreateBottariItemRequest;
import com.bottari.dto.EditBottariItemsRequest;
import com.bottari.dto.ReadBottariItemResponse;
import com.bottari.service.fixture.BottariFixture;
import com.bottari.service.fixture.BottariItemFixture;
import com.bottari.service.fixture.MemberFixture;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Nested
    class GetAllByBottariIdTest {

        @DisplayName("보따리 체크리스트를 조회한다.")
        @Test
        void getAllByBottariId() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Bottari bottari1 = BottariFixture.BOTTARI.get(member);
            final Bottari bottari2 = BottariFixture.ANOTHER_BOTTARI.get(member);
            entityManager.persist(bottari1);
            entityManager.persist(bottari2);

            final BottariItem bottariItem1 = BottariItemFixture.BOTTARI_ITEM_1.get(bottari1);
            final BottariItem bottariItem2 = BottariItemFixture.BOTTARI_ITEM_2.get(bottari2);
            entityManager.persist(bottariItem1);
            entityManager.persist(bottariItem2);

            // when
            final List<ReadBottariItemResponse> actual = bottariItemService.getAllByBottariId(bottari2.getId());

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(1),
                    () -> assertThat(actual.getFirst()).isEqualTo(ReadBottariItemResponse.from(bottariItem2))
            );
        }

        @DisplayName("존재하지 않는 보따리의 물품을 조회할 경우, 예외를 던진다.")
        @Test
        void getAllByBottariId_Exception_NotFoundBottari() {
            // given
            final Long notFoundBottariId = 1L;

            // when & then
            assertThatThrownBy(() -> bottariItemService.getAllByBottariId(notFoundBottariId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("보따리를 찾을 수 없습니다.");
        }
    }

    @Nested
    class CreateTest {
        @DisplayName("보따리 물품을 생성한다.")
        @Test
        void create() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Bottari bottari = BottariFixture.BOTTARI.get(member);
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
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Bottari bottari = BottariFixture.BOTTARI.get(member);
            entityManager.persist(bottari);

            final BottariItem bottariItem = BottariItemFixture.BOTTARI_ITEM_1.get(bottari);
            entityManager.persist(bottariItem);

            final String duplicateItemName = bottariItem.getName();

            final CreateBottariItemRequest request = new CreateBottariItemRequest(duplicateItemName);

            // when & then
            assertThatThrownBy(() -> bottariItemService.create(bottari.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("중복된 보따리 물품명입니다.");
        }
    }

    @Nested
    class UpdateTest {

        @DisplayName("보따리 물품을 수정한다.")
        @Test
        void update() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Bottari bottari = BottariFixture.BOTTARI.get(member);
            entityManager.persist(bottari);

            final BottariItem bottariItem_1 = BottariItemFixture.BOTTARI_ITEM_1.get(bottari);
            final BottariItem bottariItem_2 = BottariItemFixture.BOTTARI_ITEM_2.get(bottari);
            final BottariItem bottariItem_3 = BottariItemFixture.BOTTARI_ITEM_3.get(bottari);
            entityManager.persist(bottariItem_1);
            entityManager.persist(bottariItem_2);
            entityManager.persist(bottariItem_3);

            final Bottari anotherBottari = BottariFixture.ANOTHER_BOTTARI.get(member);
            entityManager.persist(anotherBottari);

            final BottariItem anotherBottariItem = new BottariItem("another_name", anotherBottari);
            entityManager.persist(anotherBottariItem);

            final Long updateBottariId = bottari.getId();

            final List<Long> deleteIds = List.of(bottariItem_1.getId(), bottariItem_2.getId());
            final List<String> createNames = List.of("newName_1", "newName_2");
            final EditBottariItemsRequest request = new EditBottariItemsRequest(deleteIds, createNames);

            // when
            bottariItemService.update(updateBottariId, request);

            // then
            final List<BottariItem> actual = entityManager.createQuery(
                            "select i from BottariItem i where i.bottari.id = :bottariId", BottariItem.class)
                    .setParameter("bottariId", updateBottariId)
                    .getResultList();

            final List<BottariItem> anotherActual = entityManager.createQuery(
                            "select i from BottariItem i where i.bottari.id = :bottariId", BottariItem.class)
                    .setParameter("bottariId", anotherBottari.getId())
                    .getResultList();

            assertAll(
                    () -> assertThat(actual).extracting("name")
                            .containsExactly(bottariItem_3.getName(), "newName_1", "newName_2"),
                    () -> assertThat(anotherActual).hasSize(1),
                    () -> assertThat(anotherActual.getFirst().getName()).isEqualTo("another_name")
            );
        }

        @DisplayName("수정하려는 아이템 아이디에 중복이 존재하는 경우, 예외를 던진다.")
        @Test
        void update_Exception_DuplicateDeleteIds() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Bottari bottari = BottariFixture.BOTTARI.get(member);
            entityManager.persist(bottari);

            final List<Long> duplicateDeleteIds = List.of(1L, 1L, 2L);
            final EditBottariItemsRequest request = new EditBottariItemsRequest(duplicateDeleteIds, List.of());

            // when & then
            assertThatThrownBy(() -> bottariItemService.update(bottari.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("삭제하려는 아이템에 중복이 있습니다.");
        }

        @DisplayName("수정 시 보따리가 존재하지 않는 경우, 예외를 던진다.")
        @Test
        void update_Exception_NotFoundBottari() {
            // given
            final Long invalidBottariId = -1L;
            final EditBottariItemsRequest request = new EditBottariItemsRequest(List.of(), List.of());

            // when & then
            assertThatThrownBy(() -> bottariItemService.update(invalidBottariId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("보따리를 찾을 수 없습니다.");
        }

        @DisplayName("수정 시 삭제하려는 물품이 보따리 내에 존재하지 않는 경우, 예외를 던진다.")
        @Test
        void update_Exception_NotExistsItemInBottari() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Bottari bottari = BottariFixture.BOTTARI.get(member);
            entityManager.persist(bottari);

            final BottariItem bottariItem_1 = BottariItemFixture.BOTTARI_ITEM_1.get(bottari);
            final BottariItem bottariItem_2 = BottariItemFixture.BOTTARI_ITEM_2.get(bottari);
            final BottariItem bottariItem_3 = BottariItemFixture.BOTTARI_ITEM_3.get(bottari);
            entityManager.persist(bottariItem_1);
            entityManager.persist(bottariItem_2);
            entityManager.persist(bottariItem_3);

            final Long bottariId = bottari.getId();

            final List<Long> deleteIds = List.of(
                    bottariItem_1.getId(),
                    bottariItem_2.getId(),
                    Long.MAX_VALUE
            );

            final EditBottariItemsRequest request = new EditBottariItemsRequest(deleteIds, List.of());

            // when & then
            assertThatThrownBy(() -> bottariItemService.update(bottariId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("보따리 안에 없는 물품은 삭제할 수 없습니다.");
        }

        @DisplayName("수정 시 추가하려는 물품명에 중복이 존재하는 경우, 예외를 던진다.")
        @Test
        void update_Exception_DuplicateItemNameInRequest() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Bottari bottari = BottariFixture.BOTTARI.get(member);
            entityManager.persist(bottari);

            final Long bottariId = bottari.getId();

            final List<String> duplicateCreateNames = List.of(
                    "newName",
                    "duplicate_name",
                    "duplicate_name"
            );

            final EditBottariItemsRequest request = new EditBottariItemsRequest(List.of(), duplicateCreateNames);

            // when & then
            assertThatThrownBy(() -> bottariItemService.update(bottariId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("중복된 물품이 존재합니다.");
        }

        @DisplayName("수정 시 추가하려는 물품이 이미 존재하는 경우, 예외를 던진다.")
        @Test
        void update_Exception_AlreadyExistsItemName() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Bottari bottari = BottariFixture.BOTTARI.get(member);
            entityManager.persist(bottari);

            final BottariItem bottariItem = BottariItemFixture.BOTTARI_ITEM_1.get(bottari);
            entityManager.persist(bottariItem);
            final String duplicateName = bottariItem.getName();

            final Long bottariId = bottari.getId();

            final List<String> duplicateNameInDB = List.of(
                    "newName",
                    duplicateName
            );
            final EditBottariItemsRequest request = new EditBottariItemsRequest(
                    List.of(),
                    duplicateNameInDB
            );

            // when & then
            assertThatThrownBy(() -> bottariItemService.update(bottariId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("중복된 물품이 존재합니다.");
        }

        @DisplayName("수정 시 총 보따리 물품이 200개가 넘어가는 경우, 예외가 발생한다.")
        @Test
        void update_Exception_OverMaxBottariItemsCount() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Bottari bottari = BottariFixture.BOTTARI.get(member);
            entityManager.persist(bottari);

            for (int i = 0; i < 198; i++) {
                final BottariItem bottariItem = new BottariItem("name" + i, bottari);
                entityManager.persist(bottariItem);
            }

            final EditBottariItemsRequest request = new EditBottariItemsRequest(
                    List.of(),
                    List.of("newItem1", "newItem2", "newItem3")
            );

            // when & then
            assertThatThrownBy(() -> bottariItemService.update(bottari.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("물품은 최대 200개까지 보따리에 넣을 수 있습니다.");
        }
    }

    @Nested
    class DeleteTest {

        @DisplayName("보따리 물품을 삭제한다.")
        @Test
        void delete() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Bottari bottari = BottariFixture.BOTTARI.get(member);
            entityManager.persist(bottari);

            final BottariItem bottariItem = BottariItemFixture.BOTTARI_ITEM_1.get(bottari);
            entityManager.persist(bottariItem);

            // when
            bottariItemService.delete(bottariItem.getId());

            // then
            assertThat(entityManager.contains(bottariItem)).isFalse();
        }
    }

    @Nested
    class CheckTest {

        @DisplayName("보따리 물품을 체크한다.")
        @Test
        void check() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Bottari bottari = BottariFixture.BOTTARI.get(member);
            entityManager.persist(bottari);

            final BottariItem bottariItem = BottariItemFixture.BOTTARI_ITEM_1.get(bottari);
            entityManager.persist(bottariItem);

            // when
            bottariItemService.check(bottariItem.getId());

            // then
            final BottariItem actual = entityManager.find(BottariItem.class, bottariItem.getId());
            assertThat(actual.isChecked()).isTrue();
        }

        @DisplayName("물품 체크 시, 보따리 물품을 찾을 수 없다면, 예외를 던진다.")
        @Test
        void check_Exception_NotExistsItem() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long notExistsBottariItemId = 1L;

            // when & then
            assertThatThrownBy(() -> bottariItemService.check(notExistsBottariItemId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("보따리 물품을 찾을 수 없습니다.");
        }
    }

    @Nested
    class UncheckTest {

        @DisplayName("보따리 물품을 체크 해제한다.")
        @Test
        void uncheck() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Bottari bottari = BottariFixture.BOTTARI.get(member);
            entityManager.persist(bottari);

            final BottariItem bottariItem = BottariItemFixture.BOTTARI_ITEM_1.get(bottari);
            bottariItem.check();
            entityManager.persist(bottariItem);

            // when
            bottariItemService.uncheck(bottariItem.getId());

            // then
            final BottariItem actual = entityManager.find(BottariItem.class, bottariItem.getId());
            assertThat(actual.isChecked()).isFalse();
        }

        @DisplayName("물품 체크 해제 시, 보따리 물품을 찾을 수 없다면, 예외를 던진다.")
        @Test
        void uncheck_Exception_NotExistsItem() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long notExistsBottariItemId = 1L;

            // when & then
            assertThatThrownBy(() -> bottariItemService.uncheck(notExistsBottariItemId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("보따리 물품을 찾을 수 없습니다.");
        }
    }
}
