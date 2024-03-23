package com.codefusion.wasbackend.base.repository;

import com.codefusion.wasbackend.base.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository extends JpaRepository<BaseEntity, Long> {
}
