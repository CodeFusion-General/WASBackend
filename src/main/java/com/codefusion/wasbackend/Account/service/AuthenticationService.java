package com.codefusion.wasbackend.Account.service;

import com.codefusion.wasbackend.Account.dto.AccountEntityDto;
import com.codefusion.wasbackend.Account.model.AccountEntity;
import com.codefusion.wasbackend.Account.model.Role;
import com.codefusion.wasbackend.Account.repository.AccountRepository;
import com.codefusion.wasbackend.config.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(AccountEntityDto entity) throws Exception {
        validateUserDTO(entity);

        AccountEntity account = accountRepository.findByUsername(entity.getUsername());
        if (account == null) {
            throw new Exception("User not found");
        }
        if (!passwordEncoder.matches(entity.getPassword(), account.getPassword())) {
            throw new Exception("Invalid password");
        }

        List<String> roles = account.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        Long userId;
        if(account.getUser() != null && account.getUser().getId() != null){
            userId = account.getUser().getId();
        }
        else{
            userId = account.getId();
        }

        Long storeId = null;
        if (roles.contains("MANAGER") || roles.contains("EMPLOYEE")) {
            assert account.getUser() != null;
            storeId = account.getUser().getStores().getFirst().getId();
        }
        return jwtUtil.generateToken(entity.getUsername(), roles, userId, storeId);
    }

    private void validateUserDTO(AccountEntityDto entity) throws Exception {
        if (entity == null || entity.getUsername() == null || entity.getUsername().isEmpty()
                || entity.getPassword() == null || entity.getPassword().isEmpty()) {
            throw new Exception("Invalid request");
        }
    }
}
