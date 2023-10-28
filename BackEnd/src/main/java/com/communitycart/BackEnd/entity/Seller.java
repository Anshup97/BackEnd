package com.communitycart.BackEnd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Set;

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
    private String name;
    private String email;
    private String contactPhoneNo;
    private String aadharNo;
    private String shopPhotoUrl;

    private String shopName;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId", referencedColumnName = "addressId")
    private Address address;
    private String gstin;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sellerId", referencedColumnName = "sellerId")
    private List<Product> products;


}
