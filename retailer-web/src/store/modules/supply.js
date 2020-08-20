import request from "../../utils/request";
import {Message} from "element-ui";

const state={
    supplyList:[],
    closeDialog:false
};

const actions={
    GetSupplyList({commit},userInfo){
        let params={storeCode:userInfo.storeCode};
        request
            .FetchSupplyList(params)
            .then(res=>{
                if(res.resultStatus==1){
                    commit("SetSupplyList",res.data);
                }
                else{
                    Message.error(res.message);
                }
            })
            .catch(error=>{
                let alert=error.message?error.message:error;
                Message.error(alert);
            })
    },
    // eslint-disable-next-line no-empty-pattern
    CreateSupply({},newSupply){
        return request.CreateSupply(newSupply);
    },
    PushSupplyList({commit}, newSupply){
        commit("PushSupply",newSupply);
    }
};

const getters={
    supplyList:state=>state.supplyList
};

const mutations={
    SetSupplyList(state,data){
        state.supplyList=data;
    },
    PushSupply(state,data){
        state.supplyList.push(data);
    }
};

export default{
    state,
    actions,
    getters,
    mutations
};