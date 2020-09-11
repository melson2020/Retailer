import request from "../../utils/request";
import { Message } from "element-ui";

const state = {
    excelCategroyList: [],
    excelProductList: [],
    uploadFileDialog: false,
    needToRecheckList: false,
    productList:[],
};

const actions = {
    GenrateCategroyListAndProductList({ commit }, xlsJson) {
        let categroys = []
        let productions = []
        for (let index = 0; index < xlsJson.length; index++) {
            let element = xlsJson[index];
            let categroy = { id: index,categoryId:index, name: element.sheetName, comment: "", isSet: false, isRepeat: false }
            categroys.push(categroy)
            let products = element.sheet;
            for (let j = 0; j < products.length; j++) {
                let p = products[j];
                let product = { categoryId: index, name: p["别名"], type: p["型号"], specification: p["规格"], unit: p["单位"], feature: p["特征"], isRepeat: false, isSet: false }
                productions.push(product)
            }
        }
        commit("SetProductionList", productions)
        commit("SetCategroyList", categroys)
        commit("SetUploadDialog", false)
        commit("CheckDuplicateList")

    },
    SetUploadDialog({ commit }, show) {
        commit("SetUploadDialog", show)
    },
    RecheckList({ commit }, check) {
        commit("SetNeedToRecheckList", check)
    },
    DownloadProductDictTem() {
        request.DownLoadProductDictTemplate().then(res => {
            let blob = new Blob([res])
            let fileName = Date.parse(new Date()) + '.xlsx'
            if (window.navigator.msSaveOrOpenBlob) {
                navigator.msSaveBlob(blob, fileName)
            } else {
                var link = document.createElement('a')
                link.href = window.URL.createObjectURL(blob)
                link.download = fileName
                link.click()
                //释放内存
                window.URL.revokeObjectURL(link.href)
            }
        }).catch(err => {
            console.log(err)
        })
    },
    DeleteOneInImportedList({ commit }, product) {
        commit("DeleteOne", product)
        commit("CheckDuplicateList")
    },
    DeleteOneInCategroyList({ commit }, categroy) {
        commit("DeleteOneCategroy", categroy)
        commit("CheckDuplicateList")
    },
    CheckDuplicateList({commit}){
        commit("CheckDuplicateList")
    },
    // eslint-disable-next-line no-unused-vars
    SaveExcelList({ commit }, list) {
        request.SaveImportedList(list).then(res=>{
             if(res.resultStatus==1){
               Message.success("导入成功")
             }else{
               Message.warning("导入失败")
             }
        }).catch(err=>{
            Message.error(err.message?err.message:err)
        })
    },
    GetProductList({commit},params){
        request.GetProductList(params).then(res=>{
            if(res.resultStatus==1){
                commit("SetProductList",res.data)
              }else{
                Message.warning(res.message)
              }
        }).catch(err=>{
            Message.error(err.message?err.message:err)
        })
    }
};

const getters = {
    excelCategroyList: state => state.excelCategroyList,
    excelProductList: state => state.excelProductList,
    uploadFileDialog: state => state.uploadFileDialog,
    needToRecheckList: state => state.needToRecheckList,
    categroyDuplicateCount:state=>state.categroyDuplicateCount,
    productList:state=>state.productList
};

const mutations = {
    SetCategroyList(state, data) {
        state.excelCategroyList = data;
    },
    SetUploadDialog(state, data) {
        state.uploadFileDialog = data;
    },
    SetProductionList(state, data) {
        state.excelProductList = data
    },
    DeleteOne(state, data) {
        let index = state.excelProductList.indexOf(data)
        state.excelProductList.splice(index, 1)
    },
    SetNeedToRecheckList(state, data) {
        state.needToRecheckList = data;
    },
    DeleteOneCategroy(state, data) {
        let index = state.excelCategroyList.indexOf(data)
        state.excelCategroyList.splice(index, 1)
    },
    CheckDuplicateList(state) {
        if (state.excelProductList.length > 0 || state.excelCategroyList.length > 0) {
            //重置检查结果
            state.excelProductList.map(item => {item.isRepeat = false;});
            state.excelCategroyList.map(item => {item.isRepeat = false;});
            for (let i = 0; i < state.excelProductList.length; i++) {
                const pi = state.excelProductList[i];
                for (let j = i + 1; j < state.excelProductList.length; j++) {
                    const pj = state.excelProductList[j];
                    if (pj) {
                        if (pi.name === pj.name) {
                            pi.isRepeat = true;
                            pj.isRepeat = true;
                        }
                    }
                }
            }
            for (let i = 0; i < state.excelCategroyList.length; i++) {
                const pi = state.excelCategroyList[i];
                for (let j = i + 1; j < state.excelCategroyList.length; j++) {
                    const pj = state.excelCategroyList[j];
                    if (pj) {
                        if (pi.name === pj.name) {
                            pi.isRepeat = true;
                            pj.isRepeat = true;
                        }
                    }
                }
            }
        }
    },
    SetProductList(state,data){
        state.productList=data
    }
};

export default {
    state,
    actions,
    getters,
    mutations
};
