package com.bottari.bottari.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.error.BusinessException;
import com.bottari.fixture.BottariFixture;
import com.bottari.fixture.BottariItemFixture;
import com.bottari.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class BottariItemTest {

    @DisplayName("보따리 물품을 체크한다.")
    @Test
    void check() {
        // given
        final Member member = new Member("ssaid", "name");
        final Bottari bottari = new Bottari("title", member);
        final BottariItem bottariItem = new BottariItem("name", bottari);

        // when
        bottariItem.check();

        // then
        assertThat(bottariItem.isChecked()).isTrue();
    }

    @DisplayName("이미 체크 되어있는 물품을 체크하면, 예외를 던진다.")
    @Test
    void check_Exception_AlreadyCheck() {
        // given
        final Member member = new Member("ssaid", "name");
        final Bottari bottari = new Bottari("title", member);
        final BottariItem bottariItem = new BottariItem("name", bottari);
        bottariItem.check();

        // when & then
        assertThatThrownBy(bottariItem::check)
                .isInstanceOf(BusinessException.class)
                .hasMessage("해당 보따리 물품은 이미 체크되어 있습니다.");
    }

    @DisplayName("보따리 물품을 체크 해제한다.")
    @Test
    void uncheck() {
        // given
        final Member member = new Member("ssaid", "name");
        final Bottari bottari = new Bottari("title", member);
        final BottariItem bottariItem = new BottariItem("name", bottari);
        bottariItem.check();

        // when
        bottariItem.uncheck();

        // then
        assertThat(bottariItem.isChecked()).isFalse();
    }

    @DisplayName("이미 체크 해제되어있는 물품을 체크 해제하면, 예외를 던진다.")
    @Test
    void uncheck_Exception_AlreadyUncheck() {
        // given
        final Member member = new Member("ssaid", "name");
        final Bottari bottari = new Bottari("title", member);
        final BottariItem bottariItem = new BottariItem("name", bottari);

        // when & then
        assertThatThrownBy(bottariItem::uncheck)
                .isInstanceOf(BusinessException.class)
                .hasMessage("해당 보따리 물품은 이미 체크 해제되어 있습니다.");
    }

    @DisplayName("본인의 보따리 물품인지 확인한다.")
    @ParameterizedTest
    @CsvSource({
            "same_ssaid, true",
            "diff_ssaid, false"
    })
    void isOwner(
            final String ssaid,
            final boolean expected
    ) {
        // given
        final Member member = new Member("same_ssaid", "name");
        final Bottari bottari = BottariFixture.BOTTARI.get(member);
        final BottariItem bottariItem = BottariItemFixture.BOTTARI_ITEM_1.get(bottari);

        // when
        final boolean actual = bottariItem.isOwner(ssaid);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
