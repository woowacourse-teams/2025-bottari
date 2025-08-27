package com.bottari.apiworkshop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.apiworkshop.dto.CursorRequest;
import com.bottari.apiworkshop.dto.ItemResponse;
import com.bottari.apiworkshop.dto.MostIncludedQuery;
import com.bottari.apiworkshop.dto.MostIncludedResponse;
import com.bottari.apiworkshop.service.OpenApiService;
import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.bottaritemplate.domain.BottariTemplateItem;
import com.bottari.config.JpaAuditingConfig;
import com.bottari.fixture.MemberFixture;
import com.bottari.member.domain.Member;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({
        OpenApiService.class,
        JpaAuditingConfig.class
})
class OpenApiServiceTest {

    @Autowired
    private OpenApiService openApiService;

    @Autowired
    private EntityManager entityManager;


    @Nested
    class MostIncludedTest {

        @DisplayName("가장 많이 포함된 아이템들을 조회한다.")
        @Test
        void mostIncluded() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final LocalDate start = LocalDate.of(2024, 1, 1);
            final LocalDate end = LocalDate.of(2024, 12, 31);

            // 템플릿 생성
            final BottariTemplate template1 = createTemplate(member, "여행용 체크리스트", start.plusDays(10).atStartOfDay());
            final BottariTemplate template2 = createTemplate(member, "출장용 체크리스트", start.plusDays(20).atStartOfDay());
            final BottariTemplate template3 = createTemplate(member, "캠핑 체크리스트", start.plusDays(30).atStartOfDay());

            final BottariTemplate outOfRangeTemplate = createTemplate(
                    member, "기간외 템플릿", start.minusDays(1).atStartOfDay());

            // 아이템 생성 (충전기 3회, 노트북 2회, 텐트 2회, 여권 1회)
            createItem(template1, "충전기");
            createItem(template1, "노트북");
            createItem(template1, "텐트");
            createItem(template1, "여권");
            createItem(template2, "충전기");
            createItem(template2, "노트북");
            createItem(template3, "충전기");
            createItem(template3, "텐트");
            createItem(outOfRangeTemplate, "제외될아이템");

            final String query = "체크리스트";
            final Long limit = 10L;
            final String lastName = null;
            final Long lastRank = 0L;
            final Long lastCount = null;

            // when
            final MostIncludedQuery mostIncludedQuery = new MostIncludedQuery(query, start, end);
            final CursorRequest cursorRequest = new CursorRequest(limit, lastName, lastRank, lastCount);
            final MostIncludedResponse response = openApiService.mostIncluded(
                    mostIncludedQuery, cursorRequest
            );

            // then
            final List<ItemResponse> expectedItems = List.of(
                    new ItemResponse(1L, "충전기", 3L),
                    new ItemResponse(2L, "노트북", 2L),
                    new ItemResponse(2L, "텐트", 2L),
                    new ItemResponse(3L, "여권", 1L)
            );

            assertAll(
                    () -> assertThat(response.query().query()).isEqualTo(query),
                    () -> assertThat(response.query().start()).isEqualTo(start),
                    () -> assertThat(response.query().end()).isEqualTo(end),
                    () -> assertThat(response.items()).isEqualTo(expectedItems)
            );
        }

        @DisplayName("빈 검색어로 조회하면 모든 템플릿을 대상으로 한다.")
        @Test
        void mostIncluded_EmptyQuery() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final LocalDate start = LocalDate.of(2024, 1, 1);
            final LocalDate end = LocalDate.of(2024, 12, 31);

            final BottariTemplate template1 = createTemplate(member, "여행용 체크리스트", start.plusDays(10).atStartOfDay());
            final BottariTemplate template2 = createTemplate(member, "다른템플릿", start.plusDays(20).atStartOfDay());

            createItem(template1, "아이템1");
            createItem(template2, "아이템2");

            final String emptyQuery = "";
            final Long limit = 10L;

            // when
            final MostIncludedQuery mostIncludedQuery = new MostIncludedQuery(emptyQuery, start, end);
            final CursorRequest cursorRequest = new CursorRequest(limit, null, 0L, null);
            final MostIncludedResponse response = openApiService.mostIncluded(
                    mostIncludedQuery, cursorRequest
            );

