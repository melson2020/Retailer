import request from "../../utils/request";
import {Message} from "element-ui";

const state={
    customerList:[]
};

const actions={
    GetCustomerList({commit},userInfo){
        let params={storeCode:userInfo.storeCode};
        request
            .FetchCustomerListReq(params)
            .then(res=>{
                if(res.resultStatus==1){
                    commit("SetCustomerList",res.data);
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
    SaveCustomer({ },newCustomer){
        return request.SaveCustomerReq(newCustomer);
    },
    PushCustomerList({commit}, newCustomer){
        commit("PushCustomerList",newCustomer);
    },
    // DeleteCustomer({commit},Customer){
    //     request
    //     .DeleteCustomerReq(Customer)
    //     .then(res=>{
    //         if(res.resultStatus==1){
    //             commit("SpliceCustomerList",Customer)   //更新前台表格数据
    //             Message.info("删除成功")
    //         }
    //         else{
    //             Message.warning("删除失败："+res.message)
    //         }
    //     })
    //     .catch(error=>{
    //         let al=error.message?error.message:error
    //         Message.error(al)
    //     })

    // },
    // eslint-disable-next-line no-empty-pattern
    QueryCustomerObj({},Customer){
        return request.QueryCustomerObjReq(Customer);
    }
};

const getters={
    customerList:state=>state.customerList
};

const mutations={
    SetCustomerList(state,data){
        state.customerList=data;
    },
    PushCustomerList(state,data){
        state.customerList.push(data);
    },
    SpliceCustomerList(state,data){
        let index=state.customerList.indexOf(data);
        state.customerList.splice(index,1)
    }
};

export default{
    state,
    actions,
    getters,
    mutations
};