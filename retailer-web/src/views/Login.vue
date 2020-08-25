<template>
  <div class="login" :style="loginBackground">
    <el-card class="box-card" shadow="always">
      <div slot="header" class="clearfix">
        <span class="title">欢迎登陆</span>
      </div>
      <div class="card-content">
        <el-form ref="loginForm" label-width="80px" :model="loginUser" :rules="rules">
          <el-form-item label="用户名称" prop="loginName">
            <el-input placeholder="注册手机号" v-model="loginUser.loginName"></el-input>
          </el-form-item>
          <el-form-item label="用户密码" prop="password">
            <el-input type="password" placeholder="用户密码" v-model="loginUser.password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button class="login-button" type="primary" @click="userLogin('loginForm')">登陆</el-button>
          </el-form-item>
        </el-form>
        <router-link to="/register">注册商户</router-link>
      </div>
    </el-card>
  </div>
</template>

<script>
import { mapActions } from "vuex";
export default {
  data() {
    return {
      loginUser: {
        loginName: "",
        password: ""
      },
      loginBackground: {
        backgroundImage:
          "url(" + require("../assets/login-background.png") + ")",
        backgroundRepeat: "no-repeat"
      },
      rules: {
        loginName: [{ required: true, message: "请输入账户", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }]
      }
    };
  },
  methods: {
    ...mapActions({
      UserLogin: "UserLogin",
      SetLoginStatus: "SetLoginStatus"
    }),
    userLogin(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.UserLogin(this.loginUser)
            .then(res => {
              console.log(res)
              if (res.resultStatus == 1) {
                if (res.data) {
                  localStorage.setItem("userInfo", JSON.stringify(res.data));
                  this.SetLoginStatus(res.data);
                  this.$router.push({ path: "/main" });
                } else {
                  this.$message.error("账户密码不正确");
                }
                }
              else{
                this.$message.error(res.message);
              }
            })
            .catch(error => {
              let messageStr = error.message ? error.message : error;
              this.$message.error(messageStr);
            });
        } else {
          this.$message.warning("请填写准确信息");
          return false;
        }
      });
    }
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
