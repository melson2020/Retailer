/* eslint-disable no-unused-vars */
/* eslint-disable no-empty-pattern */
import request from "../../utils/request";
import { Message } from "element-ui";

const state = {
    productBatchList: [],
    storageOutRecordList: [],
    storageOutDetails: { }
};

const actions = {
    GetProductBatchList({ commit }, params) {
        request.GetBatchList(params).then(res => {
            if (res.resultStatus == 1) {
                commit("SetProductBatchList", res.data)
            } else {
                Message.error(res.message)
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })

    },
    ClearProductBatchList({ commit }) {
        commit("ClearProductBatchList")
    },
    SaveStorageOutTicket({ commit }, params) {
        return request.SaveStorageOutTicket(params);
    },
    GetStorageOutRecordList({ commit }, params) {
        return request.GetStorageOutRecordList(params).then(res => {
            if (res.resultStatus == 1) {
                console.log("查询记录", res.data)
                commit("SetStorageOutRecordList", res.data)
            } else {
                Message.error(res.message)
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })
    },
    GetStorageOutRecordDetails({ commit }, params) {
        return request.GetStorageOutRecordDetails(params).then(res => {
            if (res.resultStatus == 1) {
                console.log('详细记录', res.data)
                commit("SetStorageOutRecordDetails", res.data)
            } else {
                Message.error(res.message)
            }
        }).catch(error => {

        })
    }
};

const getters = {
    productBatchList: state => state.productBatchList,
    storageOutRecordList: state => state.storageOutRecordList,
    storageOutDetails:state=>state.storageOutDetails
};

const mutations = {
    SetProductBatchList(state, data) {
        data.map(item => {
            item.checked = false;
            item.outPrice = "";
            item.outCount = 0;
            item.outVat = item.vat;
            item.outTaxRate = item.taxRate
        })
        state.productBatchList = data
    },
    ClearProductBatchList(state) {
        state.productBatchList = []
    },
    SetStorageOutRecordList(state, data) {
        state.storageOutRecordList = data
    },
    SetStorageOutRecordDetails(state, data) {
        state.storageOutDetails = data
    }
};

export default {
    state,
    actions,
    getters,
    mutations
};