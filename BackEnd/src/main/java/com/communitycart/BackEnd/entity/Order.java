package com.communitycart.BackEnd.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity
@Table(name = "Order")
public class Order {
    public enum Status {FALSE, PACKED, SHIPPED, DELIVERED }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Long customerId;
    @ManyToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "shop_id")
    private Shop shopId;
    @OneToMany(cascade = CascadeType.ALL, targetEntity = Cart.class)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private List<Cart> orderItems;

    private Address shippingAddress;
    private String paymentMethod;
    private Double itemsPrice;
    private Double shippingPrice;
    private Double taxPrice;
    private Double totalPrice;
    private boolean isPaid = false;
    private Date paidAt;
    private Status status;
    private Date deliveredAt;

}
