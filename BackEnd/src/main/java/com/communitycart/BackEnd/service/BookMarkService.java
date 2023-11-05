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

@Service
public class BookMarkService {

    @Autowired
    private BookMarkRepository repository;

    @Autowired
    private ProductRepository productRepository;

    public ModelMapper mapper(){
        return new ModelMapper();
    }
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

    public ViewBookMarkDTO removeBookmark(Long customerId, Long productId) {
        BookMark bookMark = repository.findBookMarkByCustomerIdAndProductId(customerId, productId);
        if(bookMark != null){
            repository.delete(bookMark);
        }
        return viewBookmarks(customerId);
    }
}
