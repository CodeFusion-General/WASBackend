package com.codefusion.wasbackend.user.repository;

import com.codefusion.wasbackend.account.model.Role;
import com.codefusion.wasbackend.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Retrieves a list of user entities by the ID of the store they are associated with.
     *
     * @param storeId the ID of the store
     * @return a List of UserEntity objects matching the given store ID
     */
    @Query("SELECT u FROM UserEntity u JOIN u.stores s WHERE s.id = :storeId")
    List<UserEntity> findByStoreId (@Param("storeId") Long storeId);


    /**
     * Retrieves a user by the ID of the store they are associated with and their roles.
     *
     * @param storeId the ID of the store
     * @param roles the roles to filter by
     * @return the UserEntity object representing the user matching the given store ID and roles
     */
    @Query("SELECT u FROM UserEntity u JOIN u.stores s JOIN u.account a JOIN a.roles r WHERE s.id = :storeId AND r IN (:roles)")
    UserEntity findByStoreIdAndRoles(@Param("storeId") Long storeId, @Param("roles") List<Role> roles);

    List<UserEntity> findByIdIn(List<Long> ids);

}
