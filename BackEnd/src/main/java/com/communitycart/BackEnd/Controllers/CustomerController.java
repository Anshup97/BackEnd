package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.CustomerDTO;
import com.communitycart.BackEnd.entity.Customer;
import com.communitycart.BackEnd.entity.User;
import com.communitycart.BackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Customer API class for managing customers.
 */
@RestController
@CrossOrigin("*")
public class CustomerController {

    @Autowired
    private UserService userService;

    /**
     * Add a customer to the database.
     * Customer details entered by the customer while signup is passed to this endpoint
     * and customer is added.
     * If customer is already present, it doesn't add the customer and returns null.
     * @param customer
     * @return
     */
    @PostMapping("/addCustomer")
    public ResponseEntity<CustomerDTO> addCustomer(@RequestBody CustomerDTO customer){
        User user = userService.getUser(customer.getEmail());
        if(user == null){
            CustomerDTO customerDTO = userService.addCustomer(customer);
            return new ResponseEntity<>(customerDTO,
                    HttpStatus.CREATED) {
            };
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * Get customer details from database using customerId.
     * @param customerId
     * @return
     */
    @GetMapping("/getCustomer")
    public ResponseEntity<?> getCustomer(@RequestParam Long customerId){
        CustomerDTO customer = userService.getCustomer(customerId);
        System.out.println(customer);
        if(customer == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    /**
     * Delete a customer.
     * @param email
     * @return
     */
    @DeleteMapping("/deleteCustomer")
    @Transactional
    public ResponseEntity<?> deleteCustomer(@RequestParam String email){
        CustomerDTO customerDTO = userService.deleteCustomer(email);
        if(customerDTO == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return ResponseEntity.ok(customerDTO);
    }
}
