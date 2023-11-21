package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.dtos.*;
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

/**
 * Product service class for serving various product
 * functionalities.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderService orderService;

    /**
     * Get list of all products.
     * @return
     */
    public List<ProductDTO> getAllProducts(){
        List<Product> productList = productRepository.findAll();
        List<ProductDTO> productDTOS = new ArrayList<>();
        for(Product p: productList){
            productDTOS.add(new ModelMapper().map(p, ProductDTO.class));
        }
        return productDTOS;
    }

    /**
     * Get a filtered list of products by sellerId and categoryId.
     * @param sellerId
     * @param categoryId
     * @return
     */
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

    /**
     * Get product by productId
     * @param productId
     * @return
     */
    public ProductDTO getProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        return product.map(value -> new ModelMapper().map(value, ProductDTO.class)).orElse(null);
    }

    /**
     * Update product details.
     * @param productDTO
     * @return
     */
    public ProductDTO updateProduct(ProductDTO productDTO){
        Optional<Product> product = productRepository.findById(productDTO.getProductId());
        if(product.isEmpty()){
            return null;
        }
        productRepository.save(new ModelMapper().map(productDTO, Product.class));
        return productDTO;
    }

    /**
     * Delete product
     * @param productId
     * @return
     */
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

    /**
     * Update rating of a product.
     * When the user gives a review and rating, this method is called
     * to update the rating of the product.
     * @param productId
     */
    public void updateRating(Long productId){
        Product product = productRepository.findProductByProductId(productId);
        if(product != null){
            List<Review> reviews = reviewRepository.findByProductId(productId);
            if(!reviews.isEmpty()){
                Integer ratings = 0;
                for(Review r: reviews){
                    ratings += r.getRating();
                }
                Double productRating = (double) (ratings / reviews.size());
                product.setRating(productRating);
            } else {
                product.setRating(0D);
            }
            productRepository.save(product);
        }
    }

    /**
     * Returns if a customer can review a product.
     * Returns true if a customer has purchased the product
     * else returns false.
     * @param customerId
     * @param productId
     * @return
     */
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

    /**
     * Post a review for a product.
     * If a customer has already reviewed a product, then the old review
     * will be updated.
     * @param review
     * @return
     */
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
        updateRating(review.getProductId());
        return dto;
    }

    /**
     * Get list of reviews of a product.
     * @param productId
     * @return
     */
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

    //Get review by reviewId.
    public ReviewDTO getReview(Long reviewId){
        Review review = reviewRepository.findByReviewId(reviewId);
        if(review == null){
            return null;
        }
        return new ModelMapper().map(review, ReviewDTO.class);
    }

    //Delete a review.
    public void deleteReview(Long reviewId){
        Review review = reviewRepository.findByReviewId(reviewId);
        Long productId = review.getProductId();
        reviewRepository.delete(review);
        updateRating(productId);
    }

    /**
     * Set product out of stock.
     * Can be used by seller only.
     * @param stock
     * @return
     */
    public ProductDTO setOutOfStock(ProductOutOfStock stock) {
        Product product = productRepository.findProductByProductId(stock.getProductId());
        if(product == null || !product.getSellerId().equals(stock.getSellerId())){
            return null;
        }
        product.setAvailable(stock.isAvailable());
        product.setProductQuantity(0L);
        return new ModelMapper().map(productRepository.save(product), ProductDTO.class);
    }
}
