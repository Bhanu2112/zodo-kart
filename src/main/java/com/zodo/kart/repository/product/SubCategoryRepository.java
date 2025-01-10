package com.zodo.kart.repository.product;

import com.zodo.kart.entity.product.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    List<SubCategory> findByCategoryCategoryId(Long categoryId);
}
