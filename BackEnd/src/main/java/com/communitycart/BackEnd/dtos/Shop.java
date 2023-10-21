package com.communitycart.BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Shop {

    private String shopName;
    private List<String> shopImages;
    private String upiPhoneNumber;
    private AddressDTO address;
    private String regNo;
    private String qrCodeLink;
    private String gstin;
    private List<Product> products;

}
