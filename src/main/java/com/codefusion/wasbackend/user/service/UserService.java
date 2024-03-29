package com.codefusion.wasbackend.user.service;

import com.codefusion.wasbackend.base.service.BaseService;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.user.dto.UserDTO;
import com.codefusion.wasbackend.user.mapper.UserMapper;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class UserService extends BaseService<UserEntity, UserDTO, UserRepository> {

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, ResourceFileService resourceFileService, UserMapper userMapper) {
        super(userRepository, resourceFileService);
        this.userMapper = userMapper;
    }

    @Override
    protected UserDTO convertToDto(UserEntity entity) {
        return userMapper.modelToDTO(entity);
    }

    @Override
    protected UserEntity convertToEntity(UserDTO dto) {
        return userMapper.dtoToModel(dto);
    }

    //get user by id
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        return userMapper.modelToDTO(repository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    //get all users
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers(){
        List<UserEntity> userEntities = repository.findAll();
        return userEntities.stream()
                .map(userMapper::modelToDTO)
                .toList();
    }

    //get users by store id
    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByStoreId(Long storeId) {
        List<UserEntity> userEntities = repository.findByStoreId(storeId);
        return userEntities.stream()
                .map(userMapper::modelToDTO)
                .toList();
    }

    //add User
    @Transactional
    public UserDTO addUser(UserDTO userDTO, MultipartFile file) throws IOException {
        return super.add(userDTO, file);
    }

    //Update User
    @Override
    protected void updateEntity(UserDTO dto, UserEntity entity) {
        userMapper.updateModel(dto, entity);
    }

    //Delete User
    @Transactional
    public void delete(Long userId) throws IOException {
        super.delete(userId);
    }

}
