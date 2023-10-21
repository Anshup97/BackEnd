package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.AuthRequest;
import com.communitycart.BackEnd.dtos.CustomerDTO;
import com.communitycart.BackEnd.dtos.SellerDTO;
import com.communitycart.BackEnd.entity.Customer;
import com.communitycart.BackEnd.entity.User;
import com.communitycart.BackEnd.service.JWTService;
import com.communitycart.BackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @GetMapping("/")
    public ResponseEntity<String> helloWorld(){
        return new ResponseEntity<>("Hello!!", HttpStatus.OK);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmailId(), authRequest.getPassword()
        ));
        if(authentication.isAuthenticated()){
            return new ResponseEntity<>( jwtService.generateToken(authRequest.getEmailId()),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>( "Invalid User Request",
                HttpStatus.FORBIDDEN);
    }
    @GetMapping("/getUser/{emailId}")
    public ResponseEntity<String> getRole(@PathVariable String emailId){
        User user = userService.getUser(emailId);
        if(user == null){
            return new ResponseEntity<>("User not present.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.getUser(emailId).getRole(), HttpStatus.FOUND);
    }

    @PostMapping ("/addCustomer")
    public ResponseEntity<String> addCustomer(@RequestBody CustomerDTO customer){
        User user = userService.getUser(customer.getEmailId());
        if(user == null){
            Long customerId = userService.addCustomer(customer).getCustomerId();
            return new ResponseEntity<>("Customer created with ID - " + customerId.toString(),
                    HttpStatus.CREATED) {
            };
        }
        return new ResponseEntity<>("Customer already registered.", HttpStatus.OK);
    }
    @GetMapping("/getCustomer/{email}")
    public ResponseEntity<String> getCustomer(@PathVariable(name = "email") String email){
        Customer customer = userService.getCustomer(email);
        if(customer == null){
            new ResponseEntity<>("Customer not present.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.getCustomer(email).toString(), HttpStatus.FOUND);
    }


    @PostMapping ("/addSeller")
    public ResponseEntity<String> addSeller(@RequestBody SellerDTO seller){
        User user = userService.getUser(seller.getEmail());
        System.out.println(user);
        if(user == null){
            Long sellerId = userService.addSeller(seller).getSellerId();
            return new ResponseEntity<>("Seller created with ID - " + sellerId.toString(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Seller already registered.", HttpStatus.FOUND);
    }
    @GetMapping("/getSeller/{email}")
    public ResponseEntity<String> getSeller(@PathVariable(name = "email") String email){
        return new ResponseEntity<>(userService.getSeller(email).toString(), HttpStatus.OK);
    }


}
