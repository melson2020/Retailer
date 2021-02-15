package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.entity.Store;
import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.base.service.IStore;
import com.melson.webserver.Vo.ShareProductListVo;
import com.melson.webserver.Vo.ShareStorageVo;
import com.melson.webserver.Vo.StorageAndProductCountVo;
import com.melson.webserver.Vo.StorageCountedVo;
import com.melson.webserver.dto.DownLoadInfoDto;
import com.melson.webserver.dto.ProductStorageDto;
import com.melson.webserver.entity.*;
import com.melson.webserver.service.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private final IStorageCountTicketDetail storageCountTicketDetailService;
    private final IProductBatch productBatchService;
    private final IStoreSharePath storeSharePathService;
    private final IStore storeService;
    private final IMenu menuService;

    public ProductStorageResource(IProductStorage productStorageService, IStorageCountTicket storageCountTicketService, ISysConfig sysConfigService, IStorageCountTicketDetail storageCountTicketDetailService, IProductBatch productBatchService, IStoreSharePath storeSharePathService, IStore storeService, IMenu menuService) {
        this.productStorageService = productStorageService;
        this.storageCountTicketService = storageCountTicketService;
        this.sysConfigService = sysConfigService;
        this.storageCountTicketDetailService = storageCountTicketDetailService;
        this.productBatchService = productBatchService;
        this.storeSharePathService = storeSharePathService;
        this.storeService = storeService;
        this.menuService = menuService;
    }

    @RequestMapping(value = "/storageAndProductCount")
    @RequiredPermission(SecurityLevel.Employee)
    public Result GetStrogaeInfo(HttpServletRequest request) {
        long t1 = new Date().getTime();
        String storeCode = request.getParameter("storeCode");
        if (StringUtils.isEmpty(storeCode)) return this.GenerateResult(ResultType.ParametersNeeded);
        Result result = new Result();
        StorageAndProductCountVo vo = productStorageService.GetProductAndStorageCount(storeCode);
        result.setData(vo);
        long t2 = new Date().getTime();
        System.out.println("Rest Call: /storage/storageAndProductCount ..."+(t2-t1));
        return result;
    }

    @RequestMapping(value = "/generateStorage")
    @RequiredPermission(SecurityLevel.Employee)
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
        System.out.println("Rest Call: /storage/generateStorage ...");
        return result;
    }

    @RequestMapping(value = "/storageList")
    @RequiredPermission(SecurityLevel.Employee)
    public Result GetStorageList(HttpServletRequest request) {
        long t1 = new Date().getTime();
        String storeCode = request.getParameter("storeCode");
        String searchType = request.getParameter("searchType");
        if (StringUtils.isEmpty(storeCode) || StringUtils.isEmpty(searchType))
            return this.GenerateResult(ResultType.ParametersNeeded);
        Result result = new Result();
        List<ProductStorage> list = productStorageService.FindStorageListWithType(storeCode, searchType);
        result.setData(list);
        long t2 = new Date().getTime();
        System.out.println("Rest Call: /storage/storageList ..."+(t2-t1));
        return result;
    }

    @RequestMapping(value = "/storageCountList")
    @RequiredPermission(SecurityLevel.Employee)
    public Result GetStorageCountList(HttpServletRequest request) {
        long t1 = new Date().getTime();
        String storeCode = request.getParameter("storeCode");
        String searchType = request.getParameter("searchType");
        if (StringUtils.isEmpty(storeCode) || StringUtils.isEmpty(searchType))
            return this.GenerateResult(ResultType.ParametersNeeded);
        Result result = new Result();
        List<ProductStorageDto> list = productStorageService.FindWithProductType(storeCode, searchType);
        result.setData(list);
        long t2 = new Date().getTime();
        System.out.println("Rest Call: /storage/storageCountList ..."+(t2-t1));
        return result;
    }

    @RequestMapping(value = "/storageDetail")
    @RequiredPermission(SecurityLevel.Employee)
    public Result GetStorageDetail(HttpServletRequest request) {
        String storeCode = request.getParameter("storeCode");
        String productId = request.getParameter("productId");
        if (StringUtils.isEmpty(storeCode) || StringUtils.isEmpty(productId))
            return this.GenerateResult(ResultType.ParametersNeeded);
        Integer pid = Integer.parseInt(productId);
        Result result = new Result();
        List<ProductBatch> list = productStorageService.FindBatchList(storeCode, pid);
        result.setData(list);
        System.out.println("Rest Call: /storage/storageDetail ...");
        return result;
    }

    @RequestMapping(value = "/needToUpdateBatchList")
    @RequiredPermission(SecurityLevel.Employee)
    public Result FindBatchListForUpdate(HttpServletRequest request) {
        String storeCode = request.getParameter("storeCode");
        String ticketCode = request.getParameter("ticketCode");
        if (StringUtils.isEmpty(storeCode) || StringUtils.isEmpty(ticketCode))
            return this.GenerateResult(ResultType.ParametersNeeded);
        Result result = new Result();
        List<ProductBatch> list = productBatchService.FindBatchListForUpdate(storeCode, ticketCode);
        result.setData(list);
        System.out.println("Rest Call: /storage/needToUpdateBatchList ...");
        return result;
    }

    @RequestMapping(value = "/updateBatchList", method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    public Result UpdateBatchList(@RequestBody List<ProductBatch> batchList) {
        Result result = new Result();
        if (batchList == null || batchList.size() <= 0) return this.GenerateResult(ResultType.ParametersNeeded);
        List<ProductBatch> savedList = productBatchService.SaveAll(batchList);
        if (savedList == null) {
            result.setResultStatus(-1);
            result.setMessage("更新失败！");
        }
        System.out.println("Rest Call: /storage/updateBatchList ...");
        return result;
    }

    @RequestMapping(value = "/updateCountTicket", method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    public Result UpdateCountTicket(@RequestBody StorageCountTicket ticket) {
        Result result = new Result();
        if (ticket == null || ticket.getId() == null) return this.GenerateResult(ResultType.ParametersNeeded);
        StorageCountTicket savedTicket = storageCountTicketService.SaveTicket(ticket);
        if (savedTicket == null) {
            result.setResultStatus(-1);
            result.setMessage("更新失败！");
        } else {
            result.setData(ticket);
        }
        System.out.println("Rest Call: /storage/updateCountTicket ...");
        return result;
    }

    @RequestMapping(value = "/createCountTicket", method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    public Result CreateStorageCountTicket(@RequestBody StorageCountTicket ticket, HttpServletRequest request) {
        Result result = new Result();
        List<StorageCountTicket> unfinishEdTickets = storageCountTicketService.FindUnFinishedTicket(ticket.getStoreCode());
        if (unfinishEdTickets.size() > 0) {
            result.setResultStatus(2);
            result.setMessage("unfinished ticket count::" + unfinishEdTickets.size());
            return result;
        }
        List<ProductStorage> list = productStorageService.FindStorageListWithType(ticket.getStoreCode(), ticket.getProductType());
        if (list.size() <= 0) {
            result.setResultStatus(-1);
            result.setMessage("盘点内容数量<=0，请检查盘点内容");
            return result;
        }
        StorageCountTicket savedTicket = storageCountTicketService.CreateTicekt(ticket);
        if (savedTicket != null) {
            result.setData(savedTicket);
        } else {
            result.setResultStatus(-1);
            result.setMessage("create ticket fail");
        }
        System.out.println("Rest Call: /storage/createCountTicket ...");
        return result;
    }

    @RequestMapping(value = "/exportStorageCountDetail", method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    public Result ExportStorageCountDetail(@RequestBody StorageCountTicket tikcet) {
        String basePath = sysConfigService.FindValueFromCache("storageCountTicketExcelBasePath");
        List<ProductStorageDto> storageCountList = productStorageService.FindWithProductType(tikcet.getStoreCode(), tikcet.getProductType());
        Result result = storageCountTicketService.ExportExcel(storageCountList, basePath, tikcet);
        System.out.println("Rest Call: /storage/exportStorageCountDetail ...");
        return result;
    }

    @RequestMapping(value = "/downloadCountTicketExport", method = RequestMethod.POST)
    public void DownloadTemplate(@RequestBody StorageCountTicket tikcet, HttpServletResponse response) {
        String path = tikcet.getExcelExportPath();
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
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

    @RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
    public void DownLoadFile(@RequestBody DownLoadInfoDto dto, HttpServletResponse response) {
        String path = dto.getPath();
        if (StringUtils.isEmpty(path)) return;
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
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

    @RequestMapping(value = "/uploadCountedExcel", method = RequestMethod.POST)
    public Result AddExcel(@RequestBody MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ticketCode = request.getParameter("ticketCode");
        String basePath = sysConfigService.FindValueFromCache("storageCountTicketExcelBasePath");
        return storageCountTicketService.ImportCountedExcel(ticketCode, file, basePath);
    }


    /**
     * 更具上传的excel 更新库存
     */
    @RequestMapping(value = "/updateStorageAfterCounted", method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    @Transactional
    public Result UpdateStorage(@RequestBody StorageCountedVo vo) {
        List<ProductStorageDto> dtoList = vo.getDtoList();
        StorageCountTicket ticket = vo.getTicket();
        StorageCountTicket existTicket = storageCountTicketService.FindByCode(ticket.getCode());
        if (existTicket == null) {
            return this.GenerateResult(ResultType.ExceptionCatched, "wrong ticket");
        }
        if (!existTicket.getResult().equals("unComplete")) {
            return this.GenerateResult(ResultType.ExceptionCatched, "this ticket has been completed");
        }
        if (dtoList == null || ticket == null)
            return this.GenerateResult(ResultType.ParametersNeeded);
        //未有数据变动，直接更新ticket 状态
        if (dtoList.size() <= 0) {
            ticket.setResult("correct");
            ticket.setStatus(5);
            storageCountTicketService.SaveTicket(ticket);
            return new Result();
        }
        System.out.println("Rest Call: /storage/updateStorageAfterCounted ...");
        //存在数据变动项 更新库存信息
        return storageCountTicketDetailService.SaveDetailWithCountedList(dtoList, ticket);

    }

    @RequestMapping(value = "/storageCountRecord", method = RequestMethod.GET)
    @RequiredPermission(SecurityLevel.Employee)
    public Result GetStorageCountTicketRec(HttpServletRequest request) {
        Result result = new Result();
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String storeCode = request.getParameter("storeCode");
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate) || StringUtils.isEmpty(storeCode))
            return this.GenerateResult(ResultType.ParametersNeeded);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateBegin = sdf.parse(startDate);
            Date dateEnd = sdf.parse(endDate);
            calendar.setTime(dateEnd);
            calendar.add(Calendar.DATE, 1);
            dateEnd = calendar.getTime();
            List<StorageCountTicket> tickets = storageCountTicketService.GetStorageCountRecord(dateBegin, dateEnd, storeCode);
            result.setData(tickets);
        } catch (Exception ex) {
            result.setResultStatus(-1);
            result.setMessage("date parse false, wrong date formate");
        }
        System.out.println("Rest Call: /storage/storageCountRecord ...");
        return result;
    }

    @RequestMapping(value = "/countTicketDetail", method = RequestMethod.GET)
    @RequiredPermission(SecurityLevel.Employee)
    public Result GetStorageCountTicketDetail(HttpServletRequest request) {
        String ticketCode = request.getParameter("ticketCode");
        if (StringUtils.isEmpty(ticketCode)) return this.GenerateResult(ResultType.ParametersNeeded);
        Result result = new Result();
        List<StorageCountTicketDetail> detailList = storageCountTicketDetailService.FindCountTicketDetails(ticketCode);
        result.setData(detailList);
        System.out.println("Rest Call: /storage/countTicketDetail ...");
        return result;
    }

    @RequestMapping(value = "/unfinishedTicket", method = RequestMethod.GET)
    @RequiredPermission(SecurityLevel.Employee)
    public Result FindExistUnFinishCountTicket(HttpServletRequest request) {
        Result result = new Result();
        String storeCode = request.getParameter("storeCode");
        if (StringUtils.isEmpty(storeCode)) return this.GenerateResult(ResultType.ParametersNeeded);
        List<StorageCountTicket> existList = storageCountTicketService.FindUnFinishedTicket(storeCode);
        result.setData(existList);
        System.out.println("Rest Call: /storage/unfinishedTicket ...");
        return result;
    }

    @RequestMapping(value = "/findShareProduct", method = RequestMethod.GET)
    public Result FindShareProductStorage(HttpServletRequest request) {
        System.out.println("Rest Call: /storage/findShareProduct ...");
        String shareCode = request.getParameter("shareCode");
        if (StringUtils.isEmpty(shareCode)) return this.GenerateResult(ResultType.ParametersNeeded);
        Result result = new Result();
        StoreSharePath storeSharePath = storeSharePathService.FindSharePathWithShareCode(shareCode);
        if (storeSharePath == null) {
            result.setResultStatus(-1);
            result.setMessage("wrong shareCode");
        } else {
            //TODO 校验expireDate
            ShareProductListVo listVo = new ShareProductListVo();
            Store store = storeService.findByCode(storeSharePath.getStoreCode());
            List<Menu> menuList = menuService.GetMenuListWithPermission(3); //默认给展示所有的菜单
            List<ShareStorageVo> productStorageList = productStorageService.FindShareStorage(storeSharePath.getStoreCode());
            listVo.setProductStorageList(productStorageList);
            listVo.setStore(store);
            listVo.setMenuList(menuList);
            result.setData(listVo);
        }
        return result;
    }
}
