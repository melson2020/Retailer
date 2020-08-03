<template>
  <div class="login" :style="loginBackground">
    <el-card class="box-card" shadow="always">
      <div slot="header" class="clearfix">
        <span class="title">欢迎登陆</span>
      </div>
      <div class="card-content">
        <el-form label-width="80px" :model="formLabelAlign">
          <el-form-item label="用户名称">
            <el-input
              placeholder="注册手机号"
              v-model="formLabelAlign.name"
            ></el-input>
          </el-form-item>
          <el-form-item label="用户密码">
            <el-input
              placeholder="用户密码"
              v-model="formLabelAlign.region"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button class="login-button" type="primary">登陆</el-button>
          </el-form-item>
        </el-form>
        <router-link to="/register">注册商户</router-link>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      formLabelAlign: {
        name: "",
        region: "",
        type: ""
      },
      loginBackground: {
        backgroundImage:
          "url(" + require("../assets/login-background.png") + ")",
        backgroundRepeat: "no-repeat"
      }
    };
  }
};
</script>
<style>
.login {
  display: flex;
  height: 100vh;
  margin: 0;
  align-items: center;
  justify-content: center;
  background-size: 100% 100%;
}
.login-button {
  width: 100%;
  font-size: 1.5em;
  font-weight: bold;
  letter-spacing: 0.3em;
}
.card-content {
  display: flex;
  flex-direction: column;
}

.title {
  color: rgb(64, 136, 231);
  font-size: 1.5em;
  font-weight: bold;
  letter-spacing: 0.5em;
}

.clearfix:after {
  display: table;
  content: "";
  clear: both;
}

.box-card {
  width: 850px;
}
</style>
