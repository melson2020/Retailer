import request from "../../utils/request";

/**
 * App通用配置
 */
const state = {
  provinceList: [],
  cityList: [],
  areaList: [],
  errorMessage: { show: false, message: "" }
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
          commit("SetErrorMessage", { show: true, message: error.message });
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
        commit("SetErrorMessage", { show: true, message: error.message });
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
        commit("SetErrorMessage", { show: true, message: error.message });
      });
  }
};

const getters = {
  errorMessage: state => state.errorMessage,
  provinceList: state => state.provinceList,
  cityList: state => state.cityList,
  areaList: state => state.areaList
};

const mutations = {
  SetErrorMessage(state, data) {
    state.errorMessage = data;
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