            // then
            assertThat(response.items()).hasSize(2);
        }

        @DisplayName("기간 외의 템플릿은 집계에서 제외된다.")
        @Test
        void mostIncluded_ExcludeOutOfRange() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final LocalDate start = LocalDate.of(2024, 6, 1);
            final LocalDate end = LocalDate.of(2024, 6, 30);

            final BottariTemplate inRangeTemplate = createTemplate(member, "체크리스트", start.plusDays(1).atStartOfDay());
            final BottariTemplate beforeRangeTemplate = createTemplate(member, "체크리스트",
                    start.minusDays(1).atStartOfDay());
            final BottariTemplate afterRangeTemplate = createTemplate(member, "체크리스트", end.plusDays(1).atStartOfDay());

            createItem(inRangeTemplate, "포함될아이템");
            createItem(beforeRangeTemplate, "제외될아이템1");
            createItem(afterRangeTemplate, "제외될아이템2");

            final String query = "체크리스트";
            final Long limit = 10L;

            // when
            final MostIncludedQuery mostIncludedQuery = new MostIncludedQuery(query, start, end);
            final CursorRequest cursorRequest = new CursorRequest(limit, null, 0L, null);
            final MostIncludedResponse response = openApiService.mostIncluded(
                    mostIncludedQuery, cursorRequest
            );

            // then
            List<ItemResponse> expectedItems = List.of(
                    new ItemResponse(1L, "포함될아이템", 1L)
            );

            assertThat(response.items()).isEqualTo(expectedItems);
        }

        @DisplayName("동일한 포함횟수의 아이템들은 같은 순위를 가진다.")
        @Test
        void mostIncluded_SameRankForSameCount() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final LocalDate start = LocalDate.of(2024, 1, 1);
            final LocalDate end = LocalDate.of(2024, 12, 31);

            final BottariTemplate template1 = createTemplate(member, "체크리스트1", start.plusDays(1).atStartOfDay());
            final BottariTemplate template2 = createTemplate(member, "체크리스트2", start.plusDays(2).atStartOfDay());

            createItem(template1, "아이템A");
            createItem(template1, "아이템B");
            createItem(template2, "아이템C");
            createItem(template2, "아이템D");

            final String query = "체크리스트";
            final Long limit = 10L;

            // when
            final MostIncludedQuery mostIncludedQuery = new MostIncludedQuery(query, start, end);
            final CursorRequest cursorRequest = new CursorRequest(limit, null, 0L, null);
            final MostIncludedResponse response = openApiService.mostIncluded(
                    mostIncludedQuery, cursorRequest
            );

            // then
            List<ItemResponse> expectedItems = List.of(
                    new ItemResponse(1L, "아이템A", 1L),
                    new ItemResponse(1L, "아이템B", 1L),
                    new ItemResponse(1L, "아이템C", 1L),
                    new ItemResponse(1L, "아이템D", 1L)
            );

            assertThat(response.items()).isEqualTo(expectedItems);
        }

        @DisplayName("limit만큼만 아이템을 반환한다.")
        @Test
        void mostIncluded_WithLimit() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final LocalDate start = LocalDate.of(2024, 1, 1);
            final LocalDate end = LocalDate.of(2024, 12, 31);

            final BottariTemplate template = createTemplate(member, "체크리스트", start.plusDays(1).atStartOfDay());

            for (int i = 1; i <= 10; i++) {
                createItem(template, "아이템" + i);
            }

            final String query = "체크리스트";
            final Long limit = 5L;

            // when
            final MostIncludedQuery mostIncludedQuery = new MostIncludedQuery(query, start, end);
            final CursorRequest cursorRequest = new CursorRequest(limit, null, 0L, null);
            final MostIncludedResponse response = openApiService.mostIncluded(
                    mostIncludedQuery, cursorRequest
            );

            // then
            assertThat(response.items()).hasSize(5);
        }

        @DisplayName("start / end가 null이라면, 모든 범위의 아이템을 반환한다.")
        @Test
        void mostIncluded_StartEndNull() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final LocalDate date = LocalDate.of(2024, 1, 1);

            final BottariTemplate template = createTemplate(member, "체크리스트", date.plusDays(1).atStartOfDay());

            for (int i = 1; i <= 10; i++) {
                createItem(template, "아이템" + i);
            }

            final String query = "체크리스트";
            final Long limit = 10L;

            // when
            final MostIncludedQuery mostIncludedQuery = new MostIncludedQuery(query, null, null);
            final CursorRequest cursorRequest = new CursorRequest(limit, null, 0L, null);
            final MostIncludedResponse response = openApiService.mostIncluded(
                    mostIncludedQuery, cursorRequest
            );

            // then
            assertThat(response.items()).hasSize(10);
        }
    }

    private BottariTemplate createTemplate(
            final Member member,
            final String title,
            final LocalDateTime createdAt
    ) {
        final BottariTemplate template = new BottariTemplate(title, member);
        entityManager.persist(template);
        entityManager.flush();

        // 리플렉션을 사용하여 createdAt 설정
        try {
            final var field = BottariTemplate.class.getDeclaredField("createdAt");
            field.setAccessible(true);
            field.set(template, createdAt);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set createdAt", e);
        }

        entityManager.merge(template);
        return template;
    }

    private void createItem(
            final BottariTemplate template,
            final String name
    ) {
        final BottariTemplateItem item = new BottariTemplateItem(name, template);
        entityManager.persist(item);
    }
}
