import Vue from "vue";
import Vuex from "vuex";

import login from "./modules/login";
import employee from "./modules/employee";
import com from "./modules/com";
import supply from "./modules/supply";
import product from "./modules/product";
import storage from "./modules/productStorage"
import storageIn from "./modules/storageIn"
import storageOut from "./modules/storageOut"

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
    product,
    storage,
    storageIn,
    storageOut
  },
});
