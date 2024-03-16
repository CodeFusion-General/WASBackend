package com.codefusion.wosbackend.Account.Repository;

import com.codefusion.wosbackend.Account.Model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
