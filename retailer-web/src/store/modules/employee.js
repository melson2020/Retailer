import request from "../../utils/request";
import { Message } from "element-ui";


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
  CreateEmployee({ }, payload) {
    return request.CreateEmployee(payload);
  },
  AddEmployee({ commit }, employee) {
    commit("AddEmployee", employee);
  },
  
  // eslint-disable-next-line no-empty-pattern
  CheckLoginName({ }, palyload) {
    return request.CheckLoginName(palyload);
  },
  // eslint-disable-next-line no-empty-pattern
  UpdateEmployee({ }, employee) {
    return request.UpdateEmployee(employee);
  },
  ReplaceEmployee({ commit }, employee) {
    commit("UpdateEmployee", employee);
  },
  DeleteEmployee({ commit }, employee) {
    request.DeleteEmployee(employee).then(res => {
      if (res.resultStatus == 1) {
        commit("DeleteEmployee", employee)
        Message.info("删除成功")
      } else {
        Message.warning("删除失败:" + res.message)
      }
    }).catch(error => {
      let al = error.message ? error.message : error
      Message.error(al)
    })
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
  },
  UpdateEmployee(state, data) {
    let updatedEmp = state.employeeList[data.index]
    updatedEmp.userName = data.userName;
    updatedEmp.phone = data.phone;
    updatedEmp.gender = data.gender;
    updatedEmp.permission = data.permission;
  },
  DeleteEmployee(state, data) {
    state.employeeList.splice(data.index, 1)
  }
};

export default {
  state,
  actions,
  getters,
  mutations
};
