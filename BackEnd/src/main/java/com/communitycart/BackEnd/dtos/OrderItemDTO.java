package com.communitycart.BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItemDTO {

    private Long orderItemId;
    private Long quantity;
    private ProductDTO product;
    private Double totalPrice;
    private Double itemPrice;

}
