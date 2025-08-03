package com.bottari.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BottariTemplateHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receiver_id")
    private Long memberId;

    @Column(name = "template_id")
    private Long bottariTemplateId;

    public BottariTemplateHistory(
            final Long memberId,
            final Long bottariTemplateId
    ) {
        this.memberId = memberId;
        this.bottariTemplateId = bottariTemplateId;
    }
}
