package com.bottari.bottaritemplate.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.bottari.bottaritemplate.dto.ReadHashtagWithUsageCountResponse;
import com.bottari.bottaritemplate.repository.dto.HashtagPopularityProjection;
import com.bottari.error.BusinessException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {

    @InjectMocks
    private HashtagService hashtagService;

    @Mock
    private TrendingHashtagProvider trendingHashtagProvider;

    @Nested
    @DisplayName("인기 해시태그 조회")
    class GetTopHashtagsByUsageCountTest {

        @DisplayName("해시태그 Provider로부터 받은 데이터를 응답 DTO로 변환하여 반환한다.")
        @Test
        void getTopHashtagsByUsageCount() {
            // given
            final int limit = 10;
            final HashtagPopularityProjection projection1 = mock(HashtagPopularityProjection.class);
            given(projection1.getHashtagId()).willReturn(1L);
            given(projection1.getHashtagName()).willReturn("여행");
            given(projection1.getUsageCount()).willReturn(100);

            final HashtagPopularityProjection projection2 = mock(HashtagPopularityProjection.class);
            given(projection2.getHashtagId()).willReturn(2L);
            given(projection2.getHashtagName()).willReturn("캠핑");
            given(projection2.getUsageCount()).willReturn(50);

            given(trendingHashtagProvider.getPopularHashtags(limit))
                    .willReturn(List.of(projection1, projection2));

            // when
            final List<ReadHashtagWithUsageCountResponse> responses = hashtagService.getPopularHashtags(limit);

            // then
            assertAll(
                    () -> assertThat(responses).hasSize(2),
                    () -> assertThat(responses.get(0).id()).isEqualTo(1L),
                    () -> assertThat(responses.get(0).name()).isEqualTo("여행"),
                    () -> assertThat(responses.get(0).usageCount()).isEqualTo(100L),
                    () -> assertThat(responses.get(1).id()).isEqualTo(2L),
                    () -> assertThat(responses.get(1).name()).isEqualTo("캠핑"),
                    () -> assertThat(responses.get(1).usageCount()).isEqualTo(50L)
            );
        }

        @DisplayName("limit이 0 이하일 경우, 예외를 던진다.")
        @ParameterizedTest
        @ValueSource(ints = {0, -1, -10})
        void getTopHashtagsByUsageCount_Exception_LimitTooLow(final int limit) {
            // when & then
            assertThatThrownBy(() -> hashtagService.getPopularHashtags(limit))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("인기 해시태그 조회 limit이 너무 적습니다. - 조회는 1개 이상 가능합니다.");
        }

        @DisplayName("limit이 100을 초과할 경우, 예외를 던진다.")
        @ParameterizedTest
        @ValueSource(ints = {101, 200, 1000})
        void getTopHashtagsByUsageCount_Exception_LimitTooHigh(final int limit) {
            // when & then
            assertThatThrownBy(() -> hashtagService.getPopularHashtags(limit))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("인기 해시태그 조회 limit이 너무 높습니다. - 조회는 100개 이하 가능합니다.");
        }
    }
}
