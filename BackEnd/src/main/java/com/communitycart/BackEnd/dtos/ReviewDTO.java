package com.communitycart.BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewDTO {

    private Long reviewId;
    private Integer rating;
    private String review;
    private Long productId;
    private Long customerId;

}
