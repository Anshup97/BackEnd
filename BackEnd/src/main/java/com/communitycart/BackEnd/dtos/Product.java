package com.communitycart.BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    private String productName;
    private Double productPrice;
    private String productImage;
    private String productQuantity;
    private String productDescription;
    private String productSlug;
    private boolean productFeatured;
    private CategoryDTO category;


}
