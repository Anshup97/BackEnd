package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.dtos.CategoryDTO;
import com.communitycart.BackEnd.entity.Category;
import com.communitycart.BackEnd.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public Category addCategory(CategoryDTO category){
        Category category1= categoryRepository.findByCategoryName(category.getCategoryName());
        category1 = new Category();
        category1.setCategoryName(category.getCategoryName());
        category1.setCategoryDescription(category.getCategoryDescription());

        category1.setCategorySlug(category.getCategorySlug());
        return categoryRepository.save(category1);

    }

    public Category deleteCategory(Long categoryId){
        if(categoryRepository.findByCategoryId(categoryId) != null){
            return categoryRepository.deleteByCategoryId(categoryId);

        }
        return null;
    }

    public Category updateCategory(CategoryDTO category){
        if(categoryRepository.findByCategoryName(category.getCategoryName()) == null){
            return null;
        }
       return addCategory(category);

    }

    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findByCategoryId(categoryId);
    }
}
