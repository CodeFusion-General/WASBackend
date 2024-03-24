package com.codefusion.wasbackend.store.dto;

import com.codefusion.wasbackend.user.model.UserEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StoreDTO implements Serializable {
    Long id;

    @NotBlank(message = "Name cannot be empty")
    String name;

    List<UserEntity> user;
}