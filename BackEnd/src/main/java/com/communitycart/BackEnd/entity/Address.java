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
@Table(name = "Address")
public class Address {
    @Id
    @SequenceGenerator(
            name = "address_sequence",
            sequenceName = "address_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "address_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long addressId;
    private String address1;
    private String address2;
    private String city;
    private String district;
    private String state;
    private String pinCode;
    private Double latitude;
    private Double longitude;

    @OneToOne(mappedBy = "address")
    private Seller seller;

    @OneToOne(mappedBy = "address")
    private Customer customer;
}
