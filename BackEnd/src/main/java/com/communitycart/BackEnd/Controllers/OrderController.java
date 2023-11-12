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

@RestController
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService service;


    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkout(@RequestParam Long customerId) throws StripeException {
        Session session = service.createSession(customerId);
        StripeResponse stripeResponse = new StripeResponse(session.getId(), session.getUrl());
        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<?> placeOrder(@RequestBody CreateOrderDTO createOrderDTO){
        OrderDTO order = service.placeOrder(createOrderDTO.getCustomerId(), createOrderDTO.getPaymentMethod(),
                createOrderDTO.getSessionId());
        return ResponseEntity.ok(order);
    }

    @GetMapping("/getOrders")
    public ResponseEntity<?> getOrders(@RequestParam(required = false) Long customerId, @RequestParam(required = false)
                                       Long sellerId){
        return ResponseEntity.ok(service.getOrders(customerId, sellerId));
    }

    @GetMapping("/getOrderById")
    public ResponseEntity<?> getOrderById(@RequestParam Long orderId){
        return ResponseEntity.ok(service.getOrderById(orderId));
    }

    @PutMapping("/updateOrder")
    public ResponseEntity<?> updateOrder(@RequestBody UpdateOrderBySeller updateOrderBySeller){
        OrderDTO orderDTO = service.updateOrder(updateOrderBySeller);
        return ResponseEntity.ok(orderDTO);
    }
}
