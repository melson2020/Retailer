import request from "../../utils/request";
import {Message} from "element-ui";

const state={
   storageOutTickets:[]
};

const actions={
    FindOutTicketsWithSearchVale({commit},params){
         request.FindOutTicketsWithSearchVale(params).then(res=>{
            if(res.resultStatus==1){
                commit("SetStorageOutTickets",res.data)
            }else{
                Message.warning(res.message);
            }
         }).catch(err=>{
            Message.error(err.message ? err.message : err)
         })

     }
};

const getters={
    storageOutTickets: state => state.storageOutTickets,
};

const mutations={
  SetStorageOutTickets(state,data){
      state.storageOutTickets=data;
  }
};

export default{
    state,
    actions,
    getters,
    mutations
};