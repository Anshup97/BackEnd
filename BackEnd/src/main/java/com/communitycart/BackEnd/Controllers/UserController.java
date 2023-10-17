package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.CustomerDTO;
import com.communitycart.BackEnd.dtos.SellerDTO;
import com.communitycart.BackEnd.entity.User;
import com.communitycart.BackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/")
    public String helloWorld(){
        return "Hello!!";
    }
    @GetMapping("/getUser")
    public String getRole(@RequestParam String emailId){
        User user = userService.getUser(emailId);
        if(user == null){
            return "User not present.";
        }
        return userService.getUser(emailId).getRole();
    }

    @PostMapping ("/addCustomer")
    public ResponseEntity<String> addCustomer(@RequestBody CustomerDTO customer){
        User user = userService.getUser(customer.getEmailId());
        System.out.println(user);
        if(user == null){
            Long customerId = userService.addCustomer(customer).getCustomerId();
            return new ResponseEntity<>("Customer created with ID - " + customerId.toString(),
                    HttpStatus.CREATED) {
            };
        }
        return new ResponseEntity<>("Customer already registered.", HttpStatus.OK);
    }
    @GetMapping("/getCustomer/{email}")
    public String getCustomer(@PathVariable(name = "email") String email){
        return userService.getCustomer(email).toString();
    }


    @PostMapping ("/addSeller")
    public String addSeller(@RequestBody SellerDTO seller){
        User user = userService.getUser(seller.getEmail());
        System.out.println(user);
        if(user == null){
            Long sellerId = userService.addSeller(seller).getSellerId();
            return "Seller created with ID - " + sellerId.toString();
        }
        return "Seller already registered.";
    }
    @GetMapping("/getSeller/{email}")
    public String getSeller(@PathVariable(name = "email") String email){
        return userService.getSeller(email).toString();
    }


}
