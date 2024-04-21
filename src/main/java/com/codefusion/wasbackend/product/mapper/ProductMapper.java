package com.codefusion.wasbackend.product.mapper;

import com.codefusion.wasbackend.product.dto.ProductDTO;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.user.mapper.UserMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {UserMapper.class})
public interface ProductMapper {

    /**
     * Converts a {@link ProductDTO} object to a {@link ProductEntity} object.
     *
     * @param productDTO the {@link ProductDTO} object representing the product data transfer object to be converted
     * @return the {@link Product*/
    ProductEntity toEntity(ProductDTO productDTO);

    /**
     * Links the product fields of a ProductEntity object to the product entity itself.
     *
     * @param productEntity the ProductEntity object to link the product fields to
     */
    @AfterMapping
    default void linkProductFields(@MappingTarget ProductEntity productEntity) {
        productEntity.getProductFields().forEach(productField -> productField.setProduct(productEntity));
    }

    /**
     * Links the transactions of a product to the product entity.
     *
     * @param productEntity the ProductEntity object representing the product
     */
    @AfterMapping
    default void linkTransactions(@MappingTarget ProductEntity productEntity) {
        productEntity.getTransactions().forEach(transaction -> transaction.setProduct(productEntity));
    }

    /**
     * Converts a ProductEntity object to a ProductDTO object.
     *
     * @param productEntity the ProductEntity object to be converted
     * @return the ProductDTO object representing the converted product
     */
    @Mapping(target = "resourceFileId", source = "resourceFile.id")
    ProductDTO toDto(ProductEntity productEntity);

    /**
     * Partially updates a {@link ProductEntity} based on the corresponding fields in a {@link ProductDTO}.
     *
     * @param productDTO the {@link ProductDTO} object representing the updated product information
     * @param productEntity the {@link ProductEntity}*/
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductEntity partialUpdate(ProductDTO productDTO, @MappingTarget ProductEntity productEntity);

    /**
     * Converts a ProductEntity object to another ProductEntity object.
     *
     * @param productEntity the ProductEntity object to be converted
     * @return the converted ProductEntity object
     */
    ProductEntity toEntity(ProductEntity productEntity);

}
