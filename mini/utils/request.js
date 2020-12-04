class request {
  constructor(url) {
    this._baseUrl = url;
    this._token = wx.getStorageSync('token');
    // this._header = {'Authorization': 'Bearer ' + token}
  }

  /**
   * GET类型的网络请求
   */
  getRequest(url, data) {
    return this.requestAll(url, data, 'GET')
  }

  /**
   * DELETE类型的网络请求
   */
  deleteRequest(url, data) {
    return this.requestAll(url, data, 'DELETE')
  }

  /**
   * PUT类型的网络请求
   */
  putRequest(url, data) {
    return this.requestAll(url, data, 'PUT')
  }

  /**
   * POST类型的网络请求
   */
  postRequest(url, data) {
    return this.requestAll(url, data, 'POST')
  }

  /**
   * 网络请求
   */
  requestAll(url, data, method) {
    return new Promise((resolve, reject) => {
      wx.request({
        url: this._baseUrl + url,
        data:JSON.stringify(data),
        method: method,
        dataType: 'json',
        success: (res => {
          if (res.statusCode === 200) {
            //200: 服务端业务处理正常结束
            resolve(res)
          } else {
            //其它错误，提示用户错误信息
            reject(res)
          }
        }),
        fail: (res => {       
          reject(res)
        })
      })
    })
  }
}

export default request