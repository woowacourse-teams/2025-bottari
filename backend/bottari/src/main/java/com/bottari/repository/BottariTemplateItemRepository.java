package com.bottari.repository;

import com.bottari.domain.BottariTemplate;
import com.bottari.domain.BottariTemplateItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BottariTemplateItemRepository extends JpaRepository<BottariTemplateItem, Long> {

    List<BottariTemplateItem> findAllByBottariTemplateId(final Long id);

    List<BottariTemplateItem> findAllByBottariTemplateIn(final List<BottariTemplate> bottariTemplates);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM BottariTemplateItem bi where bi.bottariTemplate.id = :bottariTemplateId")
    void deleteByBottariTemplateId(final Long bottariTemplateId);
}
