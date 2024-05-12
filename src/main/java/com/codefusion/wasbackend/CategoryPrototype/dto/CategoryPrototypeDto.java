package com.codefusion.wasbackend.CategoryPrototype.dto;

import com.codefusion.wasbackend.Category.model.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CategoryPrototypeDto {
    Long id;
    String name;
    Boolean isDelete;
    CategoryEntity category;
}