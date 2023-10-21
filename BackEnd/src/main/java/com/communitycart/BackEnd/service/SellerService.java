package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.dtos.ProductDTO;
import com.communitycart.BackEnd.entity.ImageData;
import com.communitycart.BackEnd.entity.Product;
import com.communitycart.BackEnd.entity.Seller;
import com.communitycart.BackEnd.repository.ImageStorageRepository;
import com.communitycart.BackEnd.repository.SellerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private ImageStorageRepository imageStorageRepository;
    @Autowired
    private ImageStorageService imageStorageService;
    private ModelMapper mapper;
    public Seller addProduct(ProductDTO productDTO, Long sellerId){
        mapper = new ModelMapper();
        Seller seller = sellerRepository.findById(sellerId).orElse(null);
        if(seller != null){
            List<Product> productList = seller.getProducts();
            productList.add(mapper.map(productDTO, Product.class));
            sellerRepository.save(seller);
        }
        return seller;
    }

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
}
