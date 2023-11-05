package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.OrderDTO;
import com.communitycart.BackEnd.dtos.StripeResponse;
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


    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestParam Long customerId) throws StripeException {
        // create the stripe session
        Session session = service.createSession(customerId);
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        // send the stripe session id in response
        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
    }

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
