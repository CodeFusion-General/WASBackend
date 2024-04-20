package com.codefusion.wasbackend.account.repository;

import com.codefusion.wasbackend.account.model.AccountEntity;
import com.codefusion.wasbackend.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    @Query("SELECT a.user FROM AccountEntity a WHERE a.id = :id")
    UserEntity findUserById(@Param("id") Long id);
}
