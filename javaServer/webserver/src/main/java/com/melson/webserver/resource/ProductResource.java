package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.webserver.dto.ProductDto;
import com.melson.webserver.dto.ProductImportDto;
import com.melson.webserver.entity.Product;
import com.melson.webserver.entity.Supply;
import com.melson.webserver.service.IProduct;
import com.melson.webserver.service.ISysConfig;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/18
 */
@RestController
@RequestMapping(value = "/product")
public class ProductResource extends BaseResource {
    private final IProduct productService;
    private final ISysConfig sysConfigService;


    public ProductResource(IProduct productService,ISysConfig sysConfigService) {
        this.productService = productService;
        this.sysConfigService=sysConfigService;
    }

    @RequestMapping(value = "/downloadProductDictTemplate",method = RequestMethod.POST)
    public void DownloadTemplate(HttpServletRequest request, HttpServletResponse response){
        String path=sysConfigService.FindValueFromCache("templatePath");
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
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

    @RequestMapping(value = "/importProductList",method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Manager)
    public Result ImportProductList(@RequestBody ProductImportDto dto, HttpServletRequest request){
        Result result=new Result();
        boolean saved=productService.SaveImportedList(dto);
        result.setResultStatus(saved?1:-1);
        return result;
    }

//    @RequestMapping(value = "/productList")
//    @RequiredPermission(SecurityLevel.Employee)
//    public Result FindUseProductList(HttpServletRequest request){
//        String storeCode=request.getParameter("storeCode");
//        if(StringUtils.isEmpty(storeCode)) return this.GenerateResult(ResultType.ParametersNeeded);
//        List<Product> productList=productService.FindUsingList(storeCode);
//        Result result=new Result();
//        result.setData(productList);
//        return  result;
//    }

    @RequestMapping(value = "/productList")
    @RequiredPermission(SecurityLevel.Employee)
    public Result FindUseProductList(HttpServletRequest request){
        long t1 = new Date().getTime();
        String storeCode=request.getParameter("storeCode");
        if(StringUtils.isEmpty(storeCode)) return this.GenerateResult(ResultType.ParametersNeeded);
        List<ProductDto> productList=productService.FindProductList(storeCode);
        Result result=new Result();
        result.setData(productList);
        long t2 = new Date().getTime();
        System.out.println("GET Rest Call: /product/productList ..."+(t2-t1));
        return  result;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Result DeleteProduct(@RequestBody ProductDto productDto){
        Result result=new Result();
        Integer deleteCount=productService.DeleteProduct(productDto);
        result.setResultStatus(deleteCount>0?1:-1);
        result.setData(deleteCount);
        return result;
    }

    @RequestMapping(value = "/query",method = RequestMethod.POST)
    public Result QueryProduct(@RequestBody ProductDto productDto){
        ProductDto prod=productService.Query(productDto);
        Result result=new Result();
        result.setData(prod);
        return result;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result SaveProduct(@RequestBody ProductDto productDto){
        Result result=new Result();
        ProductDto saved=productService.SaveProduct(productDto);
        if(saved==null){
            result.setResultStatus(-1);
            result.setMessage("create fail");
        }else {
            result.setData(saved);
        }
        return result;
    }

}
