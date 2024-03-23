package com.codefusion.wasbackend.productField.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductFieldDTO {


    private Long id;

    private String name;

    private String feature;

    @NotNull
    private Long productId;
}
