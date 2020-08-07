import request from "../../utils/request";
import { Message } from "element-ui";

/**
 * App通用配置
 */
const state = {
  employeeList: [],
  store: []
};

const actions = {
  GetEmployeeList({ commit }, userInfo) {
    let params = { storeCode: userInfo.storeCode };
    request
      .GetStoreEmployeeList(params)
      .then(res => {
        if (res.resultStatus == 1) {
          commit("SetEmployeeList", res.data);
        } else {
          Message.error(res.message);
        }
      })
      .catch(error => {
        let alert = error.message ? error.message : error;
        Message.error(alert);
      });
  },
  CreateEmployee({ commit }, payload) {
    request
      .CreateEmployee(payload)
      .then(res => {
        if (res.resultStatus == 1) {
          commit("AddEmployee", res.data);
        } else {
          Message.error(res.message);
        }
      })
      .catch(error => {
        let alert = error.message ? error.message : error;
        Message.error(alert);
      });
  }
};

const getters = {
  employeeList: state => state.employeeList
};

const mutations = {
  SetEmployeeList(state, data) {
    state.employeeList = data;
  },
  AddEmployee(state, data) {
    state.employeeList.push(data);
  }
};

export default {
  state,
  actions,
  getters,
  mutations
};
