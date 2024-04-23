package com.codefusion.wasbackend.account.dto;

import com.codefusion.wasbackend.account.model.Role;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
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