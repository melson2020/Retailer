import request from "../../utils/request";

const state = {
    excelCategroyList: [],
    excelProductList: [],
    uploadFileDialog: false,
    needToRecheckList:false
};

const actions = {
    GenrateCategroyListAndProductList({ commit }, xlsJson) {
        let categroys = []
        let productions = []
        for (let index = 0; index < xlsJson.length; index++) {
            let element = xlsJson[index];
            let categroy = { id: index, name: element.sheetName, comment: "", isSet: false }
            categroys.push(categroy)
            let products = element.sheet;
            for (let j = 0; j < products.length; j++) {
                let p = products[j];
                let product = { categroyId: index, name: p["别名"], type: p["型号"], specification: p["规格"], unit: p["单位"], feature: p["特征"],isRepeat:false,isSet:false}
                productions.push(product)
            }
        }
        commit("SetProductionList", productions)
        commit("SetCategroyList", categroys)
        commit("SetUploadDialog", false)
        commit("SetNeedToRecheckList", true)
        
    },
    SetUploadDialog({ commit }, show) {
        commit("SetUploadDialog", show)
    },
    RecheckList({ commit },check){
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
    DeleteOneInImportedList({commit},product){
      commit("DeleteOne",product)
      commit("SetNeedToRecheckList", true)
    },
    // eslint-disable-next-line no-unused-vars
    SaveExcelList({commit},list){
        console.log("保存数据值服务器")
        console.log(list)
        console.log(this.state.excelProductList)
    }
};

const getters = {
    excelCategroyList: state => state.excelCategroyList,
    excelProductList: state => state.excelProductList,
    uploadFileDialog: state => state.uploadFileDialog,
    needToRecheckList:state=>state.needToRecheckList
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
    DeleteOne(state,data){
        let index=state.excelProductList.indexOf(data)
        state.excelProductList.splice(index,1)
    },
    SetNeedToRecheckList(state,data){
        state.needToRecheckList=data;
    }
};

export default {
    state,
    actions,
    getters,
    mutations
};
