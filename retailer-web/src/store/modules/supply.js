import request from "../../utils/request";
import {Message} from "element-ui";

const state={
    supplyList:[],
    store:[],
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
    }
};

const getters={
    supplyList:state=>state.supplyList
};

const mutations={
    SetSupplyList(state,data){
        state.supplyList=data;
    }
};

export default{
    state,
    actions,
    getters,
    mutations
};