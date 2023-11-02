package com.communitycart.BackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "CartItem")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartItem {
    @Id
    @SequenceGenerator(name = "cart_item_sequence",
    sequenceName = "cart_item_sequence",
    allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "cart_item_sequence")
    private Long cartItemId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    private Product product;
    private Long quantity;
    private Long cartId;
}
