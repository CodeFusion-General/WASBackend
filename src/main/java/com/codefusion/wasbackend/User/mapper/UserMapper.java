package com.codefusion.wasbackend.User.mapper;

import com.codefusion.wasbackend.User.Model.UserEntity;
import com.codefusion.wasbackend.User.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = LongIdMapper.class)
public interface UserMapper {

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "storeIds", source = "stores")
    UserDTO modelToDTO(UserEntity userEntity);


    UserEntity dtoToModel(UserDTO userDTO);

    UserEntity updateModel(UserDTO userDTO, @MappingTarget UserEntity userEntity);


}
