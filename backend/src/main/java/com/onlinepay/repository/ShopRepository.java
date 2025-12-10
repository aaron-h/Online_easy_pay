package com.onlinepay.repository;

import com.onlinepay.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 商店Repository
 */
@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Long> {

}