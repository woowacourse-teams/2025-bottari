package com.bottari.vo;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.support.BadWordValidator;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BottariTitle {

    private String title;

    public BottariTitle(final String title) {
        validateTitle(title);
        this.title = title;
    }

    private void validateTitle(final String title) {
        if (title.isBlank()) {
            throw new BusinessException(ErrorCode.BOTTARI_TITLE_BLANK);
        }
        if (title.length() > 15) {
            throw new BusinessException(ErrorCode.BOTTARI_TITLE_TOO_LONG);
        }
        if (BadWordValidator.hasBadWord(title)) {
            throw new BusinessException(ErrorCode.BOTTARI_TITLE_OFFENSIVE);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final BottariTitle that)) {
            return false;
        }
        return Objects.equals(getTitle(), that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTitle());
    }
}
