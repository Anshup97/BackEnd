package com.communitycart.BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;




@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO {

    private Long orderId;
    private Long customerId;
    private Long sellerId;
    private String paymentMethod;
    private Double totalPrice;
    private Double shippingCharges;
    private boolean isPaid;
    private Date createdAt;
    private Date deliveryDate;
    private Date deliveredAt;
    private String status;
    private String sessionId;

    private List<OrderItemDTO> items;
    private AddressDTO shippingAddress;

}
