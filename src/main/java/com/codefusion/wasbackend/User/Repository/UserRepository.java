package com.codefusion.wasbackend.User.Repository;

import com.codefusion.wasbackend.User.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u JOIN u.stores s WHERE s.id = ?1")
    List<UserEntity> findByStoreId (Long storeId);
}
