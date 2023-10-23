package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.CategoryDTO;
import com.communitycart.BackEnd.entity.Category;
import com.communitycart.BackEnd.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
//    @PostMapping("/addCategory")
//    @PreAuthorize("hasAuthority('SELLER')")
//    public ResponseEntity<Category> addCategory(@RequestBody CategoryDTO categoryDTO){
//        return new ResponseEntity<>(categoryService.addCategory(categoryDTO),
//                HttpStatus.CREATED);
//    }

    @DeleteMapping("/deleteCategory/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        Category category = categoryService.deleteCategory(categoryId);
        if(category == null){
            return new ResponseEntity<>("Category to be deleted not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Category deleted - " + category.getCategoryId(), HttpStatus.OK);
    }

    @PutMapping("/updateCategory")
    public ResponseEntity<String> updateCategory(CategoryDTO categoryDTO){
        Category category = categoryService.updateCategory(categoryDTO);
        if(category == null){
            return new ResponseEntity<>("Category to be updated Not Found!", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Category updated.", HttpStatus.OK);
    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<List<Category>> getCategories(){
        return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
    }
    @GetMapping("/getCategoryById/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId){
        Category category = categoryService.getCategoryById(categoryId);
        if(category != null){
            return new ResponseEntity<>(category, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }



}
