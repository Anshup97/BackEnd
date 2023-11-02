package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.dtos.ProductDTO;
import com.communitycart.BackEnd.entity.Product;
import com.communitycart.BackEnd.entity.Seller;
import com.communitycart.BackEnd.repository.ProductRepository;
import com.communitycart.BackEnd.repository.SellerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    public List<ProductDTO> getAllProducts(){
        List<Product> productList = productRepository.findAll();
        List<ProductDTO> productDTOS = new ArrayList<>();
        for(Product p: productList){
            productDTOS.add(new ModelMapper().map(p, ProductDTO.class));
        }
        return productDTOS;
    }

    public List<ProductDTO> getProductsBySellerIdAndCategoryId(Long sellerId, Long categoryId){
        if(sellerId == null){
            if(categoryId != null){
                return getAllProducts().stream()
                        .filter(p -> p.getCategoryId().equals(categoryId))
                        .collect(Collectors.toList());
            }
            return getAllProducts();
        }
        Optional<Seller> seller = sellerRepository.findById(sellerId);
        if(seller.isPresent()){
            List<Product> productList = seller.get().getProducts();
            if(productList == null){
                return null;
            }
            if(categoryId != null){
                productList = productList.stream()
                        .filter(p -> p.getCategoryId().equals(categoryId))
                        .collect(Collectors.toList());
            }
            List<ProductDTO> res = new ArrayList<>();
            for(Product pr: productList){
                res.add(new ModelMapper().map(pr, ProductDTO.class));
            }
            return res;

        }
        return null;
    }

    public ProductDTO getProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        return product.map(value -> new ModelMapper().map(value, ProductDTO.class)).orElse(null);
    }

    public ProductDTO updateProduct(ProductDTO productDTO){
        Optional<Product> product = productRepository.findById(productDTO.getProductId());
        if(product.isEmpty()){
            return null;
        }
        productRepository.save(new ModelMapper().map(productDTO, Product.class));
        return productDTO;
    }

    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findProductByProductId(productId);
        if(product == null){
            return null;
        }
        ProductDTO productDTO = new ModelMapper().map(product, ProductDTO.class);
        productRepository.delete(product);
        return productDTO;
    }
}
