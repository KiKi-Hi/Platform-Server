package com.jiyoung.kikihi.platform.adapter.out.jpa.order.repository;

import com.jiyoung.kikihi.platform.adapter.out.jpa.order.DeliveryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryJpaRepository extends JpaRepository<DeliveryJpaEntity, UUID> {


}
