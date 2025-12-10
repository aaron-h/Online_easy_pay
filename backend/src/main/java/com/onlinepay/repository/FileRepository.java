package com.onlinepay.repository;

import com.onlinepay.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 文件Repository
 */
@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    /**
     * 根据哈希值查找文件
     * @param hash 文件哈希值
     * @return 文件实体
     */
    Optional<FileEntity> findByHash(String hash);

}