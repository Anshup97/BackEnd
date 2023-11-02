package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.CategoryDTO;
import com.communitycart.BackEnd.dtos.ProductDTO;
import com.communitycart.BackEnd.dtos.SellerDTO;
import com.communitycart.BackEnd.entity.Category;
import com.communitycart.BackEnd.entity.Product;
import com.communitycart.BackEnd.entity.Seller;
import com.communitycart.BackEnd.entity.User;
import com.communitycart.BackEnd.service.CategoryService;
import com.communitycart.BackEnd.service.FIleStorage;
import com.communitycart.BackEnd.service.SellerService;
import com.communitycart.BackEnd.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FIleStorage fIleStorage;

    @GetMapping("/getAllSellers")
    public ResponseEntity<List<SellerDTO>> getAllSellers(){
        return ResponseEntity.ok(sellerService.getAllSellers());
    }

    @PostMapping("/addProduct")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO){
        return new ResponseEntity<>(sellerService.addProduct(productDTO), HttpStatus.CREATED);
    }

    @PostMapping(value = "/uploadImage/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProductImage(@RequestParam("productId") Long productId,
                                                     @RequestPart("productImage") MultipartFile productImage) throws Exception {
        String isUploaded = sellerService.uploadProductImage(productId, productImage);
        if(isUploaded.equals("-1")){
            return new ResponseEntity<>(new ArrayList<>(),
                    HttpStatus.OK);
        }
        return ResponseEntity.ok(isUploaded);
    }

    @PostMapping("/updateProduct")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO){
        return ResponseEntity.ok(sellerService.updateProduct(productDTO));
    }

    @PostMapping("/addCategory/{email}")
    public ResponseEntity<CategoryDTO> addCategory(@PathVariable("email") String email, @RequestBody CategoryDTO category){
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/getSellerCategories")
    public ResponseEntity<?> getSellerCategories(@RequestParam(value = "sellerId", required = false) Long sellerId){
        if(sellerId == null){
            return new ResponseEntity<>(categoryService.getCategories().stream()
                    .map(c -> new ModelMapper().map(c, CategoryDTO.class))
                    .collect(Collectors.toList()), HttpStatus.OK);
        }
        List<CategoryDTO> categoryDTOS = sellerService.getCategoriesBySeller(sellerId);
        if(categoryDTOS == null || categoryDTOS.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        return ResponseEntity.ok(sellerService.getCategoriesBySeller(sellerId));
    }

    @GetMapping("/getCategoryPhoto/{categoryId}")
    public ResponseEntity<?> getCategoryPhoto(@PathVariable(name = "categoryId") Long categoryId){
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(sellerService.getCategoryPhoto(categoryId));
    }

    @GetMapping("/getProducts/{email}/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getSellerProducts(@PathVariable("email") String email,
                                                              @PathVariable("categoryId") Long categoryId){
        List<ProductDTO> productDTOS = sellerService.getProductsBySeller(email, categoryId);
        if(productDTOS == null){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }

    @GetMapping("/getNearbySellers")
    public ResponseEntity<?> getNearbySellers(@RequestParam Double sourceLat, @RequestParam Double sourceLng,
                                              @RequestParam Double elevation,
                                              @RequestParam(required = false) Long categoryId){
        List<SellerDTO> sellerDTOS = sellerService.getNearbySellers(sourceLat, sourceLng, elevation);
        if(sellerDTOS.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        if(categoryId == null){
            return new ResponseEntity<>(sellerDTOS, HttpStatus.OK);
        }
        sellerDTOS = sellerService.getNearbySellersByCategory(categoryId);
        if(sellerDTOS.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        return ResponseEntity.ok(sellerDTOS);
    }

}
