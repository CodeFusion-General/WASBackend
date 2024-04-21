package com.codefusion.wasbackend.account.service;

import com.codefusion.wasbackend.account.dto.AccountEntityDto;
import com.codefusion.wasbackend.account.mapper.AccountEntityMapper;
import com.codefusion.wasbackend.account.model.AccountEntity;
import com.codefusion.wasbackend.account.model.Role;
import com.codefusion.wasbackend.account.repository.AccountRepository;
import com.codefusion.wasbackend.user.dto.UserDTO;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;
import com.codefusion.wasbackend.user.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Service
public class AccountService {

    private final AccountEntityMapper accountEntityMapper;
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AccountService(AccountEntityMapper accountEntityMapper, AccountRepository accountRepository, UserService userService,
                          UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.accountEntityMapper = accountEntityMapper;
        this.accountRepository = accountRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

    public void initializeUsers() {
        if (accountRepository.count() == 0) {
            createUsers();
        }
    }

    public void createUsers() {
        AccountEntity admin = createAccountEntity("admin", "admin123", Role.ADMIN);
        AccountEntity superUser = createAccountEntity("boss", "boss123", Role.BOSS);
        AccountEntity user = createAccountEntity("manager", "manager123", Role.MANAGER);
        AccountEntity authUser = createAccountEntity("employee", "employee123", Role.EMPLOYEE);

        accountRepository.saveAll(Arrays.asList(admin, superUser, user, authUser));
    }

    private AccountEntity createAccountEntity(String username, String password, Role role) {
        AccountEntity account = new AccountEntity();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setRoles(new HashSet<>(Collections.singletonList(role)));
        return account;
    }


}
