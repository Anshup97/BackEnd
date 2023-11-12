package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.dtos.OrderDTO;
import com.communitycart.BackEnd.dtos.OrderItemDTO;
import com.communitycart.BackEnd.dtos.ProductDTO;
import com.communitycart.BackEnd.dtos.ReviewDTO;
import com.communitycart.BackEnd.entity.Product;
import com.communitycart.BackEnd.entity.Review;
import com.communitycart.BackEnd.entity.Seller;
import com.communitycart.BackEnd.repository.CartItemRepository;
import com.communitycart.BackEnd.repository.ProductRepository;
import com.communitycart.BackEnd.repository.ReviewRepository;
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

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderService orderService;

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
        System.out.println("Product --> " + product);
        if(product == null){
            return null;
        }
        ProductDTO productDTO = new ModelMapper().map(product, ProductDTO.class);
        productRepository.delete(product);
        return productDTO;
    }

    public boolean canReview(Long customerId, Long productId){
        List<OrderDTO> orders = orderService.getOrders(customerId, null);
        for(OrderDTO o: orders){
            List<OrderItemDTO> items = o.getItems();
            for(OrderItemDTO item: items){
                if(productId.equals(item.getProduct().getProductId())){
                    return true;
                }
            }
        }
        return false;
    }

    public ReviewDTO postReview(ReviewDTO review) {
        Product product = productRepository.findProductByProductId(review.getProductId());
        if(product == null || !canReview(review.getCustomerId(), review.getProductId())){
            return null;
        }
        ReviewDTO dto = null;
        List<Review> oldReview = reviewRepository.findByProductIdAndCustomerId(review.getProductId(),
                review.getCustomerId());
        if(!oldReview.isEmpty()){
            Review reviewOld = oldReview.get(0);
            reviewOld.setReview(review.getReview());
            reviewOld.setRating(review.getRating());
            dto = new ModelMapper().map(reviewRepository.save(reviewOld), ReviewDTO.class);
        } else {
            dto = new ModelMapper().map(reviewRepository.save(new ModelMapper().map(review, Review.class)),
                    ReviewDTO.class);
        }
        List<Review> reviews = reviewRepository.findByProductId(review.getProductId());
        Integer ratings = 0;
        for(Review r: reviews){
            ratings += r.getRating();
        }
        Double productRating = (double) (ratings / reviews.size());
        product.setRating(productRating);
        productRepository.save(product);
        return dto;
    }

    public List<ReviewDTO> getReviews(Long productId){
        Product product = productRepository.findProductByProductId(productId);
        if(product == null){
            return null;
        }
        List<Review> reviews = reviewRepository.findByProductId(productId);
        if(reviews.isEmpty()){
            return null;
        }
        return reviews.stream()
                .map(r -> new ModelMapper().map(r, ReviewDTO.class))
                .collect(Collectors.toList());
    }

    public ReviewDTO getReview(Long reviewId){
        Review review = reviewRepository.findByReviewId(reviewId);
        if(review == null){
            return null;
        }
        return new ModelMapper().map(review, ReviewDTO.class);
    }

    public void deleteReview(Long reviewId){
        Review review = reviewRepository.findByReviewId(reviewId);
        reviewRepository.delete(review);
    }
}
