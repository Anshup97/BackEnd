package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.ProductDTO;
import com.communitycart.BackEnd.dtos.SellerDTO;
import com.communitycart.BackEnd.entity.Seller;
import com.communitycart.BackEnd.entity.User;
import com.communitycart.BackEnd.service.SellerService;
import com.communitycart.BackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SellerController {

    @Autowired
    private SellerService sellerService;

    //Add categories
    //Add products
    //Manage Orders
    //Update Seller Details

    @PostMapping("/addProduct")
    public ResponseEntity<Seller> addProduct(@RequestBody ProductDTO productDTO, @RequestBody Long sellerId){
        Seller seller = sellerService.addProduct(productDTO, sellerId);
        return ResponseEntity.ok(seller);
    }

    public ResponseEntity<Seller> deleteProduct(@RequestBody ProductDTO productDTO, Long sellerId){
        Seller seller = sellerService.addProduct(productDTO, sellerId);
        return ResponseEntity.ok(seller);
    }

    public ResponseEntity<Seller> updateProduct(@RequestBody ProductDTO productDTO, Long sellerId){
        Seller seller = sellerService.addProduct(productDTO, sellerId);
        return ResponseEntity.ok(seller);
    }
}
