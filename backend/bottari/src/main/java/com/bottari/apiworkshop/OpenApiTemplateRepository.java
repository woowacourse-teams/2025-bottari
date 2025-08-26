package com.bottari.apiworkshop;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenApiTemplateRepository extends JpaRepository<BottariTemplate, Long> {

    List<BottariTemplate> findByTitleContaining(final String query);
}
