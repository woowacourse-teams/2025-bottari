package com.bottari.vo;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.support.BadWordValidator;
import jakarta.persistence.Embeddable;

@Embeddable
public record BottariTitle(
        String title
) {

    private static final int MAX_TITLE_LENGTH = 15;

    public BottariTitle {
        validateTitle(title);
    }

    private void validateTitle(final String title) {
        if (title.isBlank()) {
            throw new BusinessException(ErrorCode.BOTTARI_TITLE_BLANK);
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new BusinessException(ErrorCode.BOTTARI_TITLE_TOO_LONG, "최대 " + MAX_TITLE_LENGTH + "자까지 입력 가능합니다.");
        }
        if (BadWordValidator.hasBadWord(title)) {
            throw new BusinessException(ErrorCode.BOTTARI_TITLE_OFFENSIVE);
        }
    }
}
