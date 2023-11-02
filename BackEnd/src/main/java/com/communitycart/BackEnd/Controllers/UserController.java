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

import javax.xml.stream.Location;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;


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
    public ResponseEntity<?> getRole(@PathVariable String emailId){
        User user = userService.getUser(emailId);
        if(user == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(userService.getUser(emailId).getRole(), HttpStatus.OK);
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
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping(value = "/uploadPhoto/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadPhoto(@RequestParam("email") String email,
                                              @RequestPart("profilePhoto")MultipartFile profilePhoto)
            throws Exception {
        User user = userService.getUser(email);
        if(user != null){
            if(user.getRole().equals("SELLER")){
                SellerDTO seller = userService.uploadPhoto(email, profilePhoto);
                return new ResponseEntity<>(seller, HttpStatus.OK);
            } else if(user.getRole().equals("BUYER")){
                CustomerDTO customer = userService.uploadCustomerPhoto(email, profilePhoto);
                return new ResponseEntity<>(customer, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @PostMapping("/deleteSeller")
    public ResponseEntity<?> deleteSeller(@RequestBody SellerDTO seller){
        User user = userService.getUser(seller.getEmail());
        if(user != null){
            Seller sellerId = userService.deleteSeller(seller);
            return new ResponseEntity<>(sellerId, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @PostMapping("/updateSeller")
    public ResponseEntity<?> updateSeller(@RequestBody Seller seller){
        User user = userService.getUser(seller.getEmail());
        if(user != null){
            Seller sellerId = userService.updateSeller(seller);
            return new ResponseEntity<>(sellerId, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/getSeller")
    public ResponseEntity<List<SellerDTO>> getSeller(@RequestParam(name = "sellerId", required = false) Long sellerId){
        if(sellerId == null){
            return new ResponseEntity<>(sellerService.getAllSellers(), HttpStatus.OK);
        }
        SellerDTO sellerDTO = userService.getSeller(sellerId);
        if(sellerDTO == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(List.of(sellerDTO), HttpStatus.OK);
    }

}
