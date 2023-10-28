package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.dtos.CategoryDTO;
import com.communitycart.BackEnd.entity.Category;
import com.communitycart.BackEnd.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.matcher.StringMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public ModelMapper mapper(){
        return new ModelMapper();
    }

    public CategoryDTO addCategory(CategoryDTO category){
        String catName = category.getCategoryName();
        catName = catName.substring(0, 1).toUpperCase() + catName.substring(1).toLowerCase();
        Category category1 = categoryRepository.findByCategoryName(catName);
        if(category1 != null){
            return null;
        }
        category.setCategoryName(catName);
        return mapper().map(categoryRepository.save(new ModelMapper().map(category,
                Category.class)), CategoryDTO.class);

    }

    public CategoryDTO deleteCategory(String name){
        Category category = categoryRepository.findByCategoryName(name);
        if(category != null){
            return mapper().map(categoryRepository.deleteByCategoryId(category.getCategoryId()),
                    CategoryDTO.class);

        }
        return null;
    }

    public CategoryDTO updateCategory(CategoryDTO category){
        Category category1 = categoryRepository.findByCategoryName(category.getCategoryName());
        if(category1 == null){
            return null;
        }
        category1.setCategoryName(category.getCategoryName());
        category1.setCategoryDescription(category.getCategoryDescription());
        category1.setCategorySlug(category.getCategorySlug());
        category1.setCatIconUrl(category.getCatIconUrl());
        return mapper().map(categoryRepository.save(category1), CategoryDTO.class);

    }

    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findByCategoryId(categoryId);
    }

    public Category getCategoryByName(String name){
        return categoryRepository.findByCategoryName(name);
    }
}
