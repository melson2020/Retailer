package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.base.Result;
import com.melson.webserver.dto.ProductStorageDto;
import com.melson.webserver.entity.ProductStorage;
import com.melson.webserver.entity.StorageCountTicket;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/10/14
 */
public interface IStorageCountTicket extends IService<StorageCountTicket> {
    StorageCountTicket CreateTicekt(StorageCountTicket ticket);
    StorageCountTicket SaveTicket(StorageCountTicket ticket);
    StorageCountTicket FindByCode(String code);
    List<StorageCountTicket> FindUnFinishedTicket(String storeCode);
    Result ExportExcel(List<ProductStorageDto> storageList, String basePath, StorageCountTicket ticket);
    Result ImportCountedExcel(String ticketCode, MultipartFile file,String basePtah);
    List<StorageCountTicket> GetStorageCountRecord(Date startDate,Date endDate,String storeCode);
}
