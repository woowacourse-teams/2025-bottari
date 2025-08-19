package com.bottari.vo;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.support.BadWordValidator;
import jakarta.persistence.Embeddable;

@Embeddable
public record ItemName(
        String name
) {

    private static final int MAX_NAME_LENGTH = 20;

    public ItemName {
        validateName(name);
    }

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new BusinessException(ErrorCode.ITEM_NAME_BLANK);
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new BusinessException(ErrorCode.ITEM_NAME_TOO_LONG, "최대 " + MAX_NAME_LENGTH + "자까지 입력 가능합니다.");
        }
        if (BadWordValidator.hasBadWord(name)) {
            throw new BusinessException(ErrorCode.ITEM_NAME_OFFENSIVE);
        }
    }
}
