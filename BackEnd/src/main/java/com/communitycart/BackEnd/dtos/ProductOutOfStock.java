package com.communitycart.BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO used for marking a product out of stock.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductOutOfStock {

    private Long productId;
    private Long sellerId;
    private boolean available;
}
