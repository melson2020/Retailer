package com.melson.webserver.dao;

import com.melson.webserver.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Messi on 2020/8/6
 */
@Repository
public interface IProductCategoryDao extends JpaRepository<ProductCategory,String> {

    ProductCategory findByCategoryId(String categoryId);

    List<ProductCategory> findAllByStoreCode(String storeCode);

    @Modifying
    @Query(value = "DELETE from product_category WHERE id=?1",nativeQuery = true)
    Integer deleteByCategoryId(Integer id);
}
