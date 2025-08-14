package com.bottari.vo;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.support.BadWordValidator;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemName {

    private String name;

    public ItemName(final String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new BusinessException(ErrorCode.ITEM_NAME_BLANK);
        }
        if (name.length() > 20) {
            throw new BusinessException(ErrorCode.ITEM_NAME_TOO_LONG);
        }
        if (BadWordValidator.hasBadWord(name)) {
            throw new BusinessException(ErrorCode.ITEM_NAME_OFFENSIVE);
        }
    }
}
