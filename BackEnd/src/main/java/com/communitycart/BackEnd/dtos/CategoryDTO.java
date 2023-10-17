package com.communitycart.BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryDTO {

    private String categoryName;
    private String categoryDescription;
    private String categoryImage;
    private String categorySlug;

}
