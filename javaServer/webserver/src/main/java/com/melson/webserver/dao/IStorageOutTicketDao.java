package com.melson.webserver.dao;

import com.melson.webserver.entity.StorageOutTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/18
 */
@Repository
public interface IStorageOutTicketDao extends JpaRepository<StorageOutTicket,String> {
    List<StorageOutTicket> findByCreateTimeBetweenAndStoreCode(Date startDate,Date endDate,String stroeCode);
    StorageOutTicket findByStoreCodeAndCode(String storeCode,String code);

    @Query(value = "SELECT st.date,st.code,sd.storageInBatchNo as batchNo,sd.productId,sd.productName,sd.outCount as count,sd.beforeOutCount as beforeCount,sd.afterOutCount as afterCount,'OUT' as type, 'minus' as action,st.createTime,sd.supplyName FROM storage_out_ticket st RIGHT JOIN storage_out_detail sd on st.`code`=sd.outTicketCode where sd.productId=?1 and st.storeCode=?4 and st.createTime>=?2 and st.createTime<=?3", nativeQuery = true)
    List<Object[]> findStorageOutRec(Integer productId, String startDate, String endDate, String storeCode);
}
