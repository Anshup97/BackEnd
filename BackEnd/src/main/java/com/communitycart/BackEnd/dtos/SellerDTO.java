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
    private String contactPhoneNo;
    private String alternatePhoneNo;
    private String aadharNo;
    private String password;
    private Shop shop;

}
