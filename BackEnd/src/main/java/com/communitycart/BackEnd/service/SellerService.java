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

    public CategoryDTO addCategory(String email, CategoryDTO categoryDTO){
        Seller seller = sellerRepository.findByEmail(email);
        Category cat1 = null;
        if(seller != null){
            Set<Category> categories = seller.getCategories();
            if(categories == null){
                categories = new HashSet<>();
            }
            categories.add(getMapper().map(categoryDTO, Category.class));
            seller.setCategories(categories);
            seller = sellerRepository.save(seller);
            for(Category cat: seller.getCategories()){
                if(cat.getCategoryName().equals(categoryDTO.getCategoryName())){
                    cat1 = cat;
                    break;
                }
            }
        }
        return new ModelMapper().map(cat1, CategoryDTO.class);
    }

    public String deleteCategory(Long categoryId){
        return "Category deleted with category ID - " +
                categoryRepository.deleteByCategoryId(categoryId).getCategoryId().toString();
    }

    public Set<Category> getCategoriesBySeller(String email){
        System.out.println("Email - " + email);
        return sellerRepository.findByEmail(email).getCategories();
    }



//    public Seller addProduct(ProductDTO productDTO, Long sellerId){
//        Seller seller = sellerRepository.findById(sellerId).orElse(null);
//        if(seller != null){
//            List<Product> productList = seller.getProducts();
//            productList.add(getMapper().map(productDTO, Product.class));
//            sellerRepository.save(seller);
//        }
//        return seller;
//    }

//    public Seller deleteProduct(Long productId) {
//        sellerRepository.deleteById(productId);
//    }

    public byte[] getProfilePhoto(String email){
        Seller seller = sellerRepository.findByEmail(email);
        if(seller.getProfilePhotoId() != null){
            Optional<ImageData> profilePhoto = imageStorageRepository.findById(seller.getProfilePhotoId());
            if(profilePhoto.isPresent()){
                return imageStorageService.downloadImage(seller.getProfilePhotoId());

            }
        }
        return null;
    }


    public CategoryDTO uploadCategoryImage(Long categoryId, MultipartFile photo) throws IOException {
        ImageData imageData = imageStorageService.uploadImage(photo);
        Category category = categoryRepository.findByCategoryId(categoryId);
        category.setCategoryPhotoId(imageData.getId());
        return new ModelMapper().map(categoryRepository.save(category), CategoryDTO.class);

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
        Product productEntity = new Product();
        productEntity.setProductName(product.getProductName());
        productEntity.setProductPrice(product.getProductPrice());
        productEntity.setProductQuantity(product.getProductQuantity());
        productEntity.setProductDescription(product.getProductDescription());
        productEntity.setProductSlug(product.getProductSlug());
        productEntity.setProductFeatured(product.isProductFeatured());
        productEntity.setCategoryId(product.getCategoryId());
        productEntity.setSellerId(product.getSellerId());
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
        Product product = productRepository.findById(productId).get();
        product.setProductImageUrl(photoId);
        productRepository.save(product);
        return photoId;
    }

    public List<ProductDTO> getProductsBySeller(String email) {
        List<Product> productList = sellerRepository.findByEmail(email).getProducts();
        List<ProductDTO> res = new ArrayList<>();
        for(Product p: productList){
            res.add(new ModelMapper().map(p, ProductDTO.class));
        }
        return res;
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


}
