package com.bottari.repository;

import com.bottari.domain.BottariTemplateItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BottariTemplateItemRepository extends JpaRepository<BottariTemplateItem, Long> {
    List<BottariTemplateItem> findAllByBottariTemplateId(Long bottariTemplateId);
}
