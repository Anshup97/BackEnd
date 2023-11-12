package com.communitycart.BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateOrderBySeller {

    private Long orderId;
    private boolean isPaid;
    private Date deliveryDate;
    private Date deliveredAt;
    private String status;


}
