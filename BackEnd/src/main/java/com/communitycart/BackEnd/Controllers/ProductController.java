package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.ProductDTO;
import com.communitycart.BackEnd.dtos.ProductOutOfStock;
import com.communitycart.BackEnd.dtos.ReviewDTO;
import com.communitycart.BackEnd.entity.Product;
import com.communitycart.BackEnd.repository.ProductRepository;
import com.communitycart.BackEnd.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<ProductDTO>> getSellerProducts(){
        List<Product> productList = productRepository.findAll();
        List<ProductDTO> productDTOS = new ArrayList<>();
        for(Product p: productList){
            productDTOS.add(new ModelMapper().map(p, ProductDTO.class));
        }
        return ResponseEntity.ok(productDTOS);
    }

    @GetMapping("/getProducts")
    public ResponseEntity<List<ProductDTO>> getProductsBySellerIdAndCategoryId(@RequestParam(required = false)
                                                                                   Long sellerId,
                                                                               @RequestParam(required = false)
                                                                               Long categoryId){
        List<ProductDTO> productDTOList = productService.getProductsBySellerIdAndCategoryId(sellerId, categoryId);
        if(productDTOList == null){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

    @GetMapping("/getProduct")
    public ResponseEntity<?> getProductByProductId(@RequestParam Long productId){
        ProductDTO productDTO = productService.getProduct(productId);
        if(productDTO == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDTO productDTO){
        ProductDTO res = productService.updateProduct(productDTO);
        if(res == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @DeleteMapping("/deleteProduct")
    public ResponseEntity<?> deleteProduct(@RequestParam Long productId){
        ProductDTO res = productService.deleteProduct(productId);
        if(res == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PostMapping("/postReview")
    public ResponseEntity<?> postReview(@RequestBody ReviewDTO review){
        ReviewDTO rev = productService.postReview(review);
        return new ResponseEntity<>(rev, HttpStatus.OK);
    }

    @GetMapping("/getReviews")
    public ResponseEntity<?> getReviews(@RequestParam Long productId){
        return ResponseEntity.ok(productService.getReviews(productId));
    }

    @GetMapping("/getReview")
    public ResponseEntity<?> getReview(@RequestParam Long reviewId){
        return ResponseEntity.ok(productService.getReview(reviewId));
    }
    @DeleteMapping("/deleteReview")
    public ResponseEntity<?> deleteReview(@RequestParam Long reviewId){
        productService.deleteReview(reviewId);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/canReview")
    public ResponseEntity<?> canReview(@RequestParam Long customerId, @RequestParam Long productId){
        return ResponseEntity.ok(productService.canReview(customerId, productId));
    }

    @PreAuthorize("hasAuthority('SELLER')")
    @PutMapping("/outOfStock")
    public ResponseEntity<?> productOutOfStock(@RequestBody ProductOutOfStock stock){
        System.out.println(stock.isAvailable());
        return ResponseEntity.ok(productService.setOutOfStock(stock));
    }

}
