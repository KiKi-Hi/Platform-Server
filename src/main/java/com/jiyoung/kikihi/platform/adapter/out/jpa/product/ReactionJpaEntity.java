package com.jiyoung.kikihi.platform.adapter.out.jpa.product;

import com.jiyoung.kikihi.platform.adapter.out.jpa.BaseEntity;
import com.jiyoung.kikihi.platform.domain.product.LikeList;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reaction")
public class ReactionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long userId;

    @Builder
    public LikeList toDomain() {
        return LikeList.builder()
                .productId(this.productId != null ? this.productId : 0L) // Default value for productId
                .userId(this.userId != null ? this.userId : 0L)         // Default value for userId
                .build();
    }
}
