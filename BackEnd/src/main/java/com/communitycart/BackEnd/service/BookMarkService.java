package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.dtos.ProductDTO;
import com.communitycart.BackEnd.dtos.ViewBookMarkDTO;
import com.communitycart.BackEnd.entity.BookMark;
import com.communitycart.BackEnd.entity.Product;
import com.communitycart.BackEnd.repository.BookMarkRepository;
import com.communitycart.BackEnd.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bookmark service class to add, remove and view bookmarked products
 * of a user.
 */
@Service
public class BookMarkService {

    @Autowired
    private BookMarkRepository repository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Bookmark a product.
     * Product bookmarked by a customer is saved in the database.
     * @param customerId
     * @param productId
     * @return
     */
    public ViewBookMarkDTO addBookmark(Long customerId, Long productId) {
        if(repository.findBookMarkByCustomerIdAndProductId(customerId, productId) != null){
            return viewBookmarks(customerId);
        }
        BookMark bookMark = new BookMark();
        bookMark.setCustomerId(customerId);
        bookMark.setProductId(productId);
        repository.save(bookMark);
        return viewBookmarks(customerId);
    }

    /**
     * Get all the products bookmarked by a customer using customerId.
     * @param customerId
     * @return
     */
    public ViewBookMarkDTO viewBookmarks(Long customerId) {
        List<BookMark> bookMarks = repository.findByCustomerId(customerId);
        ViewBookMarkDTO bm = new ViewBookMarkDTO();
        List<Product> products = new ArrayList<>();
        for(BookMark b: bookMarks){
            Product p = productRepository.findProductByProductId(b.getProductId());
            if(p != null){
                products.add(p);
            }
        }
        bm.setCustomerId(customerId);
        bm.setProducts(products.stream()
                .map(x -> new ModelMapper().map(x, ProductDTO.class))
                .collect(Collectors.toList()));
        return bm;
    }

    /**
     * Remove bookmark.
     * @param customerId
     * @param productId
     * @return
     */
    public ViewBookMarkDTO removeBookmark(Long customerId, Long productId) {
        BookMark bookMark = repository.findBookMarkByCustomerIdAndProductId(customerId, productId);
        if(bookMark != null){
            repository.delete(bookMark);
        }
        return viewBookmarks(customerId);
    }
}
