package com.communitycart.BackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Customers")
public class Customer {
    @Id
    @SequenceGenerator(
            sequenceName = "customer_sequence",
            name = "customer_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_sequence"
    )
    private Long customerId;
    private String name;
    private String emailId;
    private String phoneNo;
    private String alternatePhoneNo;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "customerId")
    private List<Address> addresses;
}
