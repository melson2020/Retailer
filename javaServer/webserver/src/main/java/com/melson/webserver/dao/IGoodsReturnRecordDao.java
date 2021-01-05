package com.melson.webserver.dao;

import com.melson.webserver.entity.GoodsReturnRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/16
 */
@Repository
public interface IGoodsReturnRecordDao extends JpaRepository<GoodsReturnRecord,String>, JpaSpecificationExecutor<GoodsReturnRecord> {
    List<GoodsReturnRecord> findByStoreCodeAndOutTicketCode(String storeCode,String ticketCode);

    @Query(value = "SELECT  date,outTicketCode as code,batchNo,productId,productName,count,beforeCount,afterCount,'R' as type, 'plus' as action,createTime,supplyName FROM `goods_return_record` WHERE productId=?1 and storeCode=?4 and createTime>=?2 and createTime<=?3",nativeQuery = true)
    List<Object[]> findGoodsReturnRec(Integer productId, String startDate, String endDate, String storeCode);

    List<GoodsReturnRecord> findByStoreCodeAndCreateTimeBetween(String storeCode, Date startDate,Date endDate);
}
