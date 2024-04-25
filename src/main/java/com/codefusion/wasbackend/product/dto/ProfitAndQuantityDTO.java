package com.codefusion.wasbackend.product.dto;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class ProfitAndQuantityDTO implements Serializable {
    double totalProfit;
    int totalQuantity;
}