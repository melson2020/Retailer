import request from "../../utils/request";
import { Message } from "element-ui";


const state = {
    productAndStorageCount: {},
    productStorageList: []
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
            }else {
                Message.error(res.message)
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })

    },
    GetBatchList({commit},payload){
        request.GetBatchList(payload.params).then(res => {
            if (res.resultStatus == 1) {
              let data={row:payload.row,list:res.data}
              commit("SetStorageDetail",data)
            }else {
                Message.error(res.message)
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })

    }
};

const getters = {
    productAndStorageCount: state => state.productAndStorageCount,
    productStorageList: state => state.productStorageList
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
    SetStorageCount(state, data) {
        state.productAndStorageCount.storageCount = data;
    },
    SetStorageList(state, data) {
        data.map(item=>{item.batchList=[]})
        state.productStorageList=data;
    },
    SetStorageDetail(state,data){
        let index=state.productStorageList.indexOf(data.row)
        state.productStorageList[index].batchList=data.list
    }
};

export default {
    state,
    actions,
    getters,
    mutations
};
