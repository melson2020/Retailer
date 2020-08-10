import request from "../../utils/request";
import { Message } from "element-ui";

/**
 * App通用配置
 */
const state = {
  employeeList: [],
  store: [],
  closeDialog: false,
  permissionList: []
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
  GetPermissList({ commit }) {
    request
      .GetPermissionList()
      .then(res => {
        if (res.resultStatus == 1) {
          console.log("权限列表");
          console.log(res.data);
          commit("SetPermissionList", res.data);
        } else {
          Message.error(res.message);
        }
      })
      .catch(error => {
        let alert = error.message ? error.message : error;
        Message.error(alert);
      });
  },
  // eslint-disable-next-line no-empty-pattern
  CreateEmployee({}, payload) {
    return request.CreateEmployee(payload);
  },
  AddEmployee({ commit }, employee) {
    commit("AddEmployee", employee);
  },
  // eslint-disable-next-line no-empty-pattern
  CheckLoginName({}, palyload) {
    return request.CheckLoginName(palyload);
  }
};

const getters = {
  employeeList: state => state.employeeList,
  permissionList: state => state.permissionList
};

const mutations = {
  SetEmployeeList(state, data) {
    state.employeeList = data;
  },
  AddEmployee(state, data) {
    state.employeeList.push(data);
  },
  SetPermissionList(state, data) {
    state.permissionList = data;
  }
};

export default {
  state,
  actions,
  getters,
  mutations
};
