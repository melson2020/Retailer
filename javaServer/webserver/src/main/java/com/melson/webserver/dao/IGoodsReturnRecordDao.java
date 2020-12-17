package com.melson.webserver.dao;

import com.melson.webserver.entity.GoodsReturnRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/16
 */
@Repository
public interface IGoodsReturnRecordDao extends JpaRepository<GoodsReturnRecord,String> {
    List<GoodsReturnRecord> findByStoreCodeAndOutTicketCode(String storeCode,String ticketCode);
}
