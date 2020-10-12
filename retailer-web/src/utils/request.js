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
  GetProductListReq(params){
    return service.get("/product/productList",params)
  },
  GetCategoryListReq(params){
    return service.get("/product/categoryList",params)
  },
  DeleteProductReq(params){
    return service.post("/product/delete",params)
  },
  DeleteCategoryReq(params){
    return service.post("/product/deleteCategory",params)
  },
  QueryProductObjReq(params){
    return service.post("/product/query",params)
  },
  SaveProductReq(params){
    return service.post("/product/save",params)
  },
  SaveCategoryReq(params){
    return service.post("/product/saveCategory",params)
  },


  /**
   * Supply
   */
  FetchSupplyListReq(params) {
    return service.get("/supply/list", params);
  },
  SaveSupplyReq(params){
    return service.post("/supply/save",params)
  },
  DeleteSupplyReq(params){
    return service.post("/supply/delete",params);
  },
  QuerySupplyObjReq(params){
    return service.post("/supply/query",params);
  },

  /**
   * Storage
   */
  GetProductStorageCount(params){
    return service.get("/storage/storageAndProductCount",params)
  },
  GenerateStorageList(params){
    return service.get("/storage/generateStorage",params)
  },
  GetStorageList(params){
    return service.get("/storage/storageList",params)
  },
  GetBatchList(params){
    return service.get("/storage/storageDetail",params)
  },

  /**
   * Storage In
   */
  SaveStorageInTicket(params){
    return service.post("/storageIn/saveTicket",params)
  },
  GetTaxRateList(){
    return service.get("/storageIn/taxRateList")
  },
  GetRecordList(params){
    return service.get("/storageIn/storageInRecord",params)
  },
  GetRecordDetail(params){
    return service.get("/storageIn/storageInRecordDetails",params)
  },
  /**
   * Storage Out
   */
  SaveStorageOutTicket(params){
    return service.post("/storageOut/saveOutTicket",params)
  },
  GetStorageOutRecordList(params){
    return service.get("/storageOut/ticketRecord",params)
  },
  GetStorageOutRecordDetails(params){
    return service.get("/storageOut/recordDetail",params)
  }
};
