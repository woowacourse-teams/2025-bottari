package com.bottari.bottaritemplate.repository;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.bottaritemplate.domain.BottariTemplateItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BottariTemplateItemRepository extends JpaRepository<BottariTemplateItem, Long> {

    List<BottariTemplateItem> findAllByBottariTemplateId(final Long id);

    List<BottariTemplateItem> findAllByBottariTemplateIn(final List<BottariTemplate> bottariTemplates);

    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE BottariTemplateItem bti
            SET bti.deleted = true, bti.deletedAt = CURRENT_TIMESTAMP
            WHERE bti.bottariTemplate.id = :bottariTemplateId
            """)
    void deleteByBottariTemplateId(final Long bottariTemplateId);
}
