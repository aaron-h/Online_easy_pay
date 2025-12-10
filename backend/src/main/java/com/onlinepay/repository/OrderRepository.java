package com.onlinepay.repository;

import com.onlinepay.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 订单Repository
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    /**
     * 根据订单ID查找订单
     * @param orderId 订单ID
     * @return 订单实体
     */
    Optional<OrderEntity> findByOrderId(String orderId);

}