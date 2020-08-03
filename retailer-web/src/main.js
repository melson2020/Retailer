import Vue from "vue";
import App from "./App.vue";
import "./registerServiceWorker";
import router from "./router";
import "lib-flexible";
import store from "./store/store";
import {
  Card,
  Form,
  Input,
  FormItem,
  Button,
  Select,
  Icon,
  Message,
  Option
} from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

// 在调用 Vue.use 前，给 Message 添加 install 方法
Message.install = function(Vue) {
  Vue.prototype.$message = Message;
};

Vue.config.productionTip = false;
Vue.use(Card);
Vue.use(Form);
Vue.use(Input);
Vue.use(FormItem);
Vue.use(Button);
Vue.use(Select);
Vue.use(Icon);
Vue.use(Message);
Vue.use(Option);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
