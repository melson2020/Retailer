import request from "../../utils/request";
// import {Message} from "element-ui";

const state={
    shareProductStorage: {},
};

const actions={
    FindShareProductStorage({ },params){
        return request.FindShareProduct(params);
    },
    SetShareProductStorage({ commit }, data) {
        commit("ComShareProductStorage", data);
    },
};

const getters={
    shareProductStorage: state => state.shareProductStorage
};

const mutations={
    ComShareProductStorage(state,data){
        state.shareProductStorage=data;
    }
};

export default{
    state,
    actions,
    getters,
    mutations
};