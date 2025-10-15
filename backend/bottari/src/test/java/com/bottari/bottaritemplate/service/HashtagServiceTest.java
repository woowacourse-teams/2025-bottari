package com.bottari.bottaritemplate.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.bottaritemplate.domain.BottariTemplateHashtag;
import com.bottari.bottaritemplate.domain.Hashtag;
import com.bottari.bottaritemplate.dto.ReadHashtagWithUsageCountResponse;
import com.bottari.config.JpaAuditingConfig;
import com.bottari.error.BusinessException;
import com.bottari.member.domain.Member;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({HashtagService.class, JpaAuditingConfig.class})
class HashtagServiceTest {

    @Autowired
    private HashtagService hashtagService;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class GetTopHashtagsByUsageCountTest {

        @DisplayName("인기 해시태그를 사용 횟수가 많은 순으로 조회한다.")
        @Test
        void getTopHashtagsByUsageCount() {
            // given
            final Member member = new Member("ssaid", "name");
            entityManager.persist(member);

            final Hashtag hashtag1 = new Hashtag("여행");
            final Hashtag hashtag2 = new Hashtag("캠핑");
            final Hashtag hashtag3 = new Hashtag("등산");
            entityManager.persist(hashtag1);
            entityManager.persist(hashtag2);
            entityManager.persist(hashtag3);

            final BottariTemplate template1 = new BottariTemplate("title1", "description1", member);
            final BottariTemplate template2 = new BottariTemplate("title2", "description2", member);
            final BottariTemplate template3 = new BottariTemplate("title3", "description3", member);
            final BottariTemplate template4 = new BottariTemplate("title4", "description4", member);
            entityManager.persist(template1);
            entityManager.persist(template2);
            entityManager.persist(template3);
            entityManager.persist(template4);

            // hashtag1: 3번 사용 (1위)
            entityManager.persist(new BottariTemplateHashtag(template1, hashtag1));
            entityManager.persist(new BottariTemplateHashtag(template2, hashtag1));
            entityManager.persist(new BottariTemplateHashtag(template3, hashtag1));

            // hashtag2: 2번 사용 (2위)
            entityManager.persist(new BottariTemplateHashtag(template1, hashtag2));
            entityManager.persist(new BottariTemplateHashtag(template2, hashtag2));

            // hashtag3: 1번 사용 (3위)
            entityManager.persist(new BottariTemplateHashtag(template4, hashtag3));

            entityManager.flush();
            entityManager.clear();

            // when
            final List<ReadHashtagWithUsageCountResponse> responses = hashtagService.getTopHashtagsByUsageCount(10);

            // then
            assertAll(() -> assertThat(responses).hasSize(3),
                    () -> assertThat(responses.getFirst().name()).isEqualTo("여행"),
                    () -> assertThat(responses.getFirst().usageCount()).isEqualTo(3),
                    () -> assertThat(responses.get(1).name()).isEqualTo("캠핑"),
                    () -> assertThat(responses.get(1).usageCount()).isEqualTo(2),
                    () -> assertThat(responses.get(2).name()).isEqualTo("등산"),
                    () -> assertThat(responses.get(2).usageCount()).isEqualTo(1));
        }

        @DisplayName("사용 횟수가 같을 경우, ID가 작은 순서로 정렬된다.")
        @Test
        void getTopHashtagsByUsageCount_SameUsageCount_OrderById() {
            // given
            final Member member = new Member("ssaid", "name");
            entityManager.persist(member);

            final Hashtag hashtag1 = new Hashtag("가나다");
            final Hashtag hashtag2 = new Hashtag("라마바");
            entityManager.persist(hashtag1);
            entityManager.persist(hashtag2);

            final BottariTemplate template1 = new BottariTemplate("title1", "description1", member);
            final BottariTemplate template2 = new BottariTemplate("title2", "description2", member);
            entityManager.persist(template1);
            entityManager.persist(template2);

            // 둘 다 1번씩 사용
            entityManager.persist(new BottariTemplateHashtag(template1, hashtag1));
            entityManager.persist(new BottariTemplateHashtag(template2, hashtag2));

            entityManager.flush();
            entityManager.clear();

            // when
            final List<ReadHashtagWithUsageCountResponse> responses = hashtagService.getTopHashtagsByUsageCount(10);

            // then
            assertAll(() -> assertThat(responses).hasSize(2),
                    () -> assertThat(responses.getFirst().id()).isLessThan(responses.get(1).id()));
        }

        @DisplayName("삭제된 보따리 템플릿 해시태그는 집계에서 제외된다.")
        @Test
        void getTopHashtagsByUsageCount_ExcludeDeleted() {
            // given
            final Member member = new Member("ssaid", "name");
            entityManager.persist(member);

            final Hashtag hashtag = new Hashtag("여행");
            entityManager.persist(hashtag);

            final BottariTemplate template1 = new BottariTemplate("title1", "description1", member);
            final BottariTemplate template2 = new BottariTemplate("title2", "description2", member);
            entityManager.persist(template1);
            entityManager.persist(template2);

            final BottariTemplateHashtag activeHashtag = new BottariTemplateHashtag(template1, hashtag);
            final BottariTemplateHashtag deletedHashtag = new BottariTemplateHashtag(template2, hashtag);
            entityManager.persist(activeHashtag);
            entityManager.persist(deletedHashtag);

            entityManager.flush();

            // 하나를 soft delete
            entityManager.createNativeQuery("UPDATE bottari_template_hashtag SET deleted_at = NOW() WHERE id = :id")
                    .setParameter("id", deletedHashtag.getId()).executeUpdate();

            entityManager.clear();

            // when
            final List<ReadHashtagWithUsageCountResponse> responses = hashtagService.getTopHashtagsByUsageCount(10);

            // then
            assertAll(() -> assertThat(responses).hasSize(1),
                    () -> assertThat(responses.get(0).usageCount()).isEqualTo(1));
        }

        @DisplayName("limit이 0 이하일 경우, 예외를 던진다.")
        @ParameterizedTest
        @ValueSource(ints = {0, -1, -10})
        void getTopHashtagsByUsageCount_Exception_LimitTooLow(final int limit) {
            // when & then
            assertThatThrownBy(() -> hashtagService.getTopHashtagsByUsageCount(limit)).isInstanceOf(
                    BusinessException.class).hasMessage("인기 해시태그 조회 limit이 너무 적습니다. - 조회는 1개 이상 가능합니다.");
        }

        @DisplayName("limit이 100을 초과할 경우, 예외를 던진다.")
        @ParameterizedTest
        @ValueSource(ints = {101, 200, 1000})
        void getTopHashtagsByUsageCount_Exception_LimitTooHigh(final int limit) {
            // when & then
            assertThatThrownBy(() -> hashtagService.getTopHashtagsByUsageCount(limit)).isInstanceOf(
                    BusinessException.class).hasMessage("인기 해시태그 조회 limit이 너무 높습니다. - 조회는 100개 이하 가능합니다.");
        }
    }
}
