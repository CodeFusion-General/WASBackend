package com.codefusion.wasbackend.account.service;

import com.codefusion.wasbackend.account.dto.AccountEntityDto;
import com.codefusion.wasbackend.account.mapper.AccountEntityMapper;
import com.codefusion.wasbackend.account.model.AccountEntity;
import com.codefusion.wasbackend.account.repository.AccountRepository;
import com.codefusion.wasbackend.user.dto.UserDTO;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;
import com.codefusion.wasbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountEntityMapper accountEntityMapper;
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * Creates a new account.
     *
     * @param userDTO           the UserDTO object containing user information
     * @param file              the profile picture file of the user
     * @param accountEntityDto  the AccountEntityDto object representing the account entity
     * @return the AccountEntityDto object representing the created account entity
     * @throws RuntimeException if there is an error while creating the account
     */
    @Transactional
    public AccountEntityDto createAccount(UserDTO userDTO, MultipartFile file, AccountEntityDto accountEntityDto) {
        try {
            UserDTO createdUserDto = userService.addUser(userDTO, file);

            UserEntity userEntity = userService.getUserByIdforAccount(createdUserDto.getId());

            AccountEntity accountEntity = accountEntityMapper.toEntity(accountEntityDto);

            accountEntity.setUser(userEntity);

            AccountEntity savedAccountEntity = accountRepository.save(accountEntity);

            userEntity.setAccount(savedAccountEntity);

            userRepository.save(userEntity);
            
            return accountEntityMapper.toDto(savedAccountEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create account", e);
        }
    }

}
