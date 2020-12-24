import request from './utils/request.js'
//app.js
App({
  globalData: {
    userInfo: null,
    appUserInfo: {},
    // baseUrl: 'https://www.melson.top/api', //真实环境
    baseUrl:'http://localhost:8080/retailer',      //测试环境

  },
  myRequest() {
    let that = this
    return new request(that.globalData.baseUrl);
  },
  onLaunch(options) {
    // Do something initial when launch,
    //当小程序被授权时，获取userinfo and token  未授权则去userCenter 登录
    // console.log('小程序初始化')
  },
})