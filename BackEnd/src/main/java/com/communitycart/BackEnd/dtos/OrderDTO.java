package com.communitycart.BackEnd.dtos;

import com.communitycart.BackEnd.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO {

    private Long orderId;
    private Long customerId;
    private Shop shopId;
    private List<Cart> orderItems;
    private Address shippingAddress;
    private String paymentMethod;
    private Double itemsPrice;
    private Double shippingPrice;
    private Double taxPrice;
    private Double totalPrice;
    private boolean isPaid = false;
    private Date paidAt;
    private Order.Status status;
    private Date deliveredAt;

}
