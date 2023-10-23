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

    private Long categoryId;
    private String categoryName;
    private String categoryDescription;
    private Long categoryPhotoId;
    private String categorySlug;
    private String catIconUrl;

}
