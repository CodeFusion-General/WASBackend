package com.codefusion.wasbackend.User.dto;

import com.codefusion.wasbackend.User.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private Role role;
    private List<Long> storeIds;
    private Long accountId;
}
