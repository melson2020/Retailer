package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.webserver.Vo.StorageAndProductCountVo;
import com.melson.webserver.dto.ProductStorageDto;
import com.melson.webserver.entity.ProductBatch;
import com.melson.webserver.entity.ProductStorage;
import com.melson.webserver.entity.StorageCountTicket;
import com.melson.webserver.service.IProductStorage;
import com.melson.webserver.service.IStorageCountTicket;
import com.melson.webserver.service.ISysConfig;
import com.melson.webserver.utils.PoiUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/27
 */
@RestController
@RequestMapping(value = "/storage")
public class ProductStorageResource extends BaseResource {
    private final IProductStorage productStorageService;
    private final IStorageCountTicket storageCountTicketService;
    private final ISysConfig sysConfigService;

    public ProductStorageResource(IProductStorage productStorageService, IStorageCountTicket storageCountTicketService, ISysConfig sysConfigService) {
        this.productStorageService = productStorageService;
        this.storageCountTicketService = storageCountTicketService;
        this.sysConfigService = sysConfigService;
    }

    @RequestMapping(value = "/storageAndProductCount")
    @RequiredPermission(SecurityLevel.Manager)
    public Result GetStrogaeInfo(HttpServletRequest request) {
        String storeCode = request.getParameter("storeCode");
        if (StringUtils.isEmpty(storeCode)) return this.GenerateResult(ResultType.ParametersNeeded);
        Result result = new Result();
        StorageAndProductCountVo vo = productStorageService.GetProductAndStorageCount(storeCode);
        result.setData(vo);
        return result;
    }

    @RequestMapping(value = "/generateStorage")
    @RequiredPermission(SecurityLevel.Manager)
    public Result GenerateStorage(HttpServletRequest request) {
        String storeCode = request.getParameter("storeCode");
        if (StringUtils.isEmpty(storeCode)) return this.GenerateResult(ResultType.ParametersNeeded);
        Result result = new Result();
        List<ProductStorage> list = productStorageService.GenerateStorage(storeCode);
        if (list != null) {
            result.setData(list);
        } else {
            result.setResultStatus(-1);
            result.setMessage("Generate failed");
        }
        return result;
    }

    @RequestMapping(value = "/storageList")
    @RequiredPermission(SecurityLevel.Manager)
    public Result GetStorageList(HttpServletRequest request) {
        String storeCode = request.getParameter("storeCode");
        String searchType = request.getParameter("searchType");
        if (StringUtils.isEmpty(storeCode) || StringUtils.isEmpty(searchType))
            return this.GenerateResult(ResultType.ParametersNeeded);
        Result result = new Result();
        List<ProductStorage> list = productStorageService.FindStorageListWithType(storeCode, searchType);
        result.setData(list);
        return result;
    }

    @RequestMapping(value = "/storageCountList")
    @RequiredPermission(SecurityLevel.Employee)
    public Result GetStorageCountList(HttpServletRequest request) {
        String storeCode = request.getParameter("storeCode");
        String searchType = request.getParameter("searchType");
        if (StringUtils.isEmpty(storeCode) || StringUtils.isEmpty(searchType))
            return this.GenerateResult(ResultType.ParametersNeeded);
        Result result = new Result();
        List<ProductStorageDto> list = productStorageService.FindWithProductType(storeCode, searchType);
        result.setData(list);
        return result;
    }

    @RequestMapping(value = "/storageDetail")
    @RequiredPermission(SecurityLevel.Manager)
    public Result GetStorageDetail(HttpServletRequest request) {
        String storeCode = request.getParameter("storeCode");
        String productId = request.getParameter("productId");
        if (StringUtils.isEmpty(storeCode) || StringUtils.isEmpty(productId))
            return this.GenerateResult(ResultType.ParametersNeeded);
        Integer pid = Integer.parseInt(productId);
        Result result = new Result();
        List<ProductBatch> list = productStorageService.FindBatchList(storeCode, pid);
        result.setData(list);
        return result;
    }

    @RequestMapping(value = "/createCountTicket", method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    public Result CreateStorageCountTicket(@RequestBody StorageCountTicket ticket, HttpServletRequest request) {
        Result result = new Result();
        StorageCountTicket savedTicket = storageCountTicketService.CreateTicekt(ticket);
        if (savedTicket != null) {
            result.setData(savedTicket);
        } else {
            result.setResultStatus(-1);
            result.setMessage("create ticket fail");
        }
        return result;
    }

    @RequestMapping(value = "/exportStorageCountDetail", method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    public Result ExportStorageCountDetail(@RequestBody StorageCountTicket tikcet) {
        String basePath = sysConfigService.FindValueFromCache("storageCountTicketExcelBasePath");
        List<ProductStorageDto> storageCountList = productStorageService.FindWithProductType(tikcet.getStoreCode(), tikcet.getProductType());
        Result result = storageCountTicketService.ExportExcel(storageCountList,basePath,tikcet);
        return result;
    }

    @RequestMapping(value = "/downloadCountTicketExport",method = RequestMethod.POST)
    public void DownloadTemplate(@RequestBody StorageCountTicket tikcet, HttpServletResponse response){
        String path=tikcet.getExcelExportPath();
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis= new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(value = "/uploadCountedExcel",method = RequestMethod.POST)
    public Result AddExcel(@RequestBody MultipartFile file, HttpServletRequest request, HttpServletResponse response)throws Exception{
        String ticketCode=request.getParameter("ticketCode");
        String basePath = sysConfigService.FindValueFromCache("storageCountTicketExcelBasePath");
        return storageCountTicketService.ImportCountedExcel(ticketCode,file,basePath);
    }
    @RequestMapping(value = "/updateSorageAfterCounted",method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    public Result UpdateStorage(@RequestBody List<ProductStorageDto> dtoList){
        Result result=new Result();
        return result;
    }
}
