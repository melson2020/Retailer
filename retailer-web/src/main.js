/* eslint-disable no-empty */
import Vue from "vue";
import App from "./App.vue";
import "./registerServiceWorker";
import router from "./router";
import "lib-flexible";
import store from "./store/store";
//table 无限滚动插件
import elTableInfiniteScroll from 'el-table-infinite-scroll';
import {
  Card,
  Form,
  Input,
  FormItem,
  Button,
  Select,
  Icon,
  Message,
  Option,
  Container,
  Header,
  Aside,
  Main,
  Footer,
  Menu,
  Submenu,
  MenuItem,
  MenuItemGroup,
  Row,
  Col,
  Table,
  TableColumn,
  Dialog,
  Autocomplete,
  Loading,
  MessageBox,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  Upload,
  Pagination,
  Breadcrumb,
  BreadcrumbItem,
  Link,
  Tag,
  InputNumber,
  Checkbox,
  DatePicker,
  Scrollbar,
  Popover,
  CheckboxGroup,
  Divider,
  Step,
  Steps,
  Switch,
  Alert,
  RadioGroup,
  Radio,
  Tooltip
} from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

//Date 对象添加format 函数转成String
Date.prototype.format = function (fmt) {
  var o = {
      "M+": this.getMonth() + 1, //月份
      "d+": this.getDate(), //日
      "h+": this.getHours(), //小时
      "m+": this.getMinutes(), //分
      "s+": this.getSeconds(), //秒
      "q+": Math.floor((this.getMonth() + 3) / 3), //季度
      "S": this.getMilliseconds() //毫秒
  };
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var k in o)
      if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
  return fmt;
}

// 在调用 Vue.use 前，给 Message 添加 install 方法
Message.install = function (Vue) {
  Vue.prototype.$message = Message;
  Vue.prototype.$messageBox = MessageBox;
  // 除数，被除数， 保留的小数点后的位数
  Vue.prototype.NumberDiv = function (data1, data2) {// 除法
    let t1 = 0, t2 = 0, r1, r2;
    // 获取每个参数的小数的位数
    try { t1 = data1.toString().split(".")[1].length } catch (e) { }
    try { t2 = data2.toString().split(".")[1].length } catch (e) { }
    // 把所有参数的小数点去掉转为整数
    r1 = Number(data1.toString().replace(".", ""));
    r2 = Number(data2.toString().replace(".", ""));
    return ((r1 / r2) * Math.pow(10, t2 - t1));
  };
  Vue.prototype.NumberMul = function (data1, data2) {// 乘法
    let m = 0, s1 = data1.toString(), s2 = data2.toString();
    // 获取所有参数小数位长度之和
    try { m += s1.split(".")[1].length } catch (e) { }
    try { m += s2.split(".")[1].length } catch (e) { }
    // 替换掉小数点转为数字相乘再除以10的次幂值
    return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
  };
  Vue.prototype.NumberAdd = function (data1, data2) {// 加法
    let r1, r2, m;
    // 获取每个参数的小数的位数
    try { r1 = data1.toString().split(".")[1].length } catch (e) { r1 = 0 }
    try { r2 = data2.toString().split(".")[1].length } catch (e) { r2 = 0 }
    // 计算底数为10以最大小数位数为次幂的值
    m = Math.pow(10, Math.max(r1, r2));
    // 把所有参数转为整数后相加再除以次幂的值
    return (data1 * m + data2 * m) / m;
  };
  Vue.prototype.NumberSub = function (data1, data2) {// 减法
    let r1, r2, m, n;
    // 获取每个参数的小数的位数
    try { r1 = data1.toString().split(".")[1].length } catch (e) { r1 = 0 }
    try { r2 = data2.toString().split(".")[1].length } catch (e) { r2 = 0 }
    // 计算底数为10以最大小数位数为次幂的值
    m = Math.pow(10, Math.max(r1, r2));
    //精度长度以最大小数位数为长度
    n = (r1 >= r2) ? r1 : r2;
    return ((data1 * m - data2 * m) / m).toFixed(n);
  };
  String.prototype.endWith = function (endStr) {
    var d = this.length - endStr.length;
    return (d >= 0 && this.lastIndexOf(endStr) == d);
  }
  Vue.prototype.UUID=function(){
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
  }
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
Vue.use(Container);
Vue.use(Header);
Vue.use(Aside);
Vue.use(Main);
Vue.use(Footer);
Vue.use(Menu);
Vue.use(Submenu);
Vue.use(MenuItem);
Vue.use(MenuItemGroup);
Vue.use(Row);
Vue.use(Col);
Vue.use(Table);
Vue.use(TableColumn);
Vue.use(Dialog);
Vue.use(Autocomplete);
Vue.use(Loading);
Vue.use(Dropdown);
Vue.use(DropdownItem);
Vue.use(DropdownMenu);
Vue.use(Upload);
Vue.use(Pagination);
Vue.use(Breadcrumb);
Vue.use(BreadcrumbItem);
Vue.use(Link);
Vue.use(Tag);
Vue.use(InputNumber);
Vue.use(Checkbox);
Vue.use(DatePicker);
Vue.use(Scrollbar);
Vue.use(Popover);
Vue.use(CheckboxGroup);
Vue.use(Divider);
Vue.use(Step);
Vue.use(Steps);
Vue.use(Switch);
Vue.use(Alert);
Vue.use(RadioGroup);
Vue.use(Radio);
Vue.use(elTableInfiniteScroll)
Vue.use(Tooltip)

localStorage.setItem("initState", JSON.stringify(store.state));

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
