package com.bottari.teambottari.domain;

import com.bottari.vo.ItemName;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamSharedItemInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ItemName name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_bottari_id")
    private TeamBottari teamBottari;

    @CreatedDate
    private LocalDateTime createdAt;

    public TeamSharedItemInfo(
            final String name,
            final TeamBottari teamBottari
    ) {
        this.name = new ItemName(name);
        this.teamBottari = teamBottari;
    }

    public String getName() {
        return name.getName();
    }
}
