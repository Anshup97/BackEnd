package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.CreateBookmarkDTO;
import com.communitycart.BackEnd.dtos.ViewBookMarkDTO;
import com.communitycart.BackEnd.entity.BookMark;
import com.communitycart.BackEnd.service.BookMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API class for bookmarks.
 * Manages bookmarks for a product.
 * Customer can bookmark a product.
 */
@RestController
@CrossOrigin("*")
public class BookMarkController {

    @Autowired
    private BookMarkService service;

    /*
    Bookmark a product.
     */
    @PostMapping("/addBookmark")
    public ResponseEntity<?> addBookMark(@RequestParam Long customerId, @RequestParam Long productId){
        ViewBookMarkDTO createBookmarkDTO = service.addBookmark(customerId, productId);
        if(createBookmarkDTO == null){
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(createBookmarkDTO);
    }

    /*
    Get all the bookmarked products of a customer.
     */
    @GetMapping("/viewBookmarks")
    public ResponseEntity<?> viewBookmarks(@RequestParam Long customerId){
        return ResponseEntity.ok(service.viewBookmarks(customerId));
    }

    /*
    Remove bookmark for a product.
     */
    @DeleteMapping("/removeBookmark")
    public ResponseEntity<?> removeBookmark(@RequestParam Long customerId, @RequestParam Long productId){
        return ResponseEntity.ok(service.removeBookmark(customerId, productId));
    }


}
