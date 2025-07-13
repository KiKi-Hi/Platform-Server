package com.jiyoung.kikihi.platform.adapter.out.jpa.order.repository;

import com.jiyoung.kikihi.platform.adapter.out.jpa.order.DeliveryInfoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryInfoJpaRepository extends JpaRepository<DeliveryInfoJpaEntity, UUID> {

}
