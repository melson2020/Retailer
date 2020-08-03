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
    return service.post("/public/registerStore", params);
  },
  /**
   * 检测联系电话是否重复
   * @param {*} params 联系电话
   */
  CheckPhone(params) {
    return service.get("/public/phoneCheck", params);
  }
};
