package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.CategoryDTO;
import com.communitycart.BackEnd.dtos.ProductDTO;
import com.communitycart.BackEnd.dtos.SellerDTO;
import com.communitycart.BackEnd.entity.Category;
import com.communitycart.BackEnd.entity.Product;
import com.communitycart.BackEnd.entity.Seller;
import com.communitycart.BackEnd.entity.User;
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
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    //Add categories
    //Add products
    //Manage Orders
    //Update Seller Details

    @Autowired
    private FIleStorage fIleStorage;
//    @PostMapping("/saveShopImages")
//    public ResponseEntity<String> saveShopImages(@RequestPart MultipartFile file) throws Exception {
//        return ResponseEntity.ok(fIleStorage.saveShopImages(file));
//    }

    @GetMapping("/getAllSellers")
    public ResponseEntity<List<SellerDTO>> getAllSellers(){
        return ResponseEntity.ok(sellerService.getAllSellers());
    }

    @PostMapping("/addProduct")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO){
        return ResponseEntity.ok(sellerService.addProduct(productDTO));
    }

    @PostMapping(value = "/uploadImage/product/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProductImage(@PathVariable("productId") Long productId,
                                                     @RequestPart("productImage") MultipartFile productImage) throws Exception {
        return ResponseEntity.ok(sellerService.uploadProductImage(productId, productImage));
    }

//    @PostMapping("/deleteProduct")
//    public ResponseEntity<Seller> deleteProduct(@RequestBody Long productId){
//        Seller seller = sellerService.deleteProduct(productId);
//        return ResponseEntity.ok(seller);
//    }

    @PostMapping("/updateProduct")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO){
        return ResponseEntity.ok(sellerService.updateProduct(productDTO));
    }

    @PostMapping("/addCategory/{email}")
    public ResponseEntity<CategoryDTO> addCategory(@PathVariable("email") String email, @RequestBody CategoryDTO category){
        CategoryDTO categoryDTO1 = sellerService.addCategory(email, category);
        if(categoryDTO1 != null){
            return ResponseEntity.ok(categoryDTO1);
        }
        return new ResponseEntity<>(categoryDTO1, HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/uploadPhoto/category/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryDTO> uploadCategoryImage(@PathVariable("categoryId") Long categoryId,
                                                           @RequestPart("photo") MultipartFile photo) throws IOException {
        return ResponseEntity.ok(sellerService.uploadCategoryImage(categoryId, photo));
    }

    @GetMapping("/getAllCategories/{email}")
    public ResponseEntity<Set<Category>> getSellerCategories(@PathVariable("email") String email){
        return ResponseEntity.ok(sellerService.getCategoriesBySeller(email));
    }

    @GetMapping("/getCategoryPhoto/{categoryId}")
    public ResponseEntity<?> getCategoryPhoto(@PathVariable(name = "categoryId") Long categoryId){
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(sellerService.getCategoryPhoto(categoryId));
    }

    @GetMapping("/getSellerProducts/{email}")
    public ResponseEntity<List<ProductDTO>> getSellerProducts(@PathVariable("email") String email){
        return ResponseEntity.ok(sellerService.getProductsBySeller(email));
    }

}
