package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.CustomerDTO;
import com.communitycart.BackEnd.entity.Customer;
import com.communitycart.BackEnd.entity.User;
import com.communitycart.BackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class CustomerController {

    @Autowired
    private UserService userService;

    @PostMapping("/addCustomer")
    public ResponseEntity<CustomerDTO> addCustomer(@RequestBody CustomerDTO customer){
        User user = userService.getUser(customer.getEmail());
        if(user == null){
            CustomerDTO customerDTO = userService.addCustomer(customer);
            return new ResponseEntity<>(customerDTO,
                    HttpStatus.CREATED) {
            };
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @GetMapping("/getCustomer")
    public ResponseEntity<?> getCustomer(@RequestParam(name = "email") String email){
        CustomerDTO customer = userService.getCustomer(email);
        System.out.println(customer);
        if(customer == null){
            return new ResponseEntity<>("Customer not present.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer, HttpStatus.FOUND);
    }
}
