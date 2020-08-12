import Vue from "vue";
import Vuex from "vuex";

import login from "./modules/login";
import employee from "./modules/employee";
import com from "./modules/com"
import supply from "./modules/supply"

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    login,
    employee,
    com,
    supply
  }
});
