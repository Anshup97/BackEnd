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
import io.jsonwebtoken.io.Decoder;
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
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * API to manage users (both Customer and Seller).
 */

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

    //Test endpoint to check if server is running or not.
    @GetMapping("/")
    public ResponseEntity<String> helloWorld(){
        return new ResponseEntity<>("Hello!!", HttpStatus.OK);
    }

    /**
     * Authenticates both seller and customer and returns jwt token.
     * @param authRequest
     * @return
     */
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest){
        if(authRequest.isSso()){
            User user = userService.getUser(authRequest.getEmail());
            if(user == null){
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            return new ResponseEntity<>( jwtService.generateToken(authRequest.getEmail()),
                        HttpStatus.OK);

        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(), authRequest.getPassword()
        ));
        if(authentication.isAuthenticated()){
            return new ResponseEntity<>( jwtService.generateToken(authRequest.getEmail()),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>( null,
                HttpStatus.OK);
    }

    /*
    Get user role ('BUYER' or 'SELLER').
     */
    @GetMapping("/getUser/{emailId}")
    public ResponseEntity<?> getRole(@PathVariable String emailId){
        User user = userService.getUser(emailId);
        if(user == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(userService.getUser(emailId).getRole(), HttpStatus.OK);
    }


    /*
    Add new seller and return the created seller object.
    If there is an existing seller with same email, then return null.
     */
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

    /*
    Upload seller and customer profile photo.
    Saved in local.
     */
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

    /*
    Delete seller profile.
     */
    @PostMapping("/deleteSeller")
    public ResponseEntity<?> deleteSeller(@RequestBody SellerDTO seller){
        User user = userService.getUser(seller.getEmail());
        if(user != null){
            Seller sellerId = userService.deleteSeller(seller);
            return new ResponseEntity<>(sellerId, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /*
    Update seller details.
     */
    @PostMapping("/updateSeller")
    public ResponseEntity<?> updateSeller(@RequestBody Seller seller){
        User user = userService.getUser(seller.getEmail());
        if(user != null){
            Seller sellerId = userService.updateSeller(seller);
            return new ResponseEntity<>(sellerId, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /*
    Get seller by sellerId.
     */
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

    /*
    Forgot Password feature for all the users.
    An otp is sent to the email and the user has to verify the otp.
    This endpoint triggers the OTP email and also returns the OTP to the
    frontend for verification.
     */
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam String email){
        String otp = userService.forgotPassword(email);
        if(otp==null || otp.equalsIgnoreCase("")){
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(otp);
    }

    /*
    If the OTP is verified successfully, this endpoint is called
    which updates the new password. Password is always encoded before
    storing in the database.
     */
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody AuthRequest authRequest){
        User user = userService.changePassword(authRequest.getEmail(), authRequest.getPassword());
        if(user==null){
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(null);
    }

}
