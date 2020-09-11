/* eslint-disable no-empty-pattern */
import request from "../../utils/request";
import {Message} from "element-ui";

const state={
  
};

const actions={
    SaveTicket({ }, params) {
        request.SaveStorageInTicket(params).then(res => {
            if (res.resultStatus == 1) {
                Message.success("保存成功")
            } else {
                Message.error(res.message)
            }
        }).catch(err => {
            Message.error(err.message ? err.message : err)
        })
    },
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