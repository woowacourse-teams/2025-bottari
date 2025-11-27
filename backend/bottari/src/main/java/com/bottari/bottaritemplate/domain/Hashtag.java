package com.bottari.bottaritemplate.domain;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Hashtag {

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 10;
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9가-힣_]+$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Hashtag(final String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException(ErrorCode.HASHTAG_NAME_BLANK);
        }
        if (name.contains(" ") || name.contains("\t") || name.contains("\n")) {
            throw new BusinessException(ErrorCode.HASHTAG_NAME_CONTAINS_WHITESPACE);
        }
        if (name.length() < MIN_NAME_LENGTH) {
            throw new BusinessException(ErrorCode.HASHTAG_NAME_TOO_SHORT, "최소 " + MIN_NAME_LENGTH + "자 이상 입력 가능합니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new BusinessException(ErrorCode.HASHTAG_NAME_TOO_LONG, "최대 " + MAX_NAME_LENGTH + "자까지 입력 가능합니다.");
        }
        if (!VALID_NAME_PATTERN.matcher(name).matches()) {
            throw new BusinessException(ErrorCode.HASHTAG_NAME_INVALID_CHARACTER);
        }
    }
}
