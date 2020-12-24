/* eslint-disable no-empty-pattern */
import request from "../../utils/request";
import {Message} from "element-ui";

const state={
  taxRateList:[],
  recordList:[],
  recordDetails:[]
};

const actions={
    SaveTicket({ }, params) {
      return request.SaveStorageInTicket(params)
    },
    GetTaxRateList({commit}){
        request.GetTaxRateList().then(res=>{
            if (res.resultStatus == 1) {
                commit("SetTaxRateList",res.data)
            } else {
                Message.error("获取税率失败")
            }
        }).catch(err =>{
            Message.error(err.message ? err.message : err)
        })
    },
    GetRecordList({commit},params){
      request.GetRecordList(params).then(res=>{
        if(res.resultStatus==1){
          commit("SetRecordList",res.data)
        }else{
          Message.error(res.message)
        }
      }).catch(err=>{
        Message.error(err.message ? err.message : err)
      })
    },
    GetRecordDetails({commit},params){
       request.GetRecordDetail(params).then(res=>{
         if(res.resultStatus==1){
           commit("SetRecordDetails",res.data)
         }else{
          Message.error(res.message)
         }
       }).catch(err=>{
        Message.error(err.message ? err.message : err)
       })
    }
};

const getters={
    taxRateList:state=>state.taxRateList,
    recordList:state=>state.recordList,
    recordDetails:state=>state.recordDetails
};

const mutations={
  SetTaxRateList(state,data){
    state.taxRateList=data;
  },
  SetRecordList(state,data){
    state.recordList=data
  },
  SetRecordDetails(state,data){
    state.recordDetails=data
  }
};

export default{
    state,
    actions,
    getters,
    mutations
};