import * as service from "./axios";

export default {
  /**
   * 获取省份列表
   */
  ProvinceList() {
    return service.get("/public/provinceList");
  },

  /**
   * 根据省份Code 获取对应的City
   */
  CityListWithCode(params) {
    return service.get("/public/cityList", params);
  },

  /**
   * 根据城市Code 获取对应的Area
   */
  AreaListWithCode(params) {
    return service.get("/public/areaList", params);
  },

  /**
   * 注册商户
   * @param {*} params 商户信息
   */
  RegisterStore(params) {
    return service.post("/system/registerStore", params);
  },
  /**
   * 检测联系电话是否重复
   * @param {*} params 联系电话
   */
  CheckPhone(params) {
    return service.get("/system/phoneCheck", params);
  },
  /**
   * 用户登录
   * @param {*} params
   */
  UserLogin(params) {
    return service.post("/system/login", params);
  },
  GetStoreEmployeeList(params) {
    return service.get("/employee/employeeList", params);
  },
  GetPermissionList(params) {
    return service.get("/employee/permissionList", params);
  },
  CreateEmployee(payload) {
    return service.post("/employee/createEmployee", payload.employee);
  },
  CheckLoginName(params) {
    return service.get("/employee/checkLoginName", params);
  },
  UpdateEmployee(params) {
    return service.post("/employee/updateEmployee", params);
  },
  DeleteEmployee(params) {
    return service.post("/employee/deleteEmployee", params);
  },
  ResetPassword(params){
    return service.post("/employee/restPassword",params)
  },


  /**
   * 
   * Product
   */
  DownLoadProductDictTemplate(params){
    return service.exportExcel("/product/downloadProductDictTemplate",params)
  },
  SaveImportedList(params){
    return service.post("/product/importProductList",params)
  },

  /**
   * Supply
   */
  FetchSupplyList(params) {
    return service.get("/supply/list", params);
  },
  SaveSupply(params){
    return service.post("/supply/save",params)
  },
  DeleteSupply(params){
    return service.post("/supply/delete",params);
  },
  QuerySupplyObj(params){
    return service.post("/supply/query",params);
  }

  
};
