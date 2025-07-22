package com.bottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.domain.Bottari;
import com.bottari.domain.BottariItem;
import com.bottari.domain.BottariTemplate;
import com.bottari.domain.BottariTemplateItem;
import com.bottari.domain.Member;
import com.bottari.dto.CreateBottariTemplateRequest;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(BottariTemplateService.class)
class BottariTemplateServiceTest {

    @Autowired
    private BottariTemplateService bottariTemplateService;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("보따리 템플릿을 생성한다.")
    @Test
    void create() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final List<String> bottariTemplateItems = List.of("item1", "item2", "item3");
        final CreateBottariTemplateRequest request = new CreateBottariTemplateRequest(
                "title",
                bottariTemplateItems
        );

        // when
        final Long actual = bottariTemplateService.create(ssaid, request);

        // then
        final List<BottariTemplateItem> actualItems = entityManager.createQuery("""
                        select i from BottariTemplateItem i
                        where i.bottariTemplate.id =: bottariTemplateId
                        """, BottariTemplateItem.class)
                .setParameter("bottariTemplateId", actual)
                .getResultList();

        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actualItems).extracting("name")
                    .containsExactlyInAnyOrderElementsOf(bottariTemplateItems);
        });
    }

    @DisplayName("생성 시 존재하지 않는 사용자라면, 예외를 던진다.")
    @Test
    void create_Exception_NotExistsMember() {
        // given
        final List<String> bottariTemplateItems = List.of("item1", "item2", "item3");
        final CreateBottariTemplateRequest request = new CreateBottariTemplateRequest(
                "title",
                bottariTemplateItems
        );
        final String invalidSsaid = "invalid_ssaid";

        // when & then
        assertThatThrownBy(() -> bottariTemplateService.create(invalidSsaid, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 ssaid로 가입된 사용자가 없습니다.");
    }

    @DisplayName("생성 시 추가하려는 물품명에 중복이 존재하는 경우, 예외를 던진다.")
    @Test
    void create_Exception_DuplicateItemNameInRequest() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final List<String> bottariTemplateItems = List.of("item1", "duplicate_item", "duplicate_item");
        final CreateBottariTemplateRequest request = new CreateBottariTemplateRequest(
                "title",
                bottariTemplateItems
        );

        // when & then
        assertThatThrownBy(() -> bottariTemplateService.create(ssaid, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 물품이 존재합니다.");
    }

    @DisplayName("보따리 템플릿으로 보따리를 생성한다.")
    @Test
    void createBottari() {
        // given
        final Member templateOwner = new Member("owner_ssaid", "owner_name");
        entityManager.persist(templateOwner);

        final BottariTemplate bottariTemplate = new BottariTemplate("title", templateOwner);
        entityManager.persist(bottariTemplate);

        final BottariTemplateItem bottariTemplateItem1 = new BottariTemplateItem("item1", bottariTemplate);
        final BottariTemplateItem bottariTemplateItem2 = new BottariTemplateItem("item2", bottariTemplate);
        entityManager.persist(bottariTemplateItem1);
        entityManager.persist(bottariTemplateItem2);

        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        // when
        final Long actualBottariId = bottariTemplateService.createBottari(bottariTemplate.getId(), ssaid);

        // then
        assertAll(() -> {
            assertThat(actualBottariId).isNotNull();
            final Bottari actualBottari = entityManager.find(Bottari.class, actualBottariId);
            assertThat(actualBottari.getTitle()).isEqualTo("title");
            final List<BottariItem> actualBottariItems = entityManager.createQuery(
                            "select i from BottariItem i where i.bottari.id = :bottariId",
                            BottariItem.class)
                    .setParameter("bottariId", actualBottariId)
                    .getResultList();
            assertThat(actualBottariItems).extracting("name").containsExactly(
                    bottariTemplateItem1.getName(),
                    bottariTemplateItem2.getName()
            );
        });
    }

    @DisplayName("보따리 생성 시 템플릿이 존재하지 않는다면, 예외를 던진다.")
    @Test
    void createBottari_Exception_NotExistsTemplate() {
        // given
        final Member member = new Member("ssaid", "name");
        entityManager.persist(member);
        final Long notExistsTemplateId = 1L;

        // when & then
        assertThatThrownBy(() -> bottariTemplateService.createBottari(notExistsTemplateId, "ssaid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 템플릿을 찾을 수 없습니다.");
    }

    @DisplayName("보따리 생성 시 사용자가 존재하지 않는다면, 예외를 던진다.")
    @Test
    void createBottari_Exception_NotExistsMember() {
        // given
        final Member templateOwner = new Member("owner_ssaid", "owner_name");
        entityManager.persist(templateOwner);
        final BottariTemplate bottariTemplate = new BottariTemplate("title", templateOwner);
        entityManager.persist(bottariTemplate);

        final String notExistsSsaid = "invalid_ssaid";

        // when & then
        assertThatThrownBy(() -> bottariTemplateService.createBottari(bottariTemplate.getId(), notExistsSsaid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 ssaid로 가입된 사용자가 없습니다.");
    }
}
