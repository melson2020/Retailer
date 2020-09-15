package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.utils.EntityManagerUtil;
import com.melson.webserver.dao.IProductCategoryDao;
import com.melson.webserver.dao.IProductDao;
import com.melson.webserver.dto.ProductDto;
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
    private final EntityManagerUtil entityManagerUtil;

    public ProductImpl(IProductDao productDao,IProductCategoryDao productCategoryDao ,EntityManagerUtil entityManagerUtil) {
        this.productDao = productDao;
        this.productCategoryDao=productCategoryDao;
        this.entityManagerUtil=entityManagerUtil;
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

//    @Override
//    public List<Product> FindUsingList(String storeCode) {
//        return productDao.findByStoreCode(storeCode);
//    }

    @Override
    public List<ProductDto> FindProductList(String storeCode) {
        String sql="SELECT pro.id,pro.name,pro.type,pro.specification,pro.unit,pro.feature,prc.categoryId as categoryId,prc.name as category from product pro INNER JOIN product_category prc on pro.categoryId=prc.categoryId WHERE pro.storeCode='"+storeCode+"'";
        List<Object[]> objList=entityManagerUtil.ExcuteSql(sql);
        List<ProductDto> dtoList=new ArrayList<>(objList.size());
        for (Object[] obj:objList){
            ProductDto dto=new ProductDto();
            dto.setId(Integer.parseInt(obj[0].toString()));
            dto.setName(obj[1]==null?null:obj[1].toString());
            dto.setType(obj[2]==null?null:obj[2].toString());
            dto.setSpecification(obj[3]==null?null:obj[3].toString());
            dto.setUnit(obj[4]==null?null:obj[4].toString());
            dto.setFeature(obj[5]==null?null:obj[5].toString());
            dto.setCategoryId(obj[6]==null?null:obj[6].toString());
            dto.setCategoryName(obj[7]==null?null:obj[7].toString());
            dtoList.add(dto);
        }
        return dtoList;
    }
}
