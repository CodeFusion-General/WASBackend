package com.codefusion.wasbackend.transaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private Long id;
    private boolean isBuying;
    private boolean isSelling;
    private LocalDate date;
    private double price;
    private String fullName;
    private String address;
    private String phone;

    @NotNull
    private Long productId;

}
