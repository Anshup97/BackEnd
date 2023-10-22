package com.communitycart.BackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Cart")
public class Cart {
    @Id
    @SequenceGenerator(
            name = "cart_sequence",
            sequenceName = "cart_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "cart_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Integer cartId;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName="customer_id")
    private Long customerId;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName="product_id")
    private Long productId;

    private Integer quantity;

}