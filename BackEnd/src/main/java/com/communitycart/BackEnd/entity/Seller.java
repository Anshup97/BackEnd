package com.communitycart.BackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Sellers")
public class Seller {
    @Id
    @SequenceGenerator(
            name = "seller_sequence",
            sequenceName = "seller_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "seller_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long sellerId;
    private String shopName;
    private String name;
    private String email;
    private String contactPhoneNo;
    private String alternatePhoneNo;
    private String upiPhoneNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sellerId", referencedColumnName = "sellerId")
    private Address address;
    private String regNo;
    private String aadharNo;
    private String qrCodeLink;
    private String gstin;

}
