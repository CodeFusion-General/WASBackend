package com.codefusion.wasbackend.user.model;

import com.codefusion.wasbackend.account.model.AccountEntity;
import com.codefusion.wasbackend.base.model.BaseEntity;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.user.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class UserEntity extends BaseEntity {

    @NotBlank(message = "Name cannot be empty")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Surname cannot be empty")
    @Column(name = "surname")
    private String surname;

    @NotBlank(message = "Email cannot be empty")
    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_stores",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "store_id"))
    private List<StoreEntity> stores;

    @OneToOne(mappedBy = "user")
    @JsonBackReference
    private AccountEntity account;
}
