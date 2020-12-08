class request {
  constructor(url) {
    this._baseUrl = url;
  }

  /**
   * GET类型的网络请求
   */
  getRequest(url, data,token) {
    return this.requestAll(url, data, 'GET',token)
  }

  /**
   * DELETE类型的网络请求
   */
  deleteRequest(url, data,token) {
    return this.requestAll(url, data, 'DELETE',token)
  }

  /**
   * PUT类型的网络请求
   */
  putRequest(url, data,token) {
    return this.requestAll(url, data, 'PUT',token)
  }

  /**
   * POST类型的网络请求
   */
  postRequest(url, data,token) {
    return this.requestAll(url, data, 'POST',token)
  }

  /**
   * 网络请求
   */
  requestAll(url, data, method,token) {
    return new Promise((resolve, reject) => {
      wx.request({
        url: this._baseUrl + url,
        data:JSON.stringify(data),
        method: method,
        dataType: 'json',
        header:{token:token},
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