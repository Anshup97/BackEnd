package com.communitycart.BackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "product")
public class Product {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long productId;
        private String productName;
        private String productDescription;
        private String productImage;
        private String productSlug;
        private Double productPrice;
        private Integer productQuantity;
        private boolean productFeatured;
        private String productCategory;
        private String shopId;
}
