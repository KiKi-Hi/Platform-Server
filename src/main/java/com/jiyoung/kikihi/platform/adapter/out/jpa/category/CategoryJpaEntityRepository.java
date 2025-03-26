package com.jiyoung.kikihi.platform.adapter.out.jpa.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryJpaEntityRepository extends JpaRepository<CategoryJpaEntity, Long> {
}
