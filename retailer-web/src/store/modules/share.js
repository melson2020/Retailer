import request from "../../utils/request";
// import {Message} from "element-ui";

const state={
    shareProductStorage: {store:{storeName:''},productStorageList:[]},
};

const actions={
    // eslint-disable-next-line no-empty-pattern
    FindShareProductStorage({},params){
       return  request.FindShareProduct(params)
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