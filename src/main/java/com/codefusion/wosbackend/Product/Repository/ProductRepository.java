package com.codefusion.wosbackend.Product.Repository;

import com.codefusion.wosbackend.Product.Model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
