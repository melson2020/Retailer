package com.melson.webserver.dao;

import com.melson.webserver.entity.StorageInTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/10
 */
@Repository
public interface IStorageInTicketDao extends JpaRepository<StorageInTicket, String> {
    List<StorageInTicket> findByCreateTimeBetweenAndStoreCode(Date startDate, Date endDate, String storeCode);

    @Query(value = "SELECT st.date,st.code,st.batchNo,sd.productId,sd.productName,sd.count,sd.beforeStorageCount as beforeCount,sd.afterInCount as afterCount,'IN' as type ,'plus' as action,st.createTime,sd.supplyName FROM storage_in_ticket st RIGHT JOIN storage_in_detail sd on st.`code`=sd.storageInTicketCode where sd.productId=?1 and st.storeCode=?4 and st.createTime>=?2 and st.createTime<=?3", nativeQuery = true)
    List<Object[]> findStorageInRec(Integer productId, String startDate, String endDate, String storeCode);
}
