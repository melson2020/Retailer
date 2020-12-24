import request from "../../utils/request";
import { Message } from "element-ui";

const state = {
    productStorageRecords: []
};

const actions = {
    GetProductRecords({ commit }, params) {
        request.GetProductStorageRecords(params).then(res => {
            if (res.resultStatus == 1) {
                commit("SetProductStorageRecords", res.data)
            } else {
                Message.error(res.message);
            }

        }).catch(err => {
            Message.error(err.message);
        })
    },
    // eslint-disable-next-line no-empty-pattern
    GetDashBoardData({},params){
      return  request.GetDashBordData(params);
    }

};

const getters = {
    productStorageRecords: state => state.productStorageRecords,
};

const mutations = {
    SetProductStorageRecords(state, data) {
        state.productStorageRecords = data;
    }
};

export default {
    state,
    actions,
    getters,
    mutations
};