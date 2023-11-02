package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.CategoryDTO;
import com.communitycart.BackEnd.entity.Category;
import com.communitycart.BackEnd.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping("/addCategory")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO){
        CategoryDTO category = categoryService.addCategory(categoryDTO);
        if(category == null){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteCategory")
    @PreAuthorize("hasAuthority('SELLER')")
    @Transactional
    public ResponseEntity<?> deleteCategory(@RequestParam Long categoryId){
        CategoryDTO category = categoryService.deleteCategory(categoryId);
        if(category == null){
            return new ResponseEntity<>("Category to be deleted not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PutMapping("/updateCategory")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDTO categoryDTO){
        CategoryDTO category = categoryService.updateCategory(categoryDTO);
        if(category == null){
            return new ResponseEntity<>("Category to be updated Not Found!", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<List<Category>> getCategories(){
        return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
    }
    @GetMapping("/getCategoryById")
    public ResponseEntity<Category> getCategoryById(@RequestParam("categoryId") Long categoryId){
        Category category = categoryService.getCategoryById(categoryId);
        if(category != null){
            return new ResponseEntity<>(category, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }



}
