/* eslint-disable no-empty-pattern */
import request from "../../utils/request";
import {Message} from "element-ui";

const state={
  productBatchList:[],
};

const actions={
    GetProductBatchList({commit},params){
        request.GetBatchList(params).then(res => {
            if (res.resultStatus == 1) {
              commit("SetProductBatchList",res.data)
            }else {
                Message.error(res.message)
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })

    },
    ClearProductBatchList({commit}){
        commit("ClearProductBatchList")
    }
};

const getters={
    productBatchList:state=>state.productBatchList,
};

const mutations={
    SetProductBatchList(state,data){
    data.map(item=>{
        item.checked=false;
        item.outPrice="";
        item.outCount=0;
        item.outVat=item.vat;
        item.outTaxRate=item.taxRate
    })
    state.productBatchList=data
  },
  ClearProductBatchList(state){
    state.productBatchList=[]
  }
};

export default{
    state,
    actions,
    getters,
    mutations
};