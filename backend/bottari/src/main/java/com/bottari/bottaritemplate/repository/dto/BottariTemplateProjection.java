package com.bottari.bottaritemplate.repository.dto;

import java.time.LocalDateTime;

public interface BottariTemplateProjection {

    Long getBottariTemplateId();
    String getTitle();
    Integer getTakenCount();
    LocalDateTime getBottariTemplateCreatedAt();
    Long getMemberId();
    String getMemberName();
}
