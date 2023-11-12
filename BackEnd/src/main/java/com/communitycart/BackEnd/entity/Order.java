package com.communitycart.BackEnd.entity;

import com.communitycart.BackEnd.dtos.OrderItemDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "orderId", referencedColumnName = "orderId")
    private List<OrderItem> items;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId", referencedColumnName = "addressId")
    private Address shippingAddress;
}
