package com.codefusion.wasbackend.Category.dto;

import com.codefusion.wasbackend.CategoryPrototype.model.CategoryPrototypeEntity;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CategoryDto {

    Long id;
    String name;
    Boolean isDelete;

    List<CategoryPrototypeEntity> prototypes;

}