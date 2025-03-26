package com.jiyoung.kikihi.platform.adapter.out.jpa.product;

import com.jiyoung.kikihi.platform.adapter.out.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFavoriteJpaEntity extends BaseEntity {

    @Id
    private Long id;
}
