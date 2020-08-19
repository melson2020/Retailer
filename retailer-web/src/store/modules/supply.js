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
    SaveSupply({ },newSupply){
        return request.SaveSupply(newSupply);
    },
    PushSupplyList({commit}, newSupply){
        commit("PushSupplyList",newSupply);
    },
    DeleteSupply({commit},supply){
        request
        .DeleteSupply(supply)
        .then(res=>{
            if(res.resultStatus==1){
                commit("SpliceSupplyList",supply)   //更新前台表格数据
                Message.info("删除成功")
            }
            else{
                Message.warning("删除失败："+res.message)
            }
        })
        .catch(error=>{
            let al=error.message?error.message:error
            Message.error(al)
        })

    },
    QuerySupplyObj({},Supply){
        return request.QuerySupplyObj(Supply);
    }
};

const getters={
    supplyList:state=>state.supplyList
};

const mutations={
    SetSupplyList(state,data){
        state.supplyList=data;
    },
    PushSupplyList(state,data){
        state.supplyList.push(data);
    },
    SpliceSupplyList(state,data){
        state.supplyList.splice(data.index,1)
    }
};

export default{
    state,
    actions,
    getters,
    mutations
};