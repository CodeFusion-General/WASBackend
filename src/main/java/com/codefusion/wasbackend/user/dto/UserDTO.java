package com.codefusion.wasbackend.user.dto;

import com.codefusion.wasbackend.user.Role;
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
