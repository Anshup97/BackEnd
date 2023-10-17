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
public class CustomerDTO {

    private String name;
    private String emailId;
    private String phoneNo;
    private String alternatePhoneNo;
    private List<AddressDTO> address;
    private String password;

}
