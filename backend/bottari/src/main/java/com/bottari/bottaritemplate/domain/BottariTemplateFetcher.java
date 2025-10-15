package com.bottari.bottaritemplate.domain;

import com.bottari.bottaritemplate.repository.dto.BottariTemplateProjection;
import org.springframework.data.domain.Slice;

@FunctionalInterface
public interface BottariTemplateFetcher<T extends BottariTemplateCursor> {

    Slice<BottariTemplateProjection> fetch(final T cursor);
}
