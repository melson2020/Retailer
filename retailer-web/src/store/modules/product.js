import request from "../../utils/request";

const state = {
    categroyList: [],
    productList: [],
    uploadFileDialog: false
};

const actions = {
    GenrateCategroyListAndProductList({ commit }, xlsJson) {
        let categroys = []
        let productions = []
        let count=1;
        for (let index = 0; index < xlsJson.length; index++) {
            let element = xlsJson[index];
            let categroy = { id: index, name: element.sheetName, comment: "", isSet: false }
            categroys.push(categroy)
            let products = element.sheet;
            for (let j = 0; j < products.length; j++) {
                let p = products[j];
                let product = {id:count, categroyId: index, name: p["别名"], type: p["型号"], specification: p["规格"], unit: p["单位"], feature: p["特征"],isloaded:true}
                productions.push(product)
                count++
            }
        }
        commit("SetProductionList", productions)
        commit("SetCategroyList", categroys)
        commit("SetUploadDialog", false)
    },
    SetUploadDialog({ commit }, show) {
        commit("SetUploadDialog", show)
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
    }
};

const getters = {
    categroyList: state => state.categroyList,
    productList: state => state.productList,
    uploadFileDialog: state => state.uploadFileDialog
};

const mutations = {
    SetCategroyList(state, data) {
        state.categroyList = data;
    },
    SetUploadDialog(state, data) {
        state.uploadFileDialog = data;
    },
    SetProductionList(state, data) {
        state.productList = data
    }
};

export default {
    state,
    actions,
    getters,
    mutations
};
