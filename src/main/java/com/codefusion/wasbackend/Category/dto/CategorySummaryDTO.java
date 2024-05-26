package com.codefusion.wasbackend.Category.dto;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.codefusion.wasbackend.Category.model.CategoryEntity}
 */
@AllArgsConstructor
@Getter
@ToString
@NoArgsConstructor
@Setter
@Builder
public class CategorySummaryDTO implements Serializable {
    private Long categoryId;
    private String name;
    private double totalProfit;
    private int productCount;
}