//index.js
//获取应用实例
const app = getApp()
const http = app.myRequest()
Page({
  data: {
    motto: 'Hello World',
    //微信账户数据
    userInfo: {},
    //app账户数据，包含关联的retailer 用户信息
    appUserInfo: {},
    hasUserInfo: false,
    toptip: {},
    formData: {
      loginName: '',
      password: ''
    },
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },
  //事件处理函数
  onLoad: function () {
    // if (app.globalData.userInfo) {
    //   console.log(1)
    //   this.setData({
    //     userInfo: app.globalData.userInfo,
    //     hasUserInfo: true
    //   })
    // } else if (this.data.canIUse){
    //   // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
    //   // 所以此处加入 callback 以防止这种情况
    //   console.log(2)
    //   app.userInfoReadyCallback = res => {
    //     this.setData({
    //       userInfo: res.userInfo,
    //       hasUserInfo: true
    //     })
    //   }
    // } else {
    //   // 在没有 open-type=getUserInfo 版本的兼容处理
    //   console.log(3)
    //   wx.getUserInfo({
    //     success: res => {
    //       app.globalData.userInfo = res.userInfo
    //       this.setData({
    //         userInfo: res.userInfo,
    //         hasUserInfo: true
    //       })
    //     }
    //   })
    // }
  },
  getUserInfo: function (e) {
    var userInfo = e.detail.userInfo;
    var that = this;
    if (userInfo) {
      this.wechatLogin(e.detail)
    } else {
      let toptip = {
        msg: '未授权',
        type: 'warn',
        show: true
      }
      that.setData({
        'toptip': toptip
      })
    }
    // app.globalData.userInfo = e.detail.userInfo
    // this.setData({
    //   userInfo: e.detail.userInfo,
    //   hasUserInfo: true
    // })
  },

  wechatLogin: function (detial) {
    wx.login({
      success: (res) => {
        http.postRequest('/weChat/login', {
          'code': res.code,
          'rowData': detial.rawData,
        }).then((res) => {
          if (res.data.resultStatus === 1) {
            var logUser = res.data.data;
            wx.setStorage({
              key: "user",
              data: JSON.stringify(logUser)
            })
            app.globalData.userInfo = detial.userInfo
            this.setData({
              userInfo: detial.userInfo,
              hasUserInfo: true,
              appUserInfo: logUser
            })
          }
        }).catch((res) => {
          console.log("post 请求报错", res)
        })
      }
    })
  },

  updateAppUser: function (e) {
    var that=this
    if(this.data.formData.loginName==""||this.data.formData.loginName==null||this.data.formData.password==""||this.data.formData.password==null){
      var toptip = {
        msg: '登录信息不能为空',
        type: 'warn',
        show: true
      }
      that.setData({
        'toptip': toptip
      })
      return
    }
    var params = {
      nickName: this.data.userInfo.nickName,
      loginName: this.data.formData.loginName,
      password: this.data.formData.password
    }
    var userInfo = JSON.stringify(params)
    http.postRequest('/weChat/bindUser', {
      'rowData': userInfo,
    }).then((res) => {
      if (res.data.resultStatus === 1){
        //成功
        var logUser = res.data.data;
        wx.setStorage({
          key: "user",
          data: JSON.stringify(logUser)
        })
        this.setData({
          appUserInfo: logUser
        })
      }else{
        var toptip = {
          msg: res.data.message,
          type: 'warn',
          show: true
        }
        that.setData({
          'toptip': toptip
        })
      }
    }).catch((res) => {
      console.log("post 请求报错", res)
    })
  },
  formInputChange(e) {
    const {
      field
    } = e.currentTarget.dataset
    this.setData({
      [`formData.${field}`]: e.detail.value
    })
  },
})