package com.communitycart.BackEnd.dtos;

import com.communitycart.BackEnd.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartDTO {

    private Long cartId;
    private Long customerId;
    private List<CartItemDTO> items;

}
