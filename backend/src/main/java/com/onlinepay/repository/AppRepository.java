package com.onlinepay.repository;

import com.onlinepay.entity.AppEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 应用Repository
 */
@Repository
public interface AppRepository extends JpaRepository<AppEntity, Long> {

}