package com.communitycart.BackEnd.repository;

import com.communitycart.BackEnd.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    public Category findByCategoryName(String categoryName);
    public Category updateCategoryByCategoryName(String categoryName, Category category);
    public Category findByCategoryId(Long categoryId);
    public void deleteByCategoryId(Long categoryId);

}
