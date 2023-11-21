package com.communitycart.BackEnd.dtos;

import com.communitycart.BackEnd.entity.Address;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

/**
 * Seller DTO.
 * Used while creating seller, updating seller details and returning seller
 * details to the frontend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SellerDTO {

    private Long sellerId;
    private String email;
    private String name;
    private String contactPhoneNo;
    private String aadharNo;
    private String password;
    private String shopPhotoUrl;
    private String shopName;
    private AddressDTO address;
    private String gstin;

}
