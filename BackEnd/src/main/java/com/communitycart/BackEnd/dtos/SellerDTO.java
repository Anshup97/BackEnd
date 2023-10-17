package com.communitycart.BackEnd.dtos;

import com.communitycart.BackEnd.entity.Address;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SellerDTO {

    private String name;
    private String email;
    private String shopName;
    private String contactPhoneNo;
    private String alternatePhoneNo;
    private String upiPhoneNumber;
    private AddressDTO address;
    private String regNo;
    private String aadharNo;
    private String qrCodeLink;
    private String gstin;
}
