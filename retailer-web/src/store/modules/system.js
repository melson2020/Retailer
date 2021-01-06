import request from "../../utils/request";
import {Message} from "element-ui";

const state={
    systemSharePath:{}
};

const actions={
    CreateSharePath({commit},params){
        request.CreateSharePath(params).then(res=>{
            if(res.resultStatus==1){
                commit("SetSystemSharePath",res.data);
                Message.success("创建成功");
            }
            else{
                Message.error(res.message);
            }
        }).catch(err=>{
            Message.error(err.message ? err.message : err)
        })
    },
    FindSharePath({commit},params){
        request.FindSharePath(params).then(res=>{
            if(res.resultStatus==1){
                commit("SetSystemSharePath",res.data);
            }
            else{
                Message.error(res.message);
            }
        }).catch(err=>{
            Message.error(err.message ? err.message : err)
        })
    }
};

const getters={
    systemSharePath: state => state.systemSharePath,
};

const mutations={
    SetSystemSharePath(state,data){
        state.systemSharePath=data;
    }
};

export default{
    state,
    actions,
    getters,
    mutations
};