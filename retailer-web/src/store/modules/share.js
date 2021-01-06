import request from "../../utils/request";
import {Message} from "element-ui";

const state={
    
};

const actions={
    // eslint-disable-next-line no-unused-vars
    FindShareProductStorage({commit},params){
           request.FindShareProduct(params).then(res=>{
            if (res.resultStatus == 1) {
               console.log(res.data)
            } else {
                Message.error(res.message);
            }
           }).catch(err=>{
            Message.error(err.message);
           })
    }
};

const getters={

};

const mutations={

};

export default{
    state,
    actions,
    getters,
    mutations
};