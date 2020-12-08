import request from './utils/request.js'
//app.js
App({
  globalData: {
    userInfo: null,
    baseUrl:'http://192.168.43.209:8080/retailer',
  },
  myRequest(){
    let that=this
    return new request(that.globalData.baseUrl);
  },
  onLaunch (options) {
    // Do something initial when launch,
    //当小程序被授权时，获取userinfo and token  未授权则去userCenter 登录
    console.log('小程序初始化')
  },
})