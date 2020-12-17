import request from "../../utils/request";
import { Message } from "element-ui";

const state = {
    storageOutTickets: [],
    GoodsReturn_outTicketForReturn: {}
};

const actions = {
    FindOutTicketsWithSearchVale({ commit }, params) {
        request.FindOutTicketsWithSearchVale(params).then(res => {
            if (res.resultStatus == 1) {
                commit("SetStorageOutTickets", res.data)
            } else {
                Message.warning(res.message);
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })

    },
    FindOutTicketForGoodsReturn({ commit }, params) {
        request.FindOutTicketForGoodsReturn(params).then(res => {
            if (res.resultStatus == 1) {
                commit("GoodsReturn_SetOutTicketForReturn", res.data)
            } else {
                Message.warning(res.message);
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })
    },
    // eslint-disable-next-line no-empty-pattern
    SaveGoodsReturnRecords({ }, params) {
        return request.SaveGoodsReturnRecords(params)
    }
};

const getters = {
    storageOutTickets: state => state.storageOutTickets,
    GoodsReturn_outTicketForReturn: state => state.GoodsReturn_outTicketForReturn
};

const mutations = {
    SetStorageOutTickets(state, data) {
        state.storageOutTickets = data;
    },
    GoodsReturn_SetOutTicketForReturn(state, data) {
        state.GoodsReturn_outTicketForReturn = data;
    }
}

export default {
    state,
    actions,
    getters,
    mutations
};