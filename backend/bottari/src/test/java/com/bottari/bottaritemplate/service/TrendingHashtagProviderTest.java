package com.bottari.bottaritemplate.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.bottaritemplate.domain.BottariTemplateHashtag;
import com.bottari.bottaritemplate.domain.Hashtag;
import com.bottari.bottaritemplate.repository.dto.HashtagPopularityProjection;
import com.bottari.config.JpaAuditingConfig;
import com.bottari.member.domain.Member;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TrendingHashtagProvider.class, JpaAuditingConfig.class})
class TrendingHashtagProviderTest {

    @Autowired
    private TrendingHashtagProvider trendingHashtagProvider;

    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("최근 인기 해시태그 조회")
    class GetPopularHashtags {

        @DisplayName("최근 7일간 가장 많이 사용된 해시태그를 순서대로 조회한다.")
        @Test
        void getPopularHashtags() {
            // given
            final Member member = new Member("ssaid", "name");
            entityManager.persist(member);

            final Hashtag hashtag1 = new Hashtag("최신인기"); // 최근 2번
            final Hashtag hashtag2 = new Hashtag("인기"); // 최근 1번
            final Hashtag hashtag3 = new Hashtag("비인기"); // 최근 0번
            entityManager.persist(hashtag1);
            entityManager.persist(hashtag2);
            entityManager.persist(hashtag3);

            final BottariTemplate template = new BottariTemplate("title", "desc", member);
            entityManager.persist(template);

            // hashtag1: 최근 2번
            entityManager.persist(new BottariTemplateHashtag(template, hashtag1));
            entityManager.persist(new BottariTemplateHashtag(template, hashtag1));

            // hashtag2: 최근 1번
            entityManager.persist(new BottariTemplateHashtag(template, hashtag2));

            // hashtag3: 8일 전 3번 (집계되지 않아야 함)
            final BottariTemplateHashtag oldHashtag1 = new BottariTemplateHashtag(template, hashtag3);
            final BottariTemplateHashtag oldHashtag2 = new BottariTemplateHashtag(template, hashtag3);
            final BottariTemplateHashtag oldHashtag3 = new BottariTemplateHashtag(template, hashtag3);
            entityManager.persist(oldHashtag1);
            entityManager.persist(oldHashtag2);
            entityManager.persist(oldHashtag3);
            entityManager.flush();
            entityManager.createNativeQuery("UPDATE bottari_template_hashtag SET created_at = :date WHERE id IN (:id1, :id2, :id3)")
                    .setParameter("date", LocalDateTime.now().minusDays(8))
                    .setParameter("id1", oldHashtag1.getId())
                    .setParameter("id2", oldHashtag2.getId())
                    .setParameter("id3", oldHashtag3.getId())
                    .executeUpdate();

            entityManager.clear();

            // when
            final List<HashtagPopularityProjection> popularHashtags = trendingHashtagProvider.getPopularHashtags(5);

            // then
            assertAll(
                    () -> assertThat(popularHashtags).hasSize(2),
                    () -> assertThat(popularHashtags.get(0).getHashtagName()).isEqualTo("최신인기"),
                    () -> assertThat(popularHashtags.get(0).getUsageCount()).isEqualTo(2),
                    () -> assertThat(popularHashtags.get(1).getHashtagName()).isEqualTo("인기"),
                    () -> assertThat(popularHashtags.get(1).getUsageCount()).isEqualTo(1)
            );
        }

        @DisplayName("사용 횟수가 같을 경우, ID가 큰 순서로 정렬된다.")
        @Test
        void getPopularHashtags_SameUsageCount_OrderById() {
            // given
            final Member member = new Member("ssaid", "name");
            entityManager.persist(member);

            final Hashtag hashtag1 = new Hashtag("가나다");
            final Hashtag hashtag2 = new Hashtag("라마바");
            entityManager.persist(hashtag1);
            entityManager.persist(hashtag2);

            final BottariTemplate template = new BottariTemplate("title", "desc", member);
            entityManager.persist(template);

            // 둘 다 최근 1번씩 사용
            entityManager.persist(new BottariTemplateHashtag(template, hashtag1));
            entityManager.persist(new BottariTemplateHashtag(template, hashtag2));

            entityManager.flush();
            entityManager.clear();

            // when
            final List<HashtagPopularityProjection> popularHashtags = trendingHashtagProvider.getPopularHashtags(5);

            // then
            assertAll(
                    () -> assertThat(popularHashtags).hasSize(2),
                    () -> assertThat(popularHashtags.get(0).getHashtagId())
                            .isGreaterThan(popularHashtags.get(1).getHashtagId())
            );
        }
    }
}
