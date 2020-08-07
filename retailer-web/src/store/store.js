import Vue from "vue";
import Vuex from "vuex";

import login from "./modules/login";
import employee from "./modules/employee";

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    login,
    employee
  }
});
