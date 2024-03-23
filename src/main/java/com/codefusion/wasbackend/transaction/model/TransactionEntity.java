package com.codefusion.wasbackend.transaction.model;

import com.codefusion.wasbackend.base.model.BaseEntity;
import com.codefusion.wasbackend.product.model.ProductEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class TransactionEntity extends BaseEntity {

    @Column(name = "is_buying")
    private boolean isBuying;

    @Column(name = "is_selling")
    private boolean isSelling;

    @PastOrPresent(message = "Transaction date must be today or in the past")
    @Column(name = "date")
    private LocalDate date;

    @NotEmpty(message = "Price cannot be empty")
    @Column(name = "price")
    private double price;

    @NotBlank(message = "Full name cannot be empty")
    @Column(name = "full_name")
    private String fullName;

    @NotBlank(message = "Address cannot be empty")
    @Column(name = "address")
    private String address;

    @Size(min=11, max=11)
    @NotBlank(message = "Phone cannot be empty")
    @Column(name = "phone")
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

}
