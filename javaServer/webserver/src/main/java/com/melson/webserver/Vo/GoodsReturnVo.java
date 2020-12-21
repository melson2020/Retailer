package com.melson.webserver.Vo;

import com.melson.webserver.entity.GoodsReturnRecord;
import com.melson.webserver.entity.StorageOutDetail;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/18
 */
public class GoodsReturnVo {
   private List<GoodsReturnRecord> records;
   private List<StorageOutDetail> outDetails;

   public List<GoodsReturnRecord> getRecords() {
      return records;
   }

   public void setRecords(List<GoodsReturnRecord> records) {
      this.records = records;
   }

   public List<StorageOutDetail> getOutDetails() {
      return outDetails;
   }

   public void setOutDetails(List<StorageOutDetail> outDetails) {
      this.outDetails = outDetails;
   }
}
