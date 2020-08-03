package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.entity.Area;
import com.melson.base.entity.City;
import com.melson.base.entity.Province;
import com.melson.base.entity.Store;
import com.melson.base.service.IArea;
import com.melson.base.service.ICity;
import com.melson.base.service.IProvince;
import com.melson.base.service.IStore;
import com.melson.webserver.dto.StoreDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/7/29
 */
@RestController
@RequestMapping(value = "/public")
public class PublicResource extends BaseResource {
    private final IProvince provinceService;
    private final ICity cityService;
    private final IArea areaService;
    private final IStore storeService;

    public PublicResource(IProvince provinceService, ICity cityService, IArea areaService,IStore storeService) {
        this.provinceService = provinceService;
        this.cityService = cityService;
        this.areaService = areaService;
        this.storeService=storeService;
    }

    @RequestMapping("/provinceList")
    public Result GetProvinceList() {
        List<Province> provinceList=provinceService.findAllFromCache();
        Result result = new Result();
        result.setData(provinceList);
        return result;
    }

    @RequestMapping("/cityList")
    public Result GetCityList(HttpServletRequest request) {
        String code=request.getParameter("provinceCode");
        List<City> cityList=cityService.findWithProvinceCode(code);
        Result result = new Result();
        result.setData(cityList);
        return result;
    }

    @RequestMapping("/areaList")
    public Result GetAreaList(HttpServletRequest request) {
        String code=request.getParameter("cityCode");
        List<Area> areaList=areaService.findWithCityCode(code);
        Result result = new Result();
        result.setData(areaList);
        return result;
    }

    @RequestMapping(value = "/registerStore",method = RequestMethod.POST)
    public Result RegisterStore(@RequestBody StoreDto storeDto, HttpServletRequest request){
       Result result=new Result();
       Store store=storeDto.GenerateStore();
       boolean successed=storeService.RegisterStore(store,storeDto.getPassword());
       int res=successed?1:2;
       result.setResultStatus(res);
       return  result;
    }

    @RequestMapping(value = "/phoneCheck")
    public Result ValidatePhone(HttpServletRequest request){
        String phoneNum=request.getParameter("phoneNumber");
        boolean exist=storeService.CheckPhone(phoneNum);
        Result result=new Result();
        result.setData(exist);
        return result;
    }
}
