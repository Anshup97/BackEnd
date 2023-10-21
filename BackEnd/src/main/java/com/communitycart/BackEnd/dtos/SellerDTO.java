package com.communitycart.BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SellerDTO {

    private String name;
    private String email;
    private String contactPhoneNo;
    private String aadharNo;
    private String password;
    private MultipartFile profilePhoto;
//    private ShopDTO shop;

}
