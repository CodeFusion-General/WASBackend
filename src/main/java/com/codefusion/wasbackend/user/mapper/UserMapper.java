package com.codefusion.wasbackend.user.mapper;

import com.codefusion.wasbackend.user.dto.UserDTO;
import com.codefusion.wasbackend.user.model.UserEntity;
import org.mapstruct.*;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {


    UserEntity toEntity(UserDTO userDTO);

    UserDTO toDto(UserEntity userEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserEntity partialUpdate(UserDTO userDTO, @MappingTarget UserEntity userEntity);
}
