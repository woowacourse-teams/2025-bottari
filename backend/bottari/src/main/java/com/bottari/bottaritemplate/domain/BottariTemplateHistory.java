package com.bottari.bottaritemplate.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BottariTemplateHistory {

    @EmbeddedId
    private BottariTemplateHistoryId id;

    public BottariTemplateHistory(
            final Long memberId,
            final Long bottariTemplateId
    ) {
        this.id = new BottariTemplateHistoryId(memberId, bottariTemplateId);
    }
}
