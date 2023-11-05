package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.OrderDTO;
import com.communitycart.BackEnd.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService service;
    @GetMapping("/checkout")
    public ResponseEntity<?> checkOut(@RequestParam Long customerId, @RequestParam String paymentMethod){
        OrderDTO order = service.checkOut(customerId, paymentMethod);
        return ResponseEntity.ok(order);
    }

    public ResponseEntity<?> getOrders(@RequestParam(required = false) Long customerId, @RequestParam(required = false)
                                       Long sellerId){
        return ResponseEntity.ok(service.getOrders(customerId, sellerId));
    }
}
