package com.codefusion.wasbackend.user.service;

import com.codefusion.wasbackend.account.model.Role;
import com.codefusion.wasbackend.base.service.BaseService;
import com.codefusion.wasbackend.product.dto.ProductDTO;
import com.codefusion.wasbackend.product.mapper.ProductMapper;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.store.dto.StoreDTO;
import com.codefusion.wasbackend.store.mapper.StoreMapper;
import com.codefusion.wasbackend.user.dto.UserDTO;
import com.codefusion.wasbackend.user.mapper.UserMapper;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * This class represents a UserService that provides operations related to User entities.
 */
@Service
public class UserService extends BaseService<UserEntity, UserDTO, UserRepository> {

    private final UserMapper userMapper;
    private final StoreMapper storeMapper;
    private final ProductMapper productMapper;

    public UserService(UserRepository userRepository, ResourceFileService resourceFileService,
                       UserMapper userMapper, StoreMapper storeMapper, ProductMapper productMapper) {
        super(userRepository, userRepository, resourceFileService);
        this.userMapper = userMapper;
        this.storeMapper = storeMapper;
        this.productMapper = productMapper;
    }

    @Override
    protected UserDTO convertToDto(UserEntity entity) {
        return userMapper.toDto(entity);
    }

    @Override
    protected UserEntity convertToEntity(UserDTO dto) {
        return userMapper.toEntity(dto);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user
     * @return the UserDTO object representing the user with the given ID
     * @throws RuntimeException if the user is not found
     */
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        return userMapper.toDto(repository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }




    /**
     * Retrieves a user by ID for account creation.
     *
     * @param id the ID of the user
     * @return the UserEntity object representing the user with the given ID
     * @throws RuntimeException if the user is not found
     */
    @Transactional(readOnly = true)
    public UserEntity getUserByIdforAccount(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }


    /**
     * Retrieves managers and employees based on the given store ID.
     *
     * @param storeId the ID of the store
     * @return a*/
    @Transactional(readOnly = true)
    public UserDTO getManagersAndEmployees(Long storeId){
        return userMapper.toDto(repository.findByStoreIdAndRoles(storeId, Arrays.asList(Role.EMPLOYEE, Role.MANAGER)));
    }

    /**
     * Retrieves all users.
     *
     * @return a List of UserDTO objects representing all users
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers(){
        List<UserEntity> userEntities = repository.findAll();
        return userEntities.stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Retrieves all products associated with a user.
     *
     * @param userId the ID of the user
     * @return a List of ProductDTO objects representing all products associated with the user
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllUserProducts(Long userId) {
        Optional<UserEntity> userEntityOpt = repository.findById(userId);
        List<ProductDTO> allProductDTOs = new ArrayList<>();

        if (userEntityOpt.isPresent()) {
            List<StoreDTO> userStoreDTOs = userEntityOpt.get().getStores().stream()
                    .map(storeMapper::toDto)
                    .toList();

            for (StoreDTO storeDTO : userStoreDTOs) {
                List<ProductDTO> productDTOs = storeDTO.getProducts().stream()
                        .map(productMapper::toDto)
                        .toList();

                allProductDTOs.addAll(productDTOs);
            }
        }

        return allProductDTOs;
    }

    /**
     * Retrieves users by store ID.
     *
     * @param storeId the ID of the store
     * @return a List of UserDTO objects representing the users with the given store ID
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByStoreId(Long storeId) {
        List<UserEntity> userEntities = repository.findByStoreId(storeId);
        return userEntities.stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Adds a new user.
     *
     * @param userDTO the user data transfer object containing user information
     * @param file the profile picture file of the user
     * @return the UserDTO object representing the user that was added
     * @throws IOException if there is an error with the file operation
     */
    @Transactional
    public UserDTO addUser(UserDTO userDTO, MultipartFile file) throws IOException {
        return super.add(userDTO, file);
    }

    /**
     * Updates the user entity using the data transfer object.
     *
     * @param dto the UserDTO object representing the updated user information
     * @param entity the UserEntity object to update
     */
    @Override
    protected void updateEntity(UserDTO dto, UserEntity entity) {
        userMapper.partialUpdate(dto, entity);
    }

    /**
     * Deletes a user with the given user ID.
     *
     * @param userId the ID of the user to delete
     * @throws IOException if there is an error with the file operation
     * @throws NullPointerException if the user ID is null
     */
    @Transactional
    public void delete(Long userId) throws IOException {
        super.delete(userId);
    }

}
