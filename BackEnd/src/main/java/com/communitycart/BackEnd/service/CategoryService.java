package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.dtos.CategoryDTO;
import com.communitycart.BackEnd.entity.Category;
import com.communitycart.BackEnd.entity.Product;
import com.communitycart.BackEnd.repository.CategoryRepository;
import com.communitycart.BackEnd.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.matcher.StringMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;

/**
 * Service to manage categories.
 */
@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    //ModelMapper is used to map Category object (Entity) to CategoryDTO (DTO) object.
    public ModelMapper mapper(){
        return new ModelMapper();
    }

    /**
     * Seller can add a category.
     * If a category is present with the same name, returns null.
     * Else, add a new category.
     * A single category can be used by all sellers. The seller will be shown
     * a list of categories in the UI, if the desired category is not present,
     * the seller can request to add the category.
     * @param category
     * @return
     */
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

    /**
     * Delete a category.
     * Currently, not used.
     * @param categoryId
     * @return
     */
    public CategoryDTO deleteCategory(Long categoryId){
        Category category = categoryRepository.findByCategoryId(categoryId);
        if(category != null){
            List<Product> productList = productRepository.findByCategoryId(categoryId);
            for(Product p: productList){
                productRepository.delete(p);
            }
            categoryRepository.delete(category);
            return mapper().map(category,
                    CategoryDTO.class);
        }
        return null;
    }

    /**
     * Update category details.
     * @param category
     * @return
     */
    public CategoryDTO updateCategory(CategoryDTO category){
        Category category1 = categoryRepository.findByCategoryId(category.getCategoryId());
        if(category1 == null){
            return null;
        }
        category1.setCategoryName(category.getCategoryName());
        category1.setCategoryDescription(category.getCategoryDescription());
        category1.setCategorySlug(category.getCategorySlug());
        category1.setCatIconUrl(category.getCatIconUrl());
        return mapper().map(categoryRepository.save(category1), CategoryDTO.class);

    }

    /**
     * Get list of all categories.
     * @return
     */
    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }

    /**
     * Get category by categoryId.
     * @param categoryId
     * @return
     */
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findByCategoryId(categoryId);
    }

}
