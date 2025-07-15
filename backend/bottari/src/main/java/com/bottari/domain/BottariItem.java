package com.bottari.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BottariItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bottari_id")
    private Bottari bottari;

    private boolean isChecked;

    public BottariItem(
            final String name,
            final Bottari bottari
    ) {
        validateName(name);
        this.name = name;
        this.bottari = bottari;
        this.isChecked = false;
    }

    public void check() {
        if (isChecked) {
            throw new IllegalStateException("이미 체크 되었습니다.");
        }
        this.isChecked = true;
    }

    public void uncheck() {
        if (!isChecked) {
            throw new IllegalStateException("이미 체크 해제되었습니다.");
        }
        this.isChecked = false;
    }

    private void validateName(final String name) {
        if (name.isBlank() || name.length() > 20) {
            throw new IllegalArgumentException("보따리 물품명은 공백이거나 20자를 넘을 수 없습니다.");
        }
    }
}
