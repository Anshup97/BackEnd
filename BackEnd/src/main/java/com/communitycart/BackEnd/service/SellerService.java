package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.dtos.CategoryDTO;
import com.communitycart.BackEnd.dtos.ProductDTO;
import com.communitycart.BackEnd.dtos.SellerDTO;
import com.communitycart.BackEnd.entity.Category;
import com.communitycart.BackEnd.entity.ImageData;
import com.communitycart.BackEnd.entity.Product;
import com.communitycart.BackEnd.entity.Seller;
import com.communitycart.BackEnd.repository.CategoryRepository;
import com.communitycart.BackEnd.repository.ImageStorageRepository;
import com.communitycart.BackEnd.repository.ProductRepository;
import com.communitycart.BackEnd.repository.SellerRepository;
import com.communitycart.BackEnd.utils.CalculateDistance;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.matcher.StringMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageStorageRepository imageStorageRepository;

    @Autowired
    private ImageStorageService imageStorageService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FIleStorage fIleStorage;

    public ModelMapper getMapper(){
        return new ModelMapper();
    }

    public List<CategoryDTO> getCategoriesBySeller(Long sellerId){
        Optional<Seller> seller = sellerRepository.findById(sellerId);
        if(seller.isEmpty()){
            return null;
        }
        List<Product> productList = seller.get().getProducts();
        Set<Long> st = new HashSet<>();
        List<Category> categoryList = new ArrayList<>();
        for(Product pr: productList){
            st.add(pr.getCategoryId());
        }
        for(Long i: st){
            categoryList.add(categoryRepository.findByCategoryId(i));
        }
        return categoryList.stream()
                .map(c -> new ModelMapper().map(c, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    public Seller deleteProduct(Long productId) {
        sellerRepository.deleteById(productId);
        return new Seller();
    }

    public byte[] getCategoryPhoto(Long categoryId) {


            Optional<ImageData> profilePhoto = imageStorageRepository.findById(categoryId);
            if(profilePhoto.isPresent()){
                return imageStorageService.downloadImage(categoryId);

            }

        return null;
    }

    public ProductDTO addProduct(ProductDTO product){
        Seller seller = sellerRepository.findById(product.getSellerId()).get();
        Product productEntity = new ModelMapper().map(product, Product.class);
        List<Product> productList = seller.getProducts();
        if(productList == null){
            productList = new ArrayList<>();
        }
        productList.add(productEntity);
        seller.setProducts(productList);
        productList = sellerRepository.save(seller).getProducts();
        return new ModelMapper().map(productList.get(productList.size()-1), ProductDTO.class);

    }

    public String uploadProductImage(Long productId, MultipartFile productImage) throws Exception {
        String photoId = fIleStorage.saveImages("images/product", productImage, productId);
        Optional<Product> product = productRepository.findById(productId);
        if(!product.isPresent()){
            return "-1";
        }
        product.get().setProductImageUrl("http://172.17.84.65:8080/images/product/" + photoId);
        productRepository.save(product.get());
        return photoId;
    }

    public List<ProductDTO> getProductsBySeller(String email, Long categoryId) {
        List<Product> productList = sellerRepository.findByEmail(email).getProducts();
        List<ProductDTO> res = new ArrayList<>();
        for(Product p: productList){
            res.add(new ModelMapper().map(p, ProductDTO.class));
        }
        if(categoryId == -1){
            return res;
        } else {
            Category category = categoryRepository.findByCategoryId(categoryId);
            if(category == null){
                return null;
            }
            res = res.stream()
                    .filter(x -> x.getCategoryId().equals(categoryId))
                    .collect(Collectors.toList());
            return res;
        }
    }

    public ProductDTO updateProduct(ProductDTO productDTO){
        Product productEntity = productRepository.findById(productDTO.getProductId()).get();
        productEntity.setProductName(productDTO.getProductName());
        productEntity.setProductPrice(productDTO.getProductPrice());
        productEntity.setProductQuantity(productDTO.getProductQuantity());
        productEntity.setProductDescription(productDTO.getProductDescription());
        productEntity.setProductSlug(productDTO.getProductSlug());
        productEntity.setProductFeatured(productDTO.isProductFeatured());
        return new ModelMapper().map(productRepository.save(productEntity), ProductDTO.class);
    }

    public List<SellerDTO> getAllSellers(){
        List<Seller> sellers = sellerRepository.findAll();
        List<SellerDTO> sellerDTOS = new ArrayList<>();
        for(Seller seller: sellers){
            sellerDTOS.add(new ModelMapper().map(seller, SellerDTO.class));
        }
        return sellerDTOS;
    }

    public List<SellerDTO> getNearbySellers(Double lat, Double longi, Double el){
        List<Seller> sellers = sellerRepository.findAll();
        sellers = sellers.stream()
                .filter(s -> CalculateDistance.distance(lat, longi,s.getAddress().getLatitude(),
                        s.getAddress().getLongitude(), el, s.getAddress().getElevation()) <= 10)
                .collect(Collectors.toList());
        return sellers.stream()
                .map(s -> new ModelMapper().map(s, SellerDTO.class))
                .toList();
    }


    public List<SellerDTO> getNearbySellersByCategory(Long categoryId) {
        List<Seller> sellers = sellerRepository.findAll();
        Set<Seller> res = new HashSet<>();
        for(Seller seller: sellers){
            List<Product> products = seller.getProducts();
            for(Product p: products){
                if(p.getCategoryId().equals(categoryId)){
                    res.add(seller);
                }
            }
        }
        List<Seller> sellerList = res.stream().toList();
        return sellerList.stream()
                .map(s -> new ModelMapper().map(s, SellerDTO.class))
                .collect(Collectors.toList());
    }
}
