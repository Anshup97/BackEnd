package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.AuthRequest;
import com.communitycart.BackEnd.dtos.CustomerDTO;
import com.communitycart.BackEnd.dtos.SellerDTO;
import com.communitycart.BackEnd.entity.Address;
import com.communitycart.BackEnd.entity.Customer;
import com.communitycart.BackEnd.entity.Seller;
import com.communitycart.BackEnd.entity.User;
import com.communitycart.BackEnd.service.ImageStorageService;
import com.communitycart.BackEnd.service.JWTService;
import com.communitycart.BackEnd.service.SellerService;
import com.communitycart.BackEnd.service.UserService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.http.HttpResponse;

@CrossOrigin(origins = "*")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SellerService sellerService;
    @GetMapping("/")
    public ResponseEntity<String> helloWorld(){
        return new ResponseEntity<>("Hello!!", HttpStatus.OK);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(), authRequest.getPassword()
        ));
        if(authentication.isAuthenticated()){
            return new ResponseEntity<>( jwtService.generateToken(authRequest.getEmail()),
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

    @CrossOrigin
    @PostMapping(value = "/addSeller")
    public ResponseEntity<SellerDTO> addSeller(@RequestBody SellerDTO seller)
            throws IOException {
        User user = userService.getUser(seller.getEmail());
        if(user == null){
            System.out.println(seller.toString());
            SellerDTO seller1 = userService.addSeller(seller);
            return new ResponseEntity<>(seller1, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/uploadPhoto/profile/{email}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SellerDTO> uploadPhoto(@PathVariable("email") String email,
                                              @RequestPart("profilePhoto")MultipartFile profilePhoto)
            throws IOException {
        User user = userService.getUser(email);
        if(user != null){
            SellerDTO seller = userService.uploadPhoto(email, profilePhoto);
            return new ResponseEntity<>(seller, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }



//    @PostMapping(value = "/sendPhoto", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public String getPhoto(@RequestPart("name") String email, String name, String contactPhoneNo,
//                           String aadharNo, String password, @RequestPart("photo") MultipartFile photo){
//        return name + photo.getOriginalFilename();
//    }

    @PostMapping("/deleteSeller")
    public ResponseEntity<String> deleteSeller(@RequestBody SellerDTO seller){
        User user = userService.getUser(seller.getEmail());
        if(user != null){
            Long sellerId = userService.deleteSeller(seller).getSellerId();
            return new ResponseEntity<>("Seller deleted with ID - " + sellerId.toString(), HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Seller Not registered.", HttpStatus.NOT_FOUND);
    }


    @PostMapping("/updateSeller")
    public ResponseEntity<String> updateSeller(@RequestBody Seller seller){
        User user = userService.getUser(seller.getEmail());
        if(user != null){
            Long sellerId = userService.updateSeller(seller).getSellerId();
            return new ResponseEntity<>("Seller updated with ID - " + sellerId.toString(), HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Seller Not registered.", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getSeller/{email}")
    public ResponseEntity<SellerDTO> getSeller(@PathVariable(name = "email") String email){
        return new ResponseEntity<>(userService.getSeller(email), HttpStatus.OK);
    }

    @GetMapping("/getProfilePhoto/{email}")
    public ResponseEntity<?> getSellerProfilePhoto(@PathVariable(name = "email") String email){
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(sellerService.getProfilePhoto(email));
    }


}
