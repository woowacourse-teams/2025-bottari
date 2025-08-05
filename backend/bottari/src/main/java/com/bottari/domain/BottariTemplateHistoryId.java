package com.bottari.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BottariTemplateHistoryId implements Serializable {

    @Column(name = "receiver_id")
    private Long memberId;

    @Column(name = "template_id")
    private Long bottariTemplateId;

    protected BottariTemplateHistoryId(
            final Long memberId,
            final Long bottariTemplateId
    ) {
        this.memberId = memberId;
        this.bottariTemplateId = bottariTemplateId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final BottariTemplateHistoryId that)) {
            return false;
        }

        return Objects.equals(getMemberId(), that.getMemberId())
                && Objects.equals(getBottariTemplateId(), that.getBottariTemplateId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMemberId(), getBottariTemplateId());
    }
}
