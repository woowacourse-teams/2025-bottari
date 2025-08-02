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
public class BottariTemplateItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bottari_template_id")
    private BottariTemplate bottariTemplate;

    public BottariTemplateItem(
            final String name,
            final BottariTemplate bottariTemplate
    ) {
        validateName(name);
        this.name = name;
        this.bottariTemplate = bottariTemplate;
    }

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new BusinessException(ErrorCode.BOTTARI_TEMPLATE_ITEM_NAME_BLANK);
        }
        if (name.length() > 20) {
            throw new BusinessException(ErrorCode.BOTTARI_TEMPLATE_ITEM_NAME_TOO_LONG, "최대 20자까지 입력 가능합니다.");
        }
    }
}
