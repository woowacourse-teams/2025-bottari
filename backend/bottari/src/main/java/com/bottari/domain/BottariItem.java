package com.bottari.domain;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
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
            throw new BusinessException(ErrorCode.BOTTARI_ITEM_ALREADY_CHECKED);
        }
        this.isChecked = true;
    }

    public void uncheck() {
        if (!isChecked) {
            throw new BusinessException(ErrorCode.BOTTARI_ITEM_ALREADY_UNCHECKED);
        }
        this.isChecked = false;
    }

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new BusinessException(ErrorCode.BOTTARI_ITEM_NAME_IS_BLANK);
        }
        if (name.length() > 20) {
            throw new BusinessException(ErrorCode.BOTTARI_ITEM_NAME_TOO_LONG, "최대 20자까지 입력 가능합니다.");
        }
    }
}
