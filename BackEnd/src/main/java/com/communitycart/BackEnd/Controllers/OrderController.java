package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.CreateOrderDTO;
import com.communitycart.BackEnd.dtos.OrderDTO;
import com.communitycart.BackEnd.dtos.StripeResponse;
import com.communitycart.BackEnd.dtos.UpdateOrderBySeller;
import com.communitycart.BackEnd.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Order API for managing customer orders.
 */
@RestController
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService service;

    /**
     * This endpoint is called if a customer wants to do online payment.
     * Stripe payment gateway is used for online payment. This method
     * creates a stripe session and show the customer cart items, total
     * order amount, quantity of ordered items in the payment page. It returns
     * stripe session Id and payment page url to the front-end.
     * If an online payment is successful, sessionId is saved in the database and
     * placeOrder endpoint is called to place the order.
     * @param customerId
     * @return
     * @throws StripeException
     */
    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkout(@RequestParam Long customerId) throws StripeException {
        Session session = service.createSession(customerId);
        StripeResponse stripeResponse = new StripeResponse(session.getId(), session.getUrl());
        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
    }

    /**
     * This endpoint is used to place order.
     * It will create a new order and trigger an email to the customer that order is placed.
     * @param createOrderDTO
     * @return
     */
    @PostMapping("/placeOrder")
    public ResponseEntity<?> placeOrder(@RequestBody CreateOrderDTO createOrderDTO){
        OrderDTO order = service.placeOrder(createOrderDTO.getCustomerId(), createOrderDTO.getPaymentMethod(),
                createOrderDTO.getSessionId());
        return ResponseEntity.ok(order);
    }

    /**
     * This endpoint is used to get orders placed by a customer or all the orders placed
     * for a seller.
     * @param customerId
     * @param sellerId
     * @return
     */
    @GetMapping("/getOrders")
    public ResponseEntity<?> getOrders(@RequestParam(required = false) Long customerId, @RequestParam(required = false)
                                       Long sellerId){
        return ResponseEntity.ok(service.getOrders(customerId, sellerId));
    }

    /*
    Get order by order Id.
     */
    @GetMapping("/getOrderById")
    public ResponseEntity<?> getOrderById(@RequestParam Long orderId){
        return ResponseEntity.ok(service.getOrderById(orderId));
    }

    /**
     * Seller can use this endpoint to update order details.
     * After an order is placed by the customer, seller can update the
     * delivery date, payment status and delivery status like Packed,
     * Shipped and Delivered. On every update, appropriate mail will be
     * triggered to the customer.
     * @param updateOrderBySeller
     * @return
     */
    @PutMapping("/updateOrder")
    public ResponseEntity<?> updateOrder(@RequestBody UpdateOrderBySeller updateOrderBySeller){
        OrderDTO orderDTO = service.updateOrder(updateOrderBySeller);
        return ResponseEntity.ok(orderDTO);
    }
}
