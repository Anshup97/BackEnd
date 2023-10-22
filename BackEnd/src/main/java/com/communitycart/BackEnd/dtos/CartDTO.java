package com.communitycart.BackEnd.dtos;

import com.communitycart.BackEnd.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartDTO {
    private Integer cartId;
    private Long customerId;
    private Long productId;
    private Integer quantity;

}
