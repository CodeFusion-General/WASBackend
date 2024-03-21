package com.codefusion.wasbackend.User.Model;

import com.codefusion.wasbackend.Account.Model.AccountEntity;
import com.codefusion.wasbackend.Store.Model.StoreEntity;
import com.codefusion.wasbackend.User.Role;
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
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private AccountEntity account;
}
