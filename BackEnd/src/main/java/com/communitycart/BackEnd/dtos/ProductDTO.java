package com.communitycart.BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDTO {

    private Long productId;
    private String productName;
    private Double productPrice;
    private Long productQuantity;
    private String productDescription;
    private String productSlug;
    private boolean productFeatured;
    private String productImageUrl;
    private Long categoryId;
    private Long sellerId;

}
