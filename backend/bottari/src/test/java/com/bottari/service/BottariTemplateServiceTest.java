package com.bottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.config.JpaAuditingConfig;
import com.bottari.domain.Bottari;
import com.bottari.domain.BottariItem;
import com.bottari.domain.BottariTemplate;
import com.bottari.domain.BottariTemplateHistory;
import com.bottari.domain.BottariTemplateItem;
import com.bottari.domain.Member;
import com.bottari.dto.CreateBottariTemplateRequest;
import com.bottari.dto.ReadBottariTemplateResponse;
import com.bottari.dto.ReadNextBottariTemplateRequest;
import com.bottari.dto.ReadNextBottariTemplateResponse;
import com.bottari.error.BusinessException;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({BottariTemplateService.class, JpaAuditingConfig.class})
class BottariTemplateServiceTest {

    @Autowired
    private BottariTemplateService bottariTemplateService;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class GetByIdTest {

        @DisplayName("보따리 템플릿을 상세 조회한다.")
        @Test
        void getById() {
            // given
            final Member member = new Member("ssaid", "name");
            entityManager.persist(member);

            final BottariTemplate template1 = new BottariTemplate("title_1", member);
            final BottariTemplateItem item1 = new BottariTemplateItem("item_1", template1);
            final BottariTemplateItem item2 = new BottariTemplateItem("item_2", template1);
            entityManager.persist(template1);
            entityManager.persist(item1);
            entityManager.persist(item2);

            final BottariTemplate template2 = new BottariTemplate("title_2", member);
            final BottariTemplateItem item3 = new BottariTemplateItem("item_3", template2);
            entityManager.persist(template2);
            entityManager.persist(item3);

            // when
            final ReadBottariTemplateResponse actual = bottariTemplateService.getById(template1.getId());

            // then
            assertAll(
                    () -> assertThat(actual.title()).isEqualTo("title_1"),
                    () -> assertThat(actual.items()).hasSize(2),
                    () -> assertThat(actual.items().get(0).name()).isEqualTo("item_1"),
                    () -> assertThat(actual.items().get(1).name()).isEqualTo("item_2")
            );
        }

        @DisplayName("존재하지 않는 보따리 템플릿을 상세 조회할 경우, 예외를 던진다.")
        @Test
        void getById_Exception_NotExistsBottariTemplate() {
            // given
            final Long notExistsBottariTemplateId = 1L;

            // when & then
            assertThatThrownBy(() -> bottariTemplateService.getById(notExistsBottariTemplateId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("보따리 템플릿을 찾을 수 없습니다.");
        }
    }

    @Nested
    class GetBySsaidTest {

        @DisplayName("내 보따리 템플릿 조회 시, 내 보따리 템플릿을 최신순으로 조회한다.")
        @Test
        void getBySsaid() {
            // given
            final String memberASsaid = "memberA_ssaid";
            final Member memberA = new Member(memberASsaid, "memberA");
            entityManager.persist(memberA);
            final Member memberB = new Member("memberB_ssaid", "memberB");
            entityManager.persist(memberB);

            final BottariTemplate memberATemplate1 = new BottariTemplate("A_template1", memberA);
            final BottariTemplateItem item1 = new BottariTemplateItem("item_1", memberATemplate1);
            final BottariTemplateItem item2 = new BottariTemplateItem("item_2", memberATemplate1);
            entityManager.persist(memberATemplate1);
            entityManager.persist(item1);
            entityManager.persist(item2);

            final BottariTemplate memberATemplate2 = new BottariTemplate("A_template2", memberA);
            final BottariTemplateItem item3 = new BottariTemplateItem("item_3", memberATemplate2);
            entityManager.persist(memberATemplate2);
            entityManager.persist(item3);

            final BottariTemplate memberBTemplate = new BottariTemplate("B_template", memberB);
            final BottariTemplateItem item4 = new BottariTemplateItem("item_4", memberBTemplate);
            entityManager.persist(memberBTemplate);
            entityManager.persist(item4);

            // when
            final List<ReadBottariTemplateResponse> actual = bottariTemplateService.getBySsaid(memberASsaid);

            // then
            assertAll(() -> {
                        assertThat(actual).hasSize(2);
                        assertThat(actual.get(0).title()).isEqualTo("A_template2");
                        assertThat(actual.get(0).items()).hasSize(1);
                        assertThat(actual.get(0).takenCount()).isEqualTo(0);
                        assertThat(actual.get(0).items().getFirst().name()).isEqualTo("item_3");
                        assertThat(actual.get(1).title()).isEqualTo("A_template1");
                        assertThat(actual.get(1).items()).hasSize(2);
                        assertThat(actual.get(1).items().get(0).name()).isEqualTo("item_1");
                        assertThat(actual.get(1).items().get(1).name()).isEqualTo("item_2");
                        assertThat(actual.get(1).takenCount()).isEqualTo(0);
                    }
            );
        }

        @DisplayName("내 보따리 템플릿 조회 시, 존재하지 않은 사용자면 예외를 던진다.")
        @Test
        void getBySsaid_Exception_NotExistsMember() {
            // given
            final String invalidSsaid = "invalid_ssaid";

            // when & then
            assertThatThrownBy(() -> bottariTemplateService.getBySsaid(invalidSsaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("사용자를 찾을 수 없습니다. - 등록되지 않은 ssaid입니다.");
        }
    }

    @Nested
    class GetAllTest {

        @DisplayName("검색어가 없을 시, 모든 보따리 템플릿을 최신순으로 조회한다.")
        @Test
        void getAll() {
            // given
            final String empty_query = "";

            final Member member = new Member("ssaid", "name");
            entityManager.persist(member);

            final BottariTemplate template1 = new BottariTemplate("older_template", member);
            final BottariTemplateItem item1 = new BottariTemplateItem("item_1", template1);
            final BottariTemplateItem item2 = new BottariTemplateItem("item_2", template1);
            entityManager.persist(template1);
            entityManager.persist(item1);
            entityManager.persist(item2);

            final BottariTemplate template2 = new BottariTemplate("newer_template", member);
            final BottariTemplateItem item3 = new BottariTemplateItem("item_3", template2);
            entityManager.persist(template2);
            entityManager.persist(item3);

            // when
            final List<ReadBottariTemplateResponse> actual = bottariTemplateService.getAll(empty_query);

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(2),
                    () -> assertThat(actual.get(0).title()).isEqualTo("newer_template"),
                    () -> assertThat(actual.get(0).items()).hasSize(1),
                    () -> assertThat(actual.get(0).items().getFirst().name()).isEqualTo("item_3"),
                    () -> assertThat(actual.get(0).takenCount()).isEqualTo(0),
                    () -> assertThat(actual.get(1).title()).isEqualTo("older_template"),
                    () -> assertThat(actual.get(1).items()).hasSize(2),
                    () -> assertThat(actual.get(1).items().get(0).name()).isEqualTo("item_1"),
                    () -> assertThat(actual.get(1).items().get(1).name()).isEqualTo("item_2"),
                    () -> assertThat(actual.get(1).takenCount()).isEqualTo(0)
            );
        }

        @DisplayName("검색어가 존재할 시, 타이틀에 검색어가 포함된 템플릿을 모두 조회한다.")
        @Test
        void getAll_WithQuery() {
            // given
            final String query = "title";

            final Member member = new Member("ssaid", "name");
            entityManager.persist(member);

            final BottariTemplate template1 = new BottariTemplate("title_1", member);
            final BottariTemplateItem item1 = new BottariTemplateItem("item_1", template1);
            final BottariTemplateItem item2 = new BottariTemplateItem("item_2", template1);
            entityManager.persist(template1);
            entityManager.persist(item1);
            entityManager.persist(item2);

            final BottariTemplate template2 = new BottariTemplate("title_2", member);
            final BottariTemplateItem item3 = new BottariTemplateItem("item_3", template2);
            entityManager.persist(template2);
            entityManager.persist(item3);

            final BottariTemplate template3 = new BottariTemplate("subject", member);
            final BottariTemplateItem item4 = new BottariTemplateItem("item_4", template3);
            entityManager.persist(template3);
            entityManager.persist(item4);

            // when
            final List<ReadBottariTemplateResponse> actual = bottariTemplateService.getAll(query);

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(2),
                    () -> assertThat(actual.get(1).title()).isEqualTo("title_1"),
                    () -> assertThat(actual.get(1).items()).hasSize(2),
                    () -> assertThat(actual.get(1).items().get(0).name()).isEqualTo("item_1"),
                    () -> assertThat(actual.get(1).items().get(1).name()).isEqualTo("item_2"),
                    () -> assertThat(actual.getFirst().title()).isEqualTo("title_2"),
                    () -> assertThat(actual.getFirst().items()).hasSize(1),
                    () -> assertThat(actual.getFirst().items().getFirst().name()).isEqualTo("item_3")
            );
        }
    }

    @Nested
    class GetNextAllTest {

        @DisplayName("createdAt 기준으로 다음 페이지 템플릿 목록을 조회한다.")
        @Test
        void getNextAll_ByCreatedAt() {
            // given
            final Member member = new Member("ssaid", "name");
            entityManager.persist(member);

            final BottariTemplate template1 = new BottariTemplate("template1", member);
            final BottariTemplate template2 = new BottariTemplate("template2", member);
            final BottariTemplate template3 = new BottariTemplate("template3", member);
            entityManager.persist(template1);
            entityManager.persist(template2);
            entityManager.persist(template3);

            final BottariTemplateItem item1 = new BottariTemplateItem("item1", template1);
            final BottariTemplateItem item2 = new BottariTemplateItem("item2", template2);
            final BottariTemplateItem item3 = new BottariTemplateItem("item3", template3);
            entityManager.persist(item1);
            entityManager.persist(item2);
            entityManager.persist(item3);

            final ReadNextBottariTemplateRequest request = new ReadNextBottariTemplateRequest(
                    "",
                    null,
                    null,
                    0,
                    2,
                    "createdAt"
            );

            // when
            final ReadNextBottariTemplateResponse actual = bottariTemplateService.getNextAll(request);

            // then
            assertAll(
                    () -> assertThat(actual.contents()).hasSize(2),
                    () -> assertThat(actual.currentPage()).isEqualTo(0),
                    () -> assertThat(actual.size()).isEqualTo(2),
                    () -> assertThat(actual.hasNext()).isTrue(),
                    () -> assertThat(actual.first()).isTrue(),
                    () -> assertThat(actual.last()).isFalse(),
                    () -> assertThat(actual.lastId()).isNotNull(),
                    () -> assertThat(actual.lastInfo()).isNotNull()
            );
        }

        @DisplayName("takenCount 기준으로 다음 페이지 템플릿 목록을 조회한다.")
        @Test
        void getNextAll_ByTakenCount() {
            // given
            final Member member = new Member("ssaid", "name");
            entityManager.persist(member);

            final BottariTemplate template1 = new BottariTemplate("template1", member);
            final BottariTemplate template2 = new BottariTemplate("template2", member);
            entityManager.persist(template1);
            entityManager.persist(template2);

            final BottariTemplateItem item1 = new BottariTemplateItem("item1", template1);
            final BottariTemplateItem item2 = new BottariTemplateItem("item2", template2);
            entityManager.persist(item1);
            entityManager.persist(item2);

            final ReadNextBottariTemplateRequest request = new ReadNextBottariTemplateRequest(
                    "",
                    null,
                    "999999",
                    0,
                    1,
                    "takenCount"
            );

            // when
            final ReadNextBottariTemplateResponse actual = bottariTemplateService.getNextAll(request);

            // then
            assertAll(
                    () -> assertThat(actual.contents()).hasSize(1),
                    () -> assertThat(actual.currentPage()).isEqualTo(0),
                    () -> assertThat(actual.size()).isEqualTo(1),
                    () -> assertThat(actual.hasNext()).isTrue(),
                    () -> assertThat(actual.first()).isTrue(),
                    () -> assertThat(actual.last()).isFalse(),
                    () -> assertThat(actual.lastId()).isNotNull(),
                    () -> assertThat(actual.lastInfo()).isNotNull()
            );
        }

        @DisplayName("검색어로 필터링하여 다음 페이지 템플릿 목록을 조회한다.")
        @Test
        void getNextAll_WithQuery() {
            // given
            final Member member = new Member("ssaid", "name");
            entityManager.persist(member);

            final BottariTemplate template1 = new BottariTemplate("여행용 체크리스트", member);
            final BottariTemplate template2 = new BottariTemplate("캠핑 준비물", member);
            final BottariTemplate template3 = new BottariTemplate("출장 체크리스트", member);
            entityManager.persist(template1);
            entityManager.persist(template2);
            entityManager.persist(template3);

            final BottariTemplateItem item1 = new BottariTemplateItem("여권", template1);
            final BottariTemplateItem item2 = new BottariTemplateItem("텐트", template2);
            final BottariTemplateItem item3 = new BottariTemplateItem("노트북", template3);
            entityManager.persist(item1);
            entityManager.persist(item2);
            entityManager.persist(item3);

            final ReadNextBottariTemplateRequest request = new ReadNextBottariTemplateRequest(
                    "체크리스트",
                    null,
                    null,
                    0,
                    2,
                    "createdAt"
            );

            // when
            final ReadNextBottariTemplateResponse actual = bottariTemplateService.getNextAll(request);

            // then
            assertAll(
                    () -> assertThat(actual.contents()).hasSize(2),
                    () -> assertThat(actual.contents().get(0).title()).isEqualTo("출장 체크리스트"),
                    () -> assertThat(actual.contents().get(1).title()).isEqualTo("여행용 체크리스트"),
                    () -> assertThat(actual.hasNext()).isFalse(),
                    () -> assertThat(actual.last()).isTrue()
            );
        }

        @DisplayName("존재하지 않는 정렬 기준으로 조회 시 예외를 던진다.")
        @Test
        void getNextAll_Exception_InvalidSortProperty() {
            // given
            final ReadNextBottariTemplateRequest request = new ReadNextBottariTemplateRequest(
                    "",
                    null,
                    null,
                    0,
                    10,
                    "invalidProperty"
            );

            // when & then
            assertThatThrownBy(() -> bottariTemplateService.getNextAll(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 정렬 기준입니다.");
        }
    }

    @Nested
    class CreateTest {

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
                        SELECT i
                        FROM BottariTemplateItem i
                        WHERE i.bottariTemplate.id =: bottariTemplateId
                        """, BottariTemplateItem.class)
                .setParameter("bottariTemplateId", actual)
                .getResultList();

            assertAll(
                    () -> assertThat(actual).isNotNull(),
                    () -> assertThat(actualItems).extracting("name")
                            .containsExactlyInAnyOrderElementsOf(bottariTemplateItems)
            );
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
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("사용자를 찾을 수 없습니다. - 등록되지 않은 ssaid입니다.");
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
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("요청에 중복된 보따리 템플릿 물품이 있습니다.");
        }
    }

    @Nested
    class CreateBottariTest {

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
            final Bottari actualBottari = entityManager.find(Bottari.class, actualBottariId);
            final List<BottariItem> actualBottariItems = entityManager.createQuery(
                            "SELECT i FROM BottariItem i WHERE i.bottari.id = :bottariId",
                            BottariItem.class
                    )
                    .setParameter("bottariId", actualBottariId)
                    .getResultList();

            // then
            assertAll(
                    () -> assertThat(actualBottariId).isNotNull(),
                    () -> assertThat(actualBottari.getTitle()).isEqualTo("title"),
                    () -> assertThat(actualBottariItems).extracting("name")
                            .containsExactly(bottariTemplateItem1.getName(), bottariTemplateItem2.getName())
            );
        }


        @DisplayName("보따리를 처음 생성하는 경우, 보따리 히스토리에 기록되고 템플릿 가져간 횟수를 1 증가시킨다.")
        @Test
        void createBottari_WhenCreateBottariFirst() {
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
            bottariTemplateService.createBottari(bottariTemplate.getId(), ssaid);

            // then
            final BottariTemplateHistory acutalBottariTemplateHistory = entityManager.createQuery("""
                             SELECT bh
                             FROM BottariTemplateHistory bh
                             WHERE bh.id.memberId = :memberId
                             AND bh.id.bottariTemplateId = :bottariTemplateId
            """, BottariTemplateHistory.class)
                    .setParameter("memberId", member.getId())
                    .setParameter("bottariTemplateId", bottariTemplate.getId())
                    .getSingleResult();
            final BottariTemplate actualBottariTemplate = entityManager.find(
                    BottariTemplate.class, bottariTemplate.getId());
            assertAll(
                    () -> assertThat(actualBottariTemplate.getTakenCount()).isEqualTo(1),
                    () -> assertThat(acutalBottariTemplateHistory).isNotNull()
            );
        }

        @DisplayName("보따리를 이전에 생성한 경우, 보따리 생성시 템플릿 히스토리에 남지 않는다.")
        @Test
        void createBottari_WhenCreateBottariSecond() {
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

            bottariTemplateService.createBottari(bottariTemplate.getId(), ssaid);
            // when
            bottariTemplateService.createBottari(bottariTemplate.getId(), ssaid);

            // then
            final Long actualHistoryCount = entityManager.createQuery("""
                             SELECT COUNT(bh)
                             FROM BottariTemplateHistory bh
                             WHERE bh.id.memberId = :memberId
                             AND bh.id.bottariTemplateId = :bottariTemplateId
            """, Long.class)
                    .setParameter("memberId", member.getId())
                    .setParameter("bottariTemplateId", bottariTemplate.getId())
                    .getSingleResult();
            final BottariTemplate actualBottariTemplate = entityManager.find(BottariTemplate.class, bottariTemplate.getId());
            assertAll(
                    () -> assertThat(actualBottariTemplate.getTakenCount()).isEqualTo(1),
                    () -> assertThat(actualHistoryCount).isEqualTo(1L)
            );
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
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("보따리 템플릿을 찾을 수 없습니다.");
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
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("사용자를 찾을 수 없습니다. - 등록되지 않은 ssaid입니다.");
        }
    }

    @Nested
    class DeleteByIdTest {

        @DisplayName("보따리 템플릿 아이디로 해당 보따리 템플릿을 삭제한다.")
        @Test
        void deleteById() {
            // given
            final String ssaid = "ssaid";
            final Member member = new Member("ssaid", "name");
            entityManager.persist(member);

            final BottariTemplate bottariTemplate1 = new BottariTemplate("title1", member);
            final BottariTemplate bottariTemplate2 = new BottariTemplate("title2", member);
            entityManager.persist(bottariTemplate1);
            entityManager.persist(bottariTemplate2);

            final BottariTemplateItem bottariTemplate1Item1 = new BottariTemplateItem("name1", bottariTemplate1);
            final BottariTemplateItem bottariTemplate1Item2 = new BottariTemplateItem("name2", bottariTemplate1);
            final BottariTemplateItem bottariTemplate2Item1 = new BottariTemplateItem("name3", bottariTemplate2);
            entityManager.persist(bottariTemplate1Item1);
            entityManager.persist(bottariTemplate1Item2);
            entityManager.persist(bottariTemplate2Item1);

            // when
            bottariTemplateService.deleteById(bottariTemplate1.getId(), ssaid);

            // then
            final List<BottariTemplate> remainingTemplates = entityManager
                    .createQuery("SELECT bt FROM BottariTemplate bt", BottariTemplate.class)
                    .getResultList();
            final List<BottariTemplateItem> remainingItems = entityManager
                    .createQuery("SELECT bti FROM BottariTemplateItem bti", BottariTemplateItem.class)
                    .getResultList();

            assertAll(
                    () -> assertThat(remainingTemplates)
                            .hasSize(1)
                            .extracting(BottariTemplate::getId)
                            .containsExactly(bottariTemplate2.getId()),

                    () -> assertThat(remainingItems)
                            .hasSize(1)
                            .extracting(BottariTemplateItem::getName)
                            .containsExactly("name3")
            );
        }

        @DisplayName("삭제 시, 해당 보따리 템플릿이 없는 경우 예외가 발생한다.")
        @Test
        void deleteById_Exception_NotFoundBottariTemplate() {
            // given
            final Long invalidId = 1L;

            // when & then
            assertThatThrownBy(() -> bottariTemplateService.deleteById(invalidId, "ssaid"))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("보따리 템플릿을 찾을 수 없습니다.");
        }

        @DisplayName("삭제 시, 해당 보따리 템플릿 주인이 아닌 경우 예외가 발생한다.")
        @Test
        void deleteById_Exception_NotOwner() {
            // given
            final String anotherSsaid = "another_ssaid";
            final Member member = new Member("ssaid", "name");
            entityManager.persist(member);
            final BottariTemplate bottariTemplate = new BottariTemplate("title", member);
            entityManager.persist(bottariTemplate);

            // when & then
            assertThatThrownBy(() -> bottariTemplateService.deleteById(bottariTemplate.getId(), anotherSsaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 보따리 템플릿에 접근할 수 있는 권한이 없습니다. - 본인의 보따리 템플릿이 아닙니다.");
        }
    }
}
