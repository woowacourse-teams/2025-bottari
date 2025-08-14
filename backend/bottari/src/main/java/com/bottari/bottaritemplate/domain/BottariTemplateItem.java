package com.bottari.bottaritemplate.domain;

import com.bottari.vo.ItemName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BottariTemplateItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ItemName name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bottari_template_id")
    private BottariTemplate bottariTemplate;

    // Soft Delete 경우에만 사용됨
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public BottariTemplateItem(
            final String name,
            final BottariTemplate bottariTemplate
    ) {
        this.name = new ItemName(name);
        this.bottariTemplate = bottariTemplate;
    }

    public String getName() {
        return name.getName();
    }
}
