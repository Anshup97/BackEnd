package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.CartDTO;
import com.communitycart.BackEnd.dtos.CartItemDTO;
import com.communitycart.BackEnd.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@CrossOrigin("*")
public class CartController {

    @Autowired
    private CartService cartService;
    @GetMapping("/viewcart")
    public ResponseEntity<?> viewCart(@RequestParam Long customerId){
        CartDTO cartDTO = cartService.getCart(customerId);
        if(cartDTO == null){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        return ResponseEntity.ok(cartDTO);
    }

    @PostMapping("/addtocart")
    public ResponseEntity<?> addToCart(@RequestParam Long customerId,
                                       @RequestBody CartItemDTO item){
        if(customerId == null){
            return new ResponseEntity<>(null,
                    HttpStatus.OK);
        }
        CartDTO cartDTO = cartService.addToCart(customerId, item);
        if(cartDTO == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return ResponseEntity.ok(cartDTO);
    }

    @PutMapping("/updateCart")
    public ResponseEntity<?> updateCart(@RequestParam Long customerId, @RequestBody CartItemDTO item){
        CartDTO cartDTO = cartService.updateCart(customerId, item);
        if(cartDTO == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return ResponseEntity.ok(cartDTO);
    }

    @DeleteMapping("/removeCart")
    public ResponseEntity<?> deleteFromCart(@RequestParam Long customerId, @RequestBody(required = false) Long productId){
        CartDTO cartDTO = cartService.deleteFromCart(customerId, productId);
        if(cartDTO == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return ResponseEntity.ok(cartDTO);
    }
}
