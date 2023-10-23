package com.communitycart.BackEnd.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FIleStorage {

    public String saveImages(String loc, MultipartFile file, Long productId) throws Exception {
        Path path = Paths.get(loc);
        try{
            String fileName = file.getOriginalFilename();
            String ext = fileName.substring(fileName.indexOf('.'));
            File folder = new File(path.toString());
            File fileToBeDeleted = null;
            for(File fi: folder.listFiles()){
                if(fi.getName().equals(productId + ext)){
                    fileToBeDeleted = fi;
                    break;
                }
            }
            fileToBeDeleted.delete();
            Files.copy(file.getInputStream(), path.resolve(productId.toString() + ext));
            return productId + ext;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }


}
