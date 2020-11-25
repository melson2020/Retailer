package com.melson.webserver.dao;

import com.melson.webserver.entity.StorageCountTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/10/14
 */
@Repository
public interface IStorageCountTicketDao extends JpaRepository<StorageCountTicket,String> {
    StorageCountTicket findByCode(String code);
    List<StorageCountTicket> findByStoreCodeAndStatusLessThan(String storeCode,Integer status);
    List<StorageCountTicket> findByCreateTimeBetweenAndStoreCode(Date startDate,Date endDate,String storeCode);

    @Query(value = "select st.date as date,st.`code`,sd.batchNo,sd.productId,sd.productName,sd.count,sd.beforeCount,sd.afterCount,'C' as type, sd.type as action,st.createTime,sd.supplyName from storage_count_ticket st RIGHT JOIN storage_count_ticket_detail sd on st.`code`=sd.ticketCode WHERE sd.productId=?1 and st.storeCode=?4 and st.createTime>=?2 and st.createTime<=?3",nativeQuery = true)
    List<Object[]> findStorageCountRec(Integer productId, String startDate, String endDate, String storeCode);
}
