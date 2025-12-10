package com.onlinepay.repository;

import com.onlinepay.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 管理员Repository
 */
@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

    /**
     * 根据用户名查找管理员
     * @param username 用户名
     * @return 管理员实体
     */
    Optional<AdminEntity> findByUsername(String username);

}