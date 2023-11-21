package com.communitycart.BackEnd.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Used for uploading customer & seller profile photos
 * and product images.
 */
@Service
public class FileStorageService {

    //Save product images in local webapps folder.
    public String saveImages(String loc, MultipartFile file, Long productId) throws Exception {
        Path path = Paths.get("C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\images\\product");
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
            if(fileToBeDeleted != null){
                fileToBeDeleted.delete();
            }
            Files.copy(file.getInputStream(), path.resolve(productId.toString() + ext));
            return productId + ext;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    //Save seller profile photo
    public String saveSellerPhoto(MultipartFile file, Long sellerId) throws Exception {
        Path path = Paths.get("C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\images\\shop");
        try{
            String fileName = file.getOriginalFilename();
            String ext = fileName.substring(fileName.indexOf('.'));
            File folder = new File(path.toString());
            File fileToBeDeleted = null;
            for(File fi: folder.listFiles()){
                if(fi.getName().equals(sellerId + ext)){
                    fileToBeDeleted = fi;
                    break;
                }
            }
            if(fileToBeDeleted != null){
                fileToBeDeleted.delete();
            }
            Files.copy(file.getInputStream(), path.resolve(sellerId.toString() + ext));
            return sellerId + ext;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    //Save customer photo.
    public String saveCustomerPhoto(MultipartFile file, Long customerId) throws Exception {
        Path path = Paths.get("C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\images\\customers");
        try{
            String fileName = file.getOriginalFilename();
            String ext = fileName.substring(fileName.indexOf('.'));
            File folder = new File(path.toString());
            File fileToBeDeleted = null;
            for(File fi: folder.listFiles()){
                if(fi.getName().equals(customerId + ext)){
                    fileToBeDeleted = fi;
                    break;
                }
            }
            if(fileToBeDeleted != null){
                fileToBeDeleted.delete();
            }
            Files.copy(file.getInputStream(), path.resolve(customerId.toString() + ext));
            return customerId + ext;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }


}
