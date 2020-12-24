
//获取应用实例
const app = getApp()
const http = app.myRequest()
Page({
  data: {
    //微信账户数据
    userInfo: {},
    //app账户数据，包含关联的retailer 用户信息
    appUserInfo: app.globalData.appUserInfo,
    hasUserInfo: false,
    toptip: {},
    hide_mask:false,
    searchContent: "",
    outBoundList: []
  },
  goLogin:function name(params) {
    wx.switchTab({
       url: '../index/index',
       success: (res) => {
       }
    })
 },


  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var that = this
    let login = that.checkLoginStatus()
    if (login.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true,
        appUserInfo: app.globalData.appUserInfo,
        hide_mask:false
      })
    }
  },
   checkLoginStatus: function () {
      let that = this;
      let res=app.globalData
      return res
   },
   searchInputChange: function (e) {
    this.data.searchContent = e.detail.value
  },
  triggerMask:function(value){
    var that=this
    that.setData({'hide_mask':value})
  },
   outBoundSearch: function () {
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
      key: that.data.searchContent,
      storeCode: user.storeCode
    };
    var token = user.retailerUserId
    that.triggerMask(true)
    http.postRequest('/weChat/outBoundList', params, token).then((res) => {
      if (res.data.resultStatus === 1) {
        that.setData({
          'outBoundList': res.data.data,
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
  },
  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})