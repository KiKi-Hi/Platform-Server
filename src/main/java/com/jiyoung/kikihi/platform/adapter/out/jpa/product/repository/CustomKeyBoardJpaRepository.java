package com.jiyoung.kikihi.platform.adapter.out.jpa.product.repository;

import com.jiyoung.kikihi.platform.adapter.out.jpa.product.CustomKeyBoardJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomKeyBoardJpaRepository extends JpaRepository<CustomKeyBoardJpaEntity, Long> {

}
