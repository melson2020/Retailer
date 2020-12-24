package com.melson.webserver.dao;

import com.melson.webserver.entity.StorageOutTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
public interface IStorageOutTicketDao extends JpaRepository<StorageOutTicket, String> {
    List<StorageOutTicket> findByCreateTimeBetweenAndStoreCode(Date startDate, Date endDate, String stroeCode);

    StorageOutTicket findByStoreCodeAndCode(String storeCode, String code);

    @Query(value = "SELECT st.date,st.code,sd.storageInBatchNo as batchNo,sd.productId,sd.productName,sd.outCount as count,sd.beforeOutCount as beforeCount,sd.afterOutCount as afterCount,'OUT' as type, 'minus' as action,st.createTime,sd.supplyName FROM storage_out_ticket st RIGHT JOIN storage_out_detail sd on st.`code`=sd.outTicketCode where sd.productId=?1 and st.storeCode=?4 and st.createTime>=?2 and st.createTime<=?3", nativeQuery = true)
    List<Object[]> findStorageOutRec(Integer productId, String startDate, String endDate, String storeCode);

    @Query(value = "select sd.productId,sd.productName,st.employeeId,st.employeeName,sd.totalPrice from storage_out_ticket st RIGHT JOIN storage_out_detail sd on st.`code`=sd.outTicketCode  where st.storeCode=?3 and st.createTime>=?1 and st.createTime<=?2", nativeQuery = true)
    List<Object[]> findDashboardDetail(String startDate, String endDate, String storeCode);

//    List<StorageOutTicket> findByCodeLikeOrCustomerNameLike(String searchValue1, String searchValue2);
//
//    List<StorageOutTicket> findByCodeLikeOrCustomerNameLikeAndDate(String searchValue1, String searchValue2, String date);
//
//    List<StorageOutTicket> findByDate(String date);

    @Modifying
    @Query(value = "UPDATE StorageOutTicket st set st.status=?1 where st.code=?2 and st.storeCode=?3")
    void UpdateOutTicketStatus(Integer status, String code, String storeCode);
}
