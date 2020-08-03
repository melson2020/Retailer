import request from "../../utils/request";

/**
 * App通用配置
 */
const state = {
  provinceList: [],
  cityList: [],
  areaList: [],
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
  RegisterStore({ commit }, store) {
    request
      .RegisterStore(store)
      .then(res => {
        let alter = { show: true, type: "", message: "" };
        if (res.resultStatus == 1) {
          alter.type = "success";
          alter.message = "创建成功！";
        } else {
          alter.type = "warning";
          alter.message = "创建失败！";
        }
        commit("SetMessage", alter);
      })
      .catch(error => {
        commit("SetMessage", {
          show: true,
          type: "error",
          message: error.message
        });
      });
  },
  // eslint-disable-next-line no-empty-pattern
  CheckPhone({}, phoneNumber) {
    return request.CheckPhone(phoneNumber);
  }
};

const getters = {
  alterMessage: state => state.alterMessage,
  provinceList: state => state.provinceList,
  cityList: state => state.cityList,
  areaList: state => state.areaList
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
  }
};

export default {
  state,
  actions,
  getters,
  mutations
};
