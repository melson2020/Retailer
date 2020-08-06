package com.melson.webserver.dao;

import com.melson.webserver.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Messi on 2020/8/6
 */
@Repository
public interface IProductCategoryDao extends JpaRepository<ProductCategory,String> {

}
