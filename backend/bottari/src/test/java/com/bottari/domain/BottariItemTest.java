package com.bottari.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BottariItemTest {

    @DisplayName("보따리 물품명이 공백이거나 20자 초과인 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "스무 글자가 넘는 보따리 물품명입니다."})
    void validateName(final String name) {
        // given
        final Member member = new Member("ssaid", "name");
        final Bottari bottari = new Bottari("title", member);

        // when & then
        assertThatThrownBy(() -> new BottariItem(name, bottari))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("보따리 물품명은 공백이거나 20자를 넘을 수 없습니다.");
    }

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
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 체크 되었습니다.");
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
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 체크 해제되었습니다.");
    }
}
