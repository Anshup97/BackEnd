package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.dtos.CategoryDTO;
import com.communitycart.BackEnd.entity.Category;
import com.communitycart.BackEnd.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public Category addCategory(CategoryDTO category){
        Category category1= categoryRepository.findByCategoryName(category.getCategoryName());
        category1 = new Category();
        category1.setCategoryName(category.getCategoryName());
        category1.setCategoryDescription(category.getCategoryDescription());
        category1.setCategoryImage(category.getCategoryImage());
        category1.setCategorySlug(category.getCategorySlug());
        if(categoryRepository.findByCategoryName(category.getCategoryName()) == null){
            return categoryRepository.save(category1);
        } else {
            return categoryRepository.updateCategoryByCategoryName(category.getCategoryName(), category1);
        }
    }

    public String deleteCategory(Long categoryId){
        if(categoryRepository.findByCategoryId(categoryId) != null){
            categoryRepository.deleteByCategoryId(categoryId);
            return "Category deleted with ID - " + categoryId.toString();
        }
        return "Category Not Found.";
    }

    public Category updateCategory(CategoryDTO category){
       return addCategory(category);

    }
}
