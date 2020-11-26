/* eslint-disable no-empty-pattern */
import request from "../../utils/request";

/**
 * App通用配置
 */
const state = {
  provinceList: [],
  cityList: [],
  areaList: [],
  loginStatus: false,
  userInfo: {},
  alterMessage: { show: false, type: "info", message: "" }
};

const actions = {
  /**
   * 获取省份列表
   */
  GetProvinceList({ commit }) {
    if (this.state.login.provinceList.length <= 0) {
      request
        .ProvinceList()
        .then(res => {
          commit("SetProvinceList", res.data);
        })
        .catch(error => {
          commit("SetMessage", {
            show: true,
            type: "error",
            message: error.message
          });
        });
    }
  },
  /**
   * 获取城市列表
   */
  GetCityWithCode({ commit }, provinceCode) {
    request
      .CityListWithCode(provinceCode)
      .then(res => {
        commit("SetCityList", res.data);
      })
      .catch(error => {
        commit("SetMessage", {
          show: true,
          type: "error",
          message: error.message
        });
      });
  },
  /**
   * 获取区域列表
   *
   */
  GetAreaWithCode({ commit }, cityCode) {
    request
      .AreaListWithCode(cityCode)
      .then(res => {
        commit("SetAreaList", res.data);
      })
      .catch(error => {
        commit("SetMessage", {
          show: true,
          type: "error",
          message: error.message
        });
      });
  },
  /**
   * 注册商户
   * @param {*} store 商户信息
   */
  RegisterStore({  }, store) {
   return request.RegisterStore(store)
  },
  // eslint-disable-next-line no-empty-pattern
  CheckPhone({ }, phoneNumber) {
    return request.CheckPhone(phoneNumber);
  },
  // eslint-disable-next-line no-empty-pattern
  UserLogin({ }, user) {
    return request.UserLogin(user);
  },
  SetLoginStatus({ commit }, user) {
    let loginStatus = true;
    commit("SetLoginStatus", loginStatus);
    commit("SetUserInfo", user);
  },
  // eslint-disable-next-line no-empty-pattern
  ResetPassword({ }, employee) {
    return request.ResetPassword(employee);
  }
};

const getters = {
  alterMessage: state => state.alterMessage,
  provinceList: state => state.provinceList,
  cityList: state => state.cityList,
  areaList: state => state.areaList,
  userInfo: state => state.userInfo
};

const mutations = {
  SetMessage(state, data) {
    state.alterMessage = data;
  },
  SetProvinceList(state, data) {
    state.provinceList = data;
  },
  SetCityList(state, data) {
    state.cityList = data;
  },
  SetAreaList(state, data) {
    state.areaList = data;
  },
  SetLoginStatus(state, data) {
    state.loginStatus = data;
  },
  SetUserInfo(state, data) {
    state.userInfo = data;
  }
};

export default {
  state,
  actions,
  getters,
  mutations
};
