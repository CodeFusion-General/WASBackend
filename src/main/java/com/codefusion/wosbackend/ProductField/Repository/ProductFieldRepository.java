package com.codefusion.wosbackend.ProductField.Repository;

import com.codefusion.wosbackend.ProductField.Model.ProductFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductFieldRepository extends JpaRepository<ProductFieldEntity, Long> {
}
