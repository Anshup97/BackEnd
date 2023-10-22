package com.communitycart.BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderResponseDTO {

    private Double amount;
    private Long invoiceNumber;
    private String date;
    private String OrderDescription;
    private Long orderId;

}
