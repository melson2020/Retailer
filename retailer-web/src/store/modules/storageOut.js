/* eslint-disable no-unused-vars */
/* eslint-disable no-empty-pattern */
import request from "../../utils/request";
import { Message } from "element-ui";

const state = {
    productBatchList: [],
    storageOutRecordList: [],
    storageOutDetails: { },
    outBoundList:[]
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
                commit("SetStorageOutRecordDetails", res.data)
            } else {
                Message.error(res.message)
            }
        }).catch(error => {

        })
    },
    GetOutBoundList({ commit }, params) {
        console.log(params);
        return request.GetOutBoundListReq(params).then(res => {
            if (res.resultStatus == 1) {
                commit("SetOutBoundList", res.data)
            } else {
                Message.error(res.message)
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })
    },
};

const getters = {
    productBatchList: state => state.productBatchList,
    storageOutRecordList: state => state.storageOutRecordList,
    storageOutDetails:state=>state.storageOutDetails,
    outBoundList:state=>state.outBoundList
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
    SetOutBoundList(state, data) {
        state.outBoundList = data
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