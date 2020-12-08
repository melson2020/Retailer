//index.js
//获取应用实例
const app = getApp()
const http = app.myRequest()
Page({
  data: {
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
    hide_mask:false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    searchContent: "",
    productStorageList: []
  },
  //事件处理函数
  onLoad: function () {},
  getUserInfo: function (e) {
    var userInfo = e.detail.userInfo;
    var that = this;
    if (userInfo) {
      that.wechatLogin(e.detail)
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
  },

  wechatLogin: function (detial) {
    var that = this
    that.triggerMask(true)
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
              appUserInfo: logUser,
              hide_mask:false
            })
          }
        }).catch((res) => {
          var toptip = {
            msg: "post 请求报错" + res.errMsg,
            type: 'warn',
            show: true
          }
          that.setData({
            'toptip': toptip,
            'hide_mask':false
          })
        })
      }
    })
  },

  updateAppUser: function (e) {
    var that = this
    if (this.data.formData.loginName == "" || this.data.formData.loginName == null || this.data.formData.password == "" || this.data.formData.password == null) {
      var toptip = {
        msg: '登录信息不能为空',
        type: 'error',
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
    that.triggerMask(true)
    http.postRequest('/weChat/bindUser', {
      'rowData': userInfo,
    }).then((res) => {
      if (res.data.resultStatus === 1) {
        //成功
        var logUser = res.data.data;
        wx.setStorage({
          key: "user",
          data: JSON.stringify(logUser)
        })
        this.setData({
          appUserInfo: logUser,
          'hide_mask':false
        })
      } else {
        var toptip = {
          msg: res.data.message,
          type: 'warn',
          show: true
        }
        that.setData({
          'toptip': toptip,
          'hide_mask':false
        })
      }
    }).catch((res) => {
      var toptip = {
        msg: "post 请求报错" + res,
        type: 'warn',
        show: true
      }
      that.setData({
        'toptip': toptip,
        'hide_mask':false
      })
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

  triggerMask:function(value){
    var that=this
    that.setData({'hide_mask':value})
  },

  searchInputChange: function (e) {
    this.data.searchContent = e.detail.value
  },

  productStorageSearch: function () {
    var userStr = wx.getStorageSync('user')
    var user = JSON.parse(userStr)
    var that = this;
    if (that.data.searchContent == "" || that.data.searchContent == null) {
      var toptip = {
        msg: "请输入查询值",
        type: 'info',
        show: true
      }
      that.setData({
        'toptip': toptip
      })
      return
    }
    var params = {
      productName: that.data.searchContent,
      storeCode: user.storeCode
    };
    var token = user.retailerUserId
    that.triggerMask(true)
    http.postRequest('/weChat/productStorage', params, token).then((res) => {
      if (res.data.resultStatus === 1) {
        that.setData({
          'productStorageList': res.data.data,
          'hide_mask':false
        })
      } else if (res.data.resultStatus == 2) {
        var toptip = {
          msg: "查无结果",
          type: 'info',
          show: true
        }
        that.setData({
          'toptip': toptip,
          'hide_mask':false
        })
      }
    }).catch((msg) => {
      var toptip = {
        msg:"访问服务错误",
        type: 'warn',
        show: true
      }
      that.setData({
        'toptip': toptip,
        'hide_mask':false
      })
    })
  }
})