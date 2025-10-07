package com.bottari.bottaritemplate.domain;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import lombok.Getter;

@Getter
public class BottariTemplateHashtagCursor extends BottariTemplateCursor {

    private final Long hashtagId;

    public BottariTemplateHashtagCursor(
            final Long hashtagId,
            final Long lastId,
            final String lastInfo,
            final int page,
            final int size,
            final String property
    ) {
        super(lastId, lastInfo, page, size, property);
        this.hashtagId = validateHashtagId(hashtagId);
    }

    private Long validateHashtagId(final Long hashtagId) {
        if (hashtagId == null) {
            throw new BusinessException(ErrorCode.HASHTAG_ID_MISSING);
        }

        return hashtagId;
    }
}
