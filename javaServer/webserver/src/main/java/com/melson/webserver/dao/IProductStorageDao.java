package com.melson.webserver.dao;

import com.melson.webserver.entity.ProductStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/27
 */
@Repository
public interface IProductStorageDao extends JpaRepository<ProductStorage,String> {
    @Query(value = "Select count(id) from product_storage where storeCode=?1",nativeQuery = true)
    Integer GetCountWithStore(String storeCode);

    List<ProductStorage> findByStoreCode(String storeCode);
    List<ProductStorage> findByProductIdIn(Set<Integer> productIds);
    List<ProductStorage> findByStoreCodeAndSearchType(String storeCode,String searchType);
    List<ProductStorage> findByStoreCodeAndCountGreaterThan(String storeCode,Integer count);

    @Modifying
    @Query(value = "UPDATE product_storage SET productName=?2, productType=?3, productSpecification=?4, unit=?5 where productId=?1",nativeQuery = true)
    void updateStorage(Integer id, String name, String type, String specification, String unit);


    ProductStorage findByProductIdAndStoreCode(Integer id, String storeCode);

    @Modifying
    @Query(value = "DELETE from product_storage where productId=?1",nativeQuery = true)
    void deleteByProductId(Integer id);

    @Query(nativeQuery=true,value = "SELECT sot.date,sot.`code`,sot.employeeName, sobd.productName,sobd.supplyName,sobd.batchNo,sobd.unitPriceIn,sobd.unitPriceOut,sobd.outCount,sobd.profit\n" +
            "from storage_out_ticket sot\n" +
            "LEFT JOIN storage_out_bill_detail sobd on sot.billCode = sobd.outBillCode\n" +
            "where sot.employeeId =:userId and sot.createTime>=:dateBegin and sot.createTime<=:newEnd")
    List<Object[]> findVoByUserId(@Param("dateBegin") Date dateBegin,@Param("newEnd")  Date newEnd,@Param("userId")   String userId);

    @Query(nativeQuery=true,value = "SELECT sot.date,sot.`code`,sot.employeeName, sobd.productName,sobd.supplyName,sobd.batchNo,sobd.unitPriceIn,sobd.unitPriceOut,sobd.outCount,sobd.profit\n" +
            "from storage_out_ticket sot\n" +
            "LEFT JOIN storage_out_bill_detail sobd on sot.billCode = sobd.outBillCode\n" +
            "where sot.storeCode =:storeCode and sot.createTime>=:dateBegin and sot.createTime<=:newEnd")
    List<Object[]> findVoByStoreCode(@Param("dateBegin") Date dateBegin,@Param("newEnd")  Date newEnd,@Param("storeCode")   String storeCode);
}
