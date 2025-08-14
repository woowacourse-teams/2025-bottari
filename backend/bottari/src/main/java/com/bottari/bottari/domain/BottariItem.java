package com.bottari.bottari.domain;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
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
public class BottariItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ItemName name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bottari_id")
    private Bottari bottari;

    private boolean isChecked;

    // Soft Delete 경우에만 사용됨
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public BottariItem(
            final String name,
            final Bottari bottari
    ) {
        this.name = new ItemName(name);
        this.bottari = bottari;
        this.isChecked = false;
    }

    public void check() {
        if (isChecked) {
            throw new BusinessException(ErrorCode.BOTTARI_ITEM_ALREADY_CHECKED);
        }
        this.isChecked = true;
    }

    public void uncheck() {
        if (!isChecked) {
            throw new BusinessException(ErrorCode.BOTTARI_ITEM_ALREADY_UNCHECKED);
        }
        this.isChecked = false;
    }

    public boolean isOwner(final String ssaid) {
        return bottari.isOwner(ssaid);
    }

    public String getName() {
        return name.getName();
    }
}
