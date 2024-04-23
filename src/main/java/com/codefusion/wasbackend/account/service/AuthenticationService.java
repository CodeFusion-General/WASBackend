package com.codefusion.wasbackend.account.service;

import com.codefusion.wasbackend.account.dto.AccountEntityDto;
import com.codefusion.wasbackend.account.model.AccountEntity;
import com.codefusion.wasbackend.account.repository.AccountRepository;
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
        return jwtUtil.generateToken(entity.getUsername(), roles, userId);
    }

    private void validateUserDTO(AccountEntityDto entity) throws Exception {
        if (entity == null || entity.getUsername() == null || entity.getUsername().isEmpty()
                || entity.getPassword() == null || entity.getPassword().isEmpty()) {
            throw new Exception("Invalid request");
        }
    }
}