package com.communitycart.BackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "SellerCategory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SellerCategory {
    @Id
    @SequenceGenerator(name = "sellercategory_sequence",
    allocationSize = 1,
    sequenceName = "sellercategory_sequence")
    @GeneratedValue(generator = "sellercategory_sequence",
    strategy = GenerationType.AUTO)
    private Long id;
    private Long sellerId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoryId", referencedColumnName = "categoryId")
    private Category category;

}
