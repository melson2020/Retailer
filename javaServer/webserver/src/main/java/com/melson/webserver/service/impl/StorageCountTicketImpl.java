package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.Result;
import com.melson.base.dao.IStoreDao;
import com.melson.base.entity.Store;
import com.melson.base.service.IStore;
import com.melson.webserver.Vo.ExcelExportResultVo;
import com.melson.webserver.dao.IStorageCountTicketDao;
import com.melson.webserver.dto.ProductStorageDto;
import com.melson.webserver.entity.ProductStorage;
import com.melson.webserver.entity.StorageCountTicket;
import com.melson.webserver.service.IStorageCountTicket;
import com.melson.webserver.utils.FilePathValidateUtils;
import com.melson.webserver.utils.PoiUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/10/14
 */
@Service
public class StorageCountTicketImpl extends AbstractService<StorageCountTicket> implements IStorageCountTicket {
    private final IStorageCountTicketDao storageCountTicketDao;
    private final IStoreDao storeDao;

    public StorageCountTicketImpl(IStorageCountTicketDao storageCountTicketDao, IStoreDao storeDao) {
        this.storageCountTicketDao = storageCountTicketDao;
        this.storeDao = storeDao;
    }

    @Override
    public JpaRepository<StorageCountTicket, String> getRepository() {
        return storageCountTicketDao;
    }


    @Override
    public StorageCountTicket CreateTicekt(StorageCountTicket ticket) {
        ticket.setCode(UUID.randomUUID().toString());
        ticket.setCreateTime(new Date());
        ticket.setStatus(1);
        ticket.setResult("unComplete");
        return storageCountTicketDao.save(ticket);
    }

    @Override
    public StorageCountTicket SaveTicket(StorageCountTicket ticket) {
        return storageCountTicketDao.save(ticket);
    }

   public StorageCountTicket FindByCode(String code){
        return storageCountTicketDao.findByCode(code);
   }

    @Override
    public Result ExportExcel(List<ProductStorageDto> storageList, String basePath, StorageCountTicket ticket) {
        Result result = new Result();
        Store store = storeDao.findByCode(ticket.getStoreCode());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat monthSdf = new SimpleDateFormat("yyyy-MM");
        String wholeFolderPath = basePath + "\\" + ticket.getStoreCode() + "\\Export" + "\\" + monthSdf.format(ticket.getCreateTime());
        FilePathValidateUtils filePathValidateUtils = new FilePathValidateUtils();
        boolean folderCreated = filePathValidateUtils.validateFolderPath(wholeFolderPath);
        if (!folderCreated) {
            result.setResultStatus(-1);
            result.setMessage("create export excel folder failed");
            return result;
        }
        String[] headerNames = new String[]{"ID", "名称", "型号", "规格", "批次", "供应商", "现有数量","现有总数","单位","盘点数量","盘点总数"};
        String title = store.getStoreName() + "盘点单";
        String dateStr = sdf.format(ticket.getCreateTime());
        String subTitile = "出单人员:" + ticket.getEmployeeName() + "  类型:" + ticket.getType() + "  " + dateStr;
        PoiUtils poiUtils = new PoiUtils();
        String filePath = filePathValidateUtils.ValidateFilePath(wholeFolderPath + "\\" + ticket.getDate() + "盘点单", ".xls");
        ExcelExportResultVo vo = poiUtils.ExceptStorageCountDetail(title, subTitile, "盘点人员", headerNames, storageList, filePath);
        if (vo.getStatus() == 1) {
            ticket.setExcelExportPath(filePath);
            String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
            ticket.setExcelExportFileName(fileName);
            ticket.setStatus(2);
            StorageCountTicket saved = storageCountTicketDao.save(ticket);
            result.setData(saved);
        } else {
            result.setResultStatus(vo.getStatus());
            result.setMessage(vo.getMessage());
        }
        return result;
    }

    @Override
    public Result ImportCountedExcel(String ticketCode, MultipartFile file, String basePtah) {
        Result result = new Result();
        SimpleDateFormat monthSdf = new SimpleDateFormat("yyyy-MM");
        FilePathValidateUtils filePathValidateUtils = new FilePathValidateUtils();
        if (file.isEmpty()) {
            result.setResultStatus(-1);
            result.setMessage("Empty file");
            return result;
        }
        StorageCountTicket ticket = storageCountTicketDao.findByCode(ticketCode);
        if (ticket == null) {
            result.setResultStatus(-1);
            result.setMessage("Wrong ticket code");
            return result;
        }
        String month = monthSdf.format(ticket.getCreateTime());
        String fileName = file.getOriginalFilename();
        String fullFolder= basePtah + "\\" + ticket.getStoreCode() + "\\" + "Import\\" + month;
        String fullPath =fullFolder+ "\\" + fileName;
        boolean folderCreated = filePathValidateUtils.validateFolderPath(fullFolder);
        if (!folderCreated) {
            result.setResultStatus(-1);
            result.setMessage("create export excel folder failed");
            return result;
        }
        fullPath = fullPath.substring(0, fullPath.lastIndexOf("."));
        String filePath = filePathValidateUtils.ValidateFilePath(fullPath, ".xls");
        File dest = new File(filePath);
        try {
            file.transferTo(dest);
            String savedFileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
            ticket.setExcelImportPath(filePath);
            ticket.setExcelImportFileName(savedFileName);
            ticket.setStatus(3);
            StorageCountTicket saved = storageCountTicketDao.save(ticket);
            result.setData(saved);
            return result;
        } catch (Exception e) {
            result.setResultStatus(-1);
            result.setMessage(e.getMessage());
            return result;
        }
    }
}
