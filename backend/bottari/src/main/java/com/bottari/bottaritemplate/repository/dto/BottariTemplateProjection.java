package com.bottari.bottaritemplate.repository.dto;

import java.time.LocalDateTime;

public interface BottariTemplateProjection {

    Long getBottariTemplateId();

    String getTitle();

    String getDescription();

    Integer getTakenCount();

    LocalDateTime getBottariTemplateCreatedAt();

    Long getMemberId();

    String getMemberName();
}
