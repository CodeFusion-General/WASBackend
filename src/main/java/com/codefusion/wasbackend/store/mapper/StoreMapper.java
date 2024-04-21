package com.codefusion.wasbackend.store.mapper;


import com.codefusion.wasbackend.store.dto.StoreDTO;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.user.mapper.UserMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {UserMapper.class})
public interface StoreMapper {

    /**
     * Converts a StoreDTO object to a StoreEntity object.
     *
     * @param storeDTO the StoreDTO object representing the store data transfer object to be converted
     * @return the StoreEntity object representing the converted store entity
     */
    StoreEntity toEntity(StoreDTO storeDTO);

    /**
     * Converts a StoreEntity object to a StoreDTO object.
     *
     * @param storeEntity the StoreEntity object to be converted
     * @return the StoreDTO object representing the converted store
     */
    @Mapping(target = "resourceFileId", source = "resourceFile.id")
    StoreDTO toDto(StoreEntity storeEntity);

    /**
     * Performs a partial update on the given StoreEntity object using the properties from the StoreDTO object.
     *
     * @param storeDTO the StoreDTO object representing the updated store information
     * @param storeEntity the StoreEntity object to update
     * @return the updated StoreEntity object
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    StoreEntity partialUpdate(StoreDTO storeDTO, @MappingTarget StoreEntity storeEntity);
}
