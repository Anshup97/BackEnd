package com.communitycart.BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressDTO {

    private String address1;
    private String address2;
    private String city;
    private String district;
    private String state;
    private Integer pinCode;
    private Double latitude;
    private Double longitude;

}
