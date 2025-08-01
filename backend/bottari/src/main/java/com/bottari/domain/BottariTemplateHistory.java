package com.bottari.domain;

import jakarta.persistence.Entity;
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
public class BottariTemplateHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private BottariTemplate bottariTemplate;

    public BottariTemplateHistory(
            final Member member,
            final BottariTemplate bottariTemplate
    ) {
        this.member = member;
        this.bottariTemplate = bottariTemplate;
    }
}
