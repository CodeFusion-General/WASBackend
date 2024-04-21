package com.codefusion.wasbackend.transaction.dto;

import com.codefusion.wasbackend.product.model.ProductEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.codefusion.wasbackend.transaction.model.TransactionEntity}
 */
@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO implements Serializable {
    Long id;
    boolean isBuying;
    boolean isSelling;
    @PastOrPresent(message = "Transaction date must be today or in the past")
    LocalDate date;
    double price;
    @NotBlank(message = "Full name cannot be empty")
    String fullName;
    @NotBlank(message = "Address cannot be empty")
    String address;
    @Size(min = 11, max = 11)
    @NotBlank(message = "Phone cannot be empty")
    String phone;
    ProductEntity product;

    Long resourceFileId;
}