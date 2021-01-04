/* eslint-disable no-unused-vars */
/* eslint-disable no-empty-pattern */
import request from "../../utils/request";
import { Message } from "element-ui";

const state = {
    productBatchList: [],
    storageOutRecordList: [],
    storageOutDetails: {},
    outBoundDelivery:{},
    outTicketInfo: {}
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
        request.GetStorageOutRecordList(params).then(res => {
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
        request.GetStorageOutRecordDetails(params).then(res => {
            if (res.resultStatus == 1) {
                commit("SetStorageOutRecordDetails", res.data)
            } else {
                Message.error(res.message)
            }
        }).catch(error => {
            Message.error(error.message ? error.message : error)
        })
    },
    GetOutBoundList({ commit }, params) {
        request.GetOutBoundListReq(params).then(res => {
            if (res.resultStatus == 1) {
                commit("SetOutBoundList", res.data)
            } else {
                Message.error(res.message)
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })
    },
    GetOutTicketInfo({ commit }, params) {
        request.GetOutTicketInfo(params).then(res => {
            if (res.resultStatus == 1) {
                commit("SetOutTicketInfo", res.data)
            } else {
                Message.error(res.message)
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })
    }
};

const getters = {
    productBatchList: state => state.productBatchList,
    storageOutRecordList: state => state.storageOutRecordList,
    storageOutDetails: state => state.storageOutDetails,
    outBoundDelivery: state => state.outBoundDelivery,
    outTicketInfo:state=>state.outTicketInfo
};

const mutations = {
    SetProductBatchList(state, data) {
        data.map(item => {
            item.checked = false;
            item.outPrice = "";
            item.customerName = "";
            item.outCount = 0;
            item.outVat = item.vat === null ? 0 : item.vat,
                item.outTaxRate = item.taxRate === null ? '' : item.taxRate
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
        state.outBoundDelivery = data
    },
    SetStorageOutRecordDetails(state, data) {
        state.storageOutDetails = data
    },
    SetOutTicketInfo(state, data) {
        state.outTicketInfo = data
    }
};

export default {
    state,
    actions,
    getters,
    mutations
};