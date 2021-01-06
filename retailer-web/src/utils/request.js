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

  //system
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
  CreateSharePath(params) {
    return service.post("/system/createSharePath", params);
  },
  FindSharePath(params) {
    return service.get("/system/getSharePath", params);
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
  SaveImportedListNew(params){
    return service.post("/product/importProductListNew",params)
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
   * Customer
   */
  FetchCustomerListReq(params) {
    return service.get("/customer/list", params);
  },
  SaveCustomerReq(params){
    return service.post("/customer/save",params)
  },
  QueryCustomerObjReq(params){
    return service.post("/customer/query",params);
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
  CreateStorageCountTicket(params){
    return service.post("/storage/createCountTicket",params)
  },
  ExportCountTicket(params){
    return service.post("/storage/exportStorageCountDetail",params)
  },
  DownStorageCountTicketExportExcel(params){
    return service.exportExcel("/storage/downloadCountTicketExport",params)
  },
  GetStorageCountList(params){
    return service.get("/storage/storageCountList",params)
  },
  UpdateStorageAfterCounted(params){
    return service.post("/storage/updateStorageAfterCounted",params)
  },
  GetBatchListForUpdate(params){
    return service.get("/storage/needToUpdateBatchList",params)
  },
  UpdateProductBatchList(params){
    return service.post("/storage/updateBatchList",params)
  },
  UpdateCountTicket(params){
    return service.post("/storage/updateCountTicket",params)
  },
  GetStorageCountRecord(params){
    return service.get("/storage/storageCountRecord",params)
  },
  DownLoadFile(params){
    return service.exportExcel("/storage/downloadFile",params)
  },
  GetStorageCountTiketDetails(params){
    return service.get("/storage/countTicketDetail",params)
  },
  GetUnFinishedCountTickets(params){
    return service.get('/storage/unfinishedTicket',params)
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
  GetOutBoundListReq(params){
    return service.get("/storageOut/outboundList",params)
  },
  GetStorageOutRecordDetails(params){
    return service.get("/storageOut/recordDetail",params)
  },
  GetOutTicketInfo(params){
    return service.get("/storageOut/ticketInfo",params)
  },
  /**
   * reprot
   */
  GetProductStorageRecords(params){
    return service.get("/report/productStorageRec",params)
  },
  GetDashBordData(params){
    return service.get("/report/dashboard",params)
  },
  GetGoodsReturnRecord(params){
    return service.get("/report/goodsReturnRecord",params)
  },
  /**
   * goodReturn
   */
  FindOutTicketsWithSearchVale(params){
    return service.get("/goodsReturn/findOutTickets",params)
  },
  FindOutTicketForGoodsReturn(params){
    return service.get("/goodsReturn/findOutTicketDetails",params)
  },
  SaveGoodsReturnRecords(params){
    return service.post("/goodsReturn/saveGoodsReturnRecord",params)
  },
  /**
   * share
   */
  FindShareProduct(params){
    return service.get("/storage/findShareProduct",params)
  }
};
