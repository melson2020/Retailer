/* eslint-disable no-empty-pattern */
import request from "../../utils/request";
import { Message } from "element-ui";


const state = {
    productAndStorageCount: {},
    productStorageList: [],
    previewStorageList: [],
    activeStep: 0,
    currentStorageCountTicket: {},
    updateBatchList: [],
    storageCountTickets: []
};

const actions = {
    GetProductAndStorageCount({ commit }, params) {
        request.GetProductStorageCount(params).then(res => {
            if (res.resultStatus == 1) {
                commit("SetCounts", res.data)
            } else {
                Message.error(res.message)
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })
    },
    GenerateProductStorageList({ commit }, params) {
        request.GenerateStorageList(params).then(res => {
            if (res.resultStatus == 1) {
                commit("AddStorageList", res.data)
                commit("SetStorageCount", res.data.length)
            } else {
                Message.error(res.message)
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })
    },
    GetProductStorageList({ commit }, params) {
        request.GetStorageList(params).then(res => {
            if (res.resultStatus == 1) {
                commit("SetStorageList", res.data)
            } else {
                Message.error(res.message)
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })

    },
    GetPreviewStorageList({ commit }, params) {
        request.GetStorageCountList(params).then(res => {
            if (res.resultStatus == 1) {
                commit("SetPreviewStorageList", res.data)
            } else {
                Message.error(res.message)
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })
    },
    GetBatchList({ commit }, payload) {
        request.GetBatchList(payload.params).then(res => {
            if (res.resultStatus == 1) {
                let data = { row: payload.row, list: res.data }
                commit("SetStorageDetail", data)
            } else {
                Message.error(res.message)
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })

    },
    GetBatchListForUpdate({ commit }, params) {
        request.GetBatchListForUpdate(params).then(res => {
            if (res.resultStatus == 1) {
                commit("SetUpdateBatchList", res.data)
            } else {
                Message.error(res.message)
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })
    },
    GetStorageCountTickets({ commit }, params) {
        request.GetStorageCountRecord(params).then(res => {
            if (res.resultStatus == 1) {
                commit("SetStorageCountRec", res.data)
            } else {
                Message.error(res.message)
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })
    },
    GetStorageCountTiketDetails({},params){
        return request.GetStorageCountTiketDetails(params);
    },
    ExportCountTicket({ }, parmas) {
        return request.ExportCountTicket(parmas);
    },
    DownStorageCountTicketExportExcel({ }, params) {
        return request.DownStorageCountTicketExportExcel(params)
    },
    CreateStorageCountTicket({ }, params) {
        return request.CreateStorageCountTicket(params)
    },
    SetActiveSteps({ commit }, data) {
        commit("SetActiveSteps", data)
    },
    RefreshTicket({commit}){
        commit("RefreshCurrentStorageCountTicket")
    },
    SetCurrentStorageCountTicket({ commit }, data) {
        commit("SetcurrentStorageCountTicket", data)
    },
    UpdateStorageAfterCount({ }, params) {
        return request.UpdateStorageAfterCounted(params)
    },
    SetUpdateBatchList({ commit }, data) {
        commit("SetUpdateBatchList", data)
    },
    SetTicketStatus({ commit }, data) {
        commit("SetTicketStatus", data)
    },
    UpdateProductBatchList({ }, params) {
        return request.UpdateProductBatchList(params);
    },
    UpdateCountTicket({ }, params) {
        return request.UpdateCountTicket(params)
    },
    DownLoadFile({},params){
        return request.DownLoadFile(params)
    },
    GetUnFinishedCountTickets({},params){
        return request.GetUnFinishedCountTickets(params)
    }
};

const getters = {
    productAndStorageCount: state => state.productAndStorageCount,
    productStorageList: state => state.productStorageList,
    activeStep: state => state.activeStep,
    previewStorageList: state => state.previewStorageList,
    currentStorageCountTicket: state => state.currentStorageCountTicket,
    updateBatchList: state => state.updateBatchList,
    storageCountTickets:state=>state.storageCountTickets,
};

const mutations = {
    SetCounts(state, data) {
        state.productAndStorageCount = data;
    },
    AddStorageList(state, data) {
        for (let i = 0; i < data.length; i++) {
            state.productStorageList.push(data[i])
        }
    },
    RefreshCurrentStorageCountTicket(state){
        state.currentStorageCountTicket={};
    },
    SetStorageCount(state, data) {
        state.productAndStorageCount.storageCount = data;
    },
    SetStorageList(state, data) {
        data.map(item => { item.batchList = [] })
        state.productStorageList = data;
    },
    SetPreviewStorageList(state, data) {
        state.previewStorageList = data;
    },
    SetStorageDetail(state, data) {
        let index = state.productStorageList.indexOf(data.row)
        state.productStorageList[index].batchList = data.list
    },
    SetActiveSteps(state, data) {
        state.activeStep = data;
    },
    SetcurrentStorageCountTicket(state, data) {
        state.currentStorageCountTicket = data
    },
    SetUpdateBatchList(state, data) {
        state.updateBatchList = data
    },
    SetTicketStatus(state, data) {
        state.currentStorageCountTicket.status = data;
    },
    SetStorageCountRec(state, data) {
        state.storageCountTickets = data;
    }
};

export default {
    state,
    actions,
    getters,
    mutations
};
