package com.codefusion.wosbackend.User.Repository;

import com.codefusion.wosbackend.User.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
