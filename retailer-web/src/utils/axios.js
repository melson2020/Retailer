import axios from "axios";
import { Message } from "element-ui";
import store from "../store/store.js"

axios.defaults.timeout = 30000;
axios.defaults.headers.post["Content-Type"] =
  "application/x-www-form-urlencoded;charset=UTF-8";
axios.defaults.baseURL = "/api";                //dev 

//请求拦截器
axios.interceptors.request.use(
  config => {
    store.commit('showLoading')
    var token = JSON.parse(localStorage.getItem("userInfo"))==null?null:JSON.parse(localStorage.getItem("userInfo")).userId;
    if (token) {
      config.headers.token = token;
    }
    return config;
  },
  error => {
    store.commit('hideLoading')
    Message({
      showClose: true,
      message: "错误的传参",
      type: "error"
    });
    return Promise.reject(error);
  }
);

// 响应拦截器
axios.interceptors.response.use(
  response => {
    store.commit('hideLoading')
    if (response.status === 200) {
      return Promise.resolve(response);
    } else {
      return Promise.reject(response);
    }
  },
  // 服务器状态码不是200的情况
  error => {
    store.commit('hideLoading')
    let errorStatus = error.response ? error.response.status : undefined;
    if (errorStatus) {
      switch (errorStatus) {
        // 401: 未登录
        // 未登录则跳转登录页面，并携带当前页面的路径
        // 在登录成功后返回当前页面，这一步需要在登录页操作。
        case 401:
          this.$router.push({
            path: "/"
          });
          break;
        // 403 token过期
        // 登录过期对用户进行提示
        // 清除本地token和清空vuex中token对象
        // 跳转登录页面
        case 403:
          Message({
            showClose: true,
            message: "访问无权限",
            type: "error"
          });
          break;
        // 404请求不存在
        case 404:
          Message({
            showClose: true,
            message: "访问链接不存在",
            type: "error"
          });
          break;
        // 其他错误，直接抛出错误提示
        default:
          Message({
            showClose: true,
            message: error.message,
            type: "error"
          });
      }
    } else {
      Message({
        showClose: true,
        message: "未定义的error返回:" + error,
        type: "error"
      });
    }
    return Promise.reject(error);
  }
);

/**
 * get方法，对应get请求
 * @param {String} url [请求的url地址]
 * @param {Object} params [请求时携带的参数]
 */
export function get(url, params) {
  return new Promise((resolve, reject) => {
    axios
      .get(url, {
        params: params
      })
      .then(response => {
        resolve(response.data);
      })
      .catch(error => {
        reject(error);
      });
  });
}
/**
 * post方法，对应post请求
 * @param {String} url [请求的url地址]
 * @param {Object} params [请求时携带的参数]
 */
export function post(url, params) {
  return new Promise((resolve, reject) => {
    axios
      .post(url, params)
      .then(res => {
        resolve(res.data);
      })
      .catch(err => {
        reject(err.data);
      });
  });
}

export function exportExcel(url, params) {
  return new Promise((resolve, reject) => {
    axios({
      method: 'post',
      url: url, // 请求地址
      data: params, // 参数
      responseType: 'blob' // 表明返回服务器返回的数据类型
    }).then(
      response => {
        resolve(response.data)
      },
      err => {
        reject(err)
      }
    )
  })
}
