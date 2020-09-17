import request from "../../utils/request";
import {Message} from "element-ui";

const state={
    supplyList:[]
};

const actions={
    GetSupplyList({commit},userInfo){
        let params={storeCode:userInfo.storeCode};
        request
            .FetchSupplyListReq(params)
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
    SaveSupply({ },newSupply){
        return request.SaveSupplyReq(newSupply);
    },
    PushSupplyList({commit}, newSupply){
        commit("PushSupplyList",newSupply);
    },
    DeleteSupply({commit},supply){
        request
        .DeleteSupplyReq(supply)
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
    // eslint-disable-next-line no-empty-pattern
    QuerySupplyObj({},Supply){
        return request.QuerySupplyObjReq(Supply);
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