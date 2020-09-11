package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.IProductCategoryDao;
import com.melson.webserver.dao.IProductDao;
import com.melson.webserver.dto.ProductImportDto;
import com.melson.webserver.entity.Product;
import com.melson.webserver.entity.ProductCategory;
import com.melson.webserver.service.IProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/25
 */
@Service
public class ProductImpl extends AbstractService<Product> implements IProduct {
    private final IProductDao productDao;
    private final IProductCategoryDao productCategoryDao;

    public ProductImpl(IProductDao productDao,IProductCategoryDao productCategoryDao) {
        this.productDao = productDao;
        this.productCategoryDao=productCategoryDao;
    }

    @Override
    public JpaRepository<Product, String> getRepository() {
        return productDao;
    }

    @Override
    @Transactional
    public boolean SaveImportedList(ProductImportDto dto) {
        Map<String, List<Product>> categoryProductMap = new HashMap<>(dto.getCategoryList().size());
        List<ProductCategory> categoryList = dto.getCategoryList();
        List<Product> productList = dto.getProductList();
        for (Product product : productList) {
            if (null == categoryProductMap.get(product.getCategoryId())) {
                List<Product> list = new ArrayList<>();
                list.add(product);
                categoryProductMap.put(product.getCategoryId(), list);
            } else {
                categoryProductMap.get(product.getCategoryId()).add(product);
            }
        }
        List<Product> saveList=new ArrayList<>();
        for(ProductCategory category:categoryList){
            String oldKey=category.getCategoryId();
            List<Product> list=categoryProductMap.get(oldKey);
            String newKey=UUID.randomUUID().toString();
            category.setCategoryId(newKey);
            category.setStoreCode(dto.getStoreCode());
            for(Product p:list){
                p.setCategoryId(newKey);
                p.setStoreCode(dto.getStoreCode());
                saveList.add(p);
            }
        }
        List<Product> saved= productDao.saveAll(saveList);
        List<ProductCategory> categorySaved= productCategoryDao.saveAll(categoryList);
        return saved.size()>0&categorySaved.size()>0;
    }

    @Override
    public List<Product> FindUsingList(String storeCode) {
        return productDao.findByStoreCode(storeCode);
    }
}
