package com.codefusion.wasbackend.Account.dto;

import com.codefusion.wasbackend.Account.model.Role;
import com.codefusion.wasbackend.user.model.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@ToString
public class AccountEntityDto implements Serializable {

    Long id;
    String username;
    String password;

    UserEntity user;
    Set<Role> roles;



}