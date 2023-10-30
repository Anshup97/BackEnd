package com.communitycart.BackEnd.dtos;

import com.communitycart.BackEnd.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO {

    private Long orderId;
    private Long customerId;
    private List<ProductDTO> items;


}
