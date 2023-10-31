package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.ProductDTO;
import com.communitycart.BackEnd.entity.Product;
import com.communitycart.BackEnd.repository.ProductRepository;
import com.communitycart.BackEnd.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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



    @DeleteMapping("/deleteProduct")
    public ResponseEntity<String> deleteProduct(@RequestParam("productId") Long productId){
        if(productId != null && productRepository.findById(productId).isPresent()){
            productRepository.deleteById(productId);
            return ResponseEntity.ok("Product deleted with ID - " + productId);
        }
        return new ResponseEntity<>("Product to be deleted not found.", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getProducts")
    public ResponseEntity<List<ProductDTO>> getProductsBySellerIdAndCategoryId(@RequestParam(required = false)
                                                                                   Long sellerId,
                                                                               @RequestParam(required = false)
                                                                               Long categoryId){
        List<ProductDTO> productDTOList = productService.getProductsBySellerIdAndCategoryId(sellerId, categoryId);
        if(productDTOList == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

    @GetMapping("/getProduct")
    public ResponseEntity<?> getProductByProductId(@RequestParam Long productId){
        ProductDTO productDTO = productService.getProduct(productId);
        if(productDTO == null){
            return new ResponseEntity<>("Product not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productDTO, HttpStatus.FOUND);
    }


}
