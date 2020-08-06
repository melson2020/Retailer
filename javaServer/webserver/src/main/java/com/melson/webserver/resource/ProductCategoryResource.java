package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.webserver.entity.ProductCategory;
import com.melson.webserver.service.IProductCategory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Messi on 2020/8/6
 */
@RestController
@RequestMapping("/productCategory")
public class ProductCategoryResource extends BaseResource {
    private final IProductCategory productCategory;

    public ProductCategoryResource(IProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Result RegisterStore(HttpServletRequest request){
        Result result=new Result();
        List<ProductCategory> productCategories = productCategory.findAll();
        result.setData(productCategories);
        System.out.println("POST Rest Call: /productCategory/list ...");
        return  result;
    }

}
