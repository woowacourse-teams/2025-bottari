package com.bottari.apiworkshop;

import com.bottari.bottaritemplate.domain.BottariTemplateItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenApiItemRepository extends JpaRepository<BottariTemplateItem, Long> {

    List<BottariTemplateItem> findAllByBottariTemplateIdIn(List<Long> inDurationTemplateIds);
}
