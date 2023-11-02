package com.communitycart.BackEnd.dtos;

import com.communitycart.BackEnd.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartItemDTO {

    private Long cartItemId;
    private ProductDTO product;
    private Long quantity;
    private Long cartId;

}
