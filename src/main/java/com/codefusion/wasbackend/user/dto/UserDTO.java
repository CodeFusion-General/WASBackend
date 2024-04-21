package com.codefusion.wasbackend.user.dto;

import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.user.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO implements Serializable {
    Long id;
    @NotBlank(message = "Name cannot be empty")
    String name;
    @NotBlank(message = "Surname cannot be empty")
    String surname;
    @NotBlank(message = "Email cannot be empty")
    String email;
    @NotBlank(message = "PhoneNo cannot be empty")
    String phoneNo;
    Role role;
    List<StoreEntity> stores;
}