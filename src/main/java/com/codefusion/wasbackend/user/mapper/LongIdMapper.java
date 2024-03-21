package com.codefusion.wasbackend.user.mapper;

import com.codefusion.wasbackend.store.model.StoreEntity;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class LongIdMapper {

    public List<Long> map(@NotNull List<StoreEntity> stores){
        return stores.stream()
                .map(StoreEntity::getId)
                .collect(Collectors.toList());
    }
}