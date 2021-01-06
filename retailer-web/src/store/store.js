import Vue from "vue";
import Vuex from "vuex";

import login from "./modules/login";
import employee from "./modules/employee";
import com from "./modules/com";
import supply from "./modules/supply";
import customer from "./modules/customer";
import product from "./modules/product";
import storage from "./modules/productStorage"
import storageIn from "./modules/storageIn"
import storageOut from "./modules/storageOut"
import report from "./modules/report"
import goodsReturn from "./modules/goodsReturn"
import system from "./modules/system"
import share from './modules/share'

Vue.use(Vuex);

export default new Vuex.Store({
  mutations: {
    resetAllState (state, payload) {
     Object.assign(state,payload)
    }
  },
  modules: {
    login,
    employee,
    com,
    supply,
    customer,
    product,
    storage,
    storageIn,
    storageOut,
    report,
    goodsReturn,
    system,
    share
  },
});
