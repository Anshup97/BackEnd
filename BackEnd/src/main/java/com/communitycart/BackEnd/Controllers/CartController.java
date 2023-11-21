package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.CartDTO;
import com.communitycart.BackEnd.dtos.CartItemDTO;
import com.communitycart.BackEnd.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * API class for Cart.
 * Manages shopping cart of a customer.
 */
@RestController
@CrossOrigin("*")
public class CartController {

    @Autowired
    private CartService cartService;

    /*
    Get the cart items for a customer.
    Fetches cart items when the user logs in.
     */
    @GetMapping("/viewcart")
    public ResponseEntity<?> viewCart(@RequestParam Long customerId){
        CartDTO cartDTO = cartService.getCart(customerId);
        if(cartDTO == null){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        return ResponseEntity.ok(cartDTO);
    }

    /*
    Add item to the cart.
    If the item is already present, it updates the quantity sent in new request.
     */
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

    /*
    Updates cart items like quantity of items
    and add multiple items to the cart at once.
     */
    @PutMapping("/updateCart")
    public ResponseEntity<?> updateCart(@RequestParam Long customerId, @RequestBody List<CartItemDTO> cartItems){
        CartDTO cartDTO = cartService.updateCart(customerId, cartItems);
        if(cartDTO == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return ResponseEntity.ok(cartDTO);
    }

    /*
    If product id is null, delete all the cart items
    else delete cart item with that productId.
     */
    @DeleteMapping("/removeCart")
    public ResponseEntity<?> deleteFromCart(@RequestParam Long customerId, @RequestParam(required = false) Long productId){
        CartDTO cartDTO = cartService.deleteFromCart(customerId, productId);
        if(cartDTO == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return ResponseEntity.ok(cartDTO);
    }
}
