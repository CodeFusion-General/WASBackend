package com.codefusion.wasbackend.Account.Repository;

import com.codefusion.wasbackend.Account.Model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
