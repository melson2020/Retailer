package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.webserver.dto.ProductDto;
import com.melson.webserver.dto.ProductImportDto;
import com.melson.webserver.entity.ProductCategory;
import com.melson.webserver.entity.ProductStorage;
import com.melson.webserver.service.IProduct;
import com.melson.webserver.service.IProductStorage;
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
    private final IProductStorage productStorageService;


    public ProductResource(IProduct productService, ISysConfig sysConfigService, IProductStorage productStorageService) {
        this.productService = productService;
        this.sysConfigService=sysConfigService;
        this.productStorageService = productStorageService;
    }

    @RequestMapping(value = "/downloadProductDictTemplate",method = RequestMethod.POST)
    public void DownloadTemplate(HttpServletRequest request, HttpServletResponse response){
        String path=sysConfigService.FindValueFromCache("templatePath");
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            System.out.println(file+"_________________________________________");
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
    @RequiredPermission(SecurityLevel.Employee)
    public Result ImportProductList(@RequestBody ProductImportDto dto, HttpServletRequest request){
        Result result=new Result();
        boolean saved=productService.SaveImportedList(dto);
        result.setResultStatus(saved?1:-1);
        return result;
    }

    @RequestMapping(value = "/importProductListNew",method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    public Result importProductListNew(@RequestBody ProductImportDto dto, HttpServletRequest request){
        Result result=new Result();
        boolean saved=productService.SaveImportedListNew(dto);
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

//    @RequestMapping(value = "/productList")
//    @RequiredPermission(SecurityLevel.Employee)
//    public Result FindUseProductList(HttpServletRequest request){
//        long t1 = new Date().getTime();
//        String storeCode=request.getParameter("storeCode");
//        if(StringUtils.isEmpty(storeCode)) return this.GenerateResult(ResultType.ParametersNeeded);
//        List<ProductDto> productList=productService.FindProductList(storeCode);
//        Result result=new Result();
//        result.setData(productList);
//        long t2 = new Date().getTime();
//        System.out.println("GET Rest Call: /product/productList ..."+(t2-t1));
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


    @RequestMapping(value = "/categoryList")
    @RequiredPermission(SecurityLevel.Employee)
    public Result FindCategoryList(HttpServletRequest request){
        String storeCode=request.getParameter("storeCode");
        if(StringUtils.isEmpty(storeCode)) return this.GenerateResult(ResultType.ParametersNeeded);
//        List<ProductCategoryDto> productCategoryDtos= productService.QueryCategoryList(storeCode);
        List<ProductCategory> productCategories=productService.FindCategoryList(storeCode);
        Result result=new Result();
        result.setData(productCategories);
        return result;
    }


    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Result DeleteProduct(@RequestBody ProductDto productDto){
        Result result=new Result();
        ProductStorage storage = productStorageService.FindByProductIdAndStoreCode(productDto.getId(),productDto.getStoreCode());
        if(storage.getCount()>0)
        {
            result.setResultStatus(-1);
            result.setMessage("库存中存在的货品不能删除！");
        }
        else
        {
            Integer deleteCount=productService.DeleteProduct(productDto);
            result.setResultStatus(deleteCount>0?1:-1);
            result.setData(deleteCount);
        }
        return result;
    }

    @RequestMapping(value = "/deleteCategory",method = RequestMethod.POST)
    public Result DeleteCategory(@RequestBody ProductCategory productCategory){
        Result result=new Result();
        Integer deleteCount=productService.DeleteCategory(productCategory);
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
        System.out.println("GET Rest Call: /product/save ...");
        return result;
    }

    @RequestMapping(value = "/saveCategory",method = RequestMethod.POST)
    public Result SaveCategory(@RequestBody ProductCategory category){
        Result result=new Result();
        ProductCategory saved=productService.SaveCategory(category);
        if(saved==null){
            result.setResultStatus(-1);
            result.setMessage("create fail");
        }else {
            result.setData(saved);
        }
        return result;
    }
}
