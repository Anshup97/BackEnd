package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.entity.ImageData;
import com.communitycart.BackEnd.repository.ImageStorageRepository;
import com.communitycart.BackEnd.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageStorageService {

    @Autowired
    private ImageStorageRepository repository;

    public ImageData uploadImage(MultipartFile file) throws IOException {

        ImageData imageData = repository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes())).build());
        return imageData;
    }

    public byte[] downloadImage(Long id){
        Optional<ImageData> dbImageData = repository.findById(id);
        byte[] images=ImageUtils.decompressImage(dbImageData.get().getImageData());
        return images;
    }
}
