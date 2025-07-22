package com.bottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.domain.BottariTemplate;
import com.bottari.domain.BottariTemplateItem;
import com.bottari.domain.Member;
import com.bottari.dto.ReadBottariTemplateResponse;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(BottariTemplateService.class)
class BottariTemplateServiceTest {

    @Autowired
    private BottariTemplateService bottariTemplateService;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("모든 보따리 템플릿을 조회한다.")
    @Test
    void getAll() {
        // given
        final Member member = new Member("ssaid", "name");
        entityManager.persist(member);

        final BottariTemplate template1 = new BottariTemplate("title_1", member);
        final BottariTemplateItem item1 = new BottariTemplateItem("item_1", template1);
        final BottariTemplateItem item2 = new BottariTemplateItem("item_2", template1);
        entityManager.persist(template1);
        entityManager.persist(item1);
        entityManager.persist(item2);

        final BottariTemplate template2 = new BottariTemplate("title_2", member);
        final BottariTemplateItem item3 = new BottariTemplateItem("item_3", template2);
        entityManager.persist(template2);
        entityManager.persist(item3);

        // when
        final List<ReadBottariTemplateResponse> actual = bottariTemplateService.getAll();

        // then
        assertAll(() -> {
                    assertThat(actual).hasSize(2);
                    assertThat(actual.getFirst().title()).isEqualTo("title_1");
                    assertThat(actual.getFirst().items()).hasSize(2);
                    assertThat(actual.get(0).items().get(0).name()).isEqualTo("item_1");
                    assertThat(actual.get(0).items().get(1).name()).isEqualTo("item_2");
                    assertThat(actual.get(1).title()).isEqualTo("title_2");
                    assertThat(actual.get(1).items()).hasSize(1);
                    assertThat(actual.get(1).items().getFirst().name()).isEqualTo("item_3");
                }
        );
    }
}
