package com.bottari.repository;

import com.bottari.domain.BottariTemplate;
import com.bottari.domain.BottariTemplateItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BottariTemplateItemRepository extends JpaRepository<BottariTemplateItem, Long> {

    List<BottariTemplateItem> findAllByBottariTemplateId(final Long bottariTemplateId);

    List<BottariTemplateItem> findAllByBottariTemplateIn(final List<BottariTemplate> bottariTemplates);
}
