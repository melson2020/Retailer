<template>
  <div class="login" :style="loginBackground">
    <el-card class="login-box-card" shadow="always">
      <div slot="header" class="login-clearfix">
        <span class="login-title">欢迎登陆</span>
      </div>
      <div class="login-card-content">
        <el-form
          ref="loginForm"
          label-width="80px"
          :model="loginUser"
          :rules="rules"
        >
          <el-form-item label="用户名称" prop="loginName">
            <el-input
              placeholder="注册手机号"
              v-model="loginUser.loginName"
            ></el-input>
          </el-form-item>
          <el-form-item label="用户密码" prop="password">
            <el-input
              type="password"
              placeholder="用户密码"
              v-model="loginUser.password"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              class="login-button"
              type="primary"
              @click="userLogin('loginForm')"
              >登陆</el-button
            >
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
        password: "",
        loginFrom: "",
      },
      loginBackground: {
        backgroundImage:
          "url(" + require("../assets/login-background.png") + ")",
        backgroundRepeat: "no-repeat",
      },
      rules: {
        loginName: [{ required: true, message: "请输入账户", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },
    };
  },
  methods: {
    ...mapActions({
      UserLogin: "UserLogin",
      SetLoginStatus: "SetLoginStatus",
      ReSetAllStates: "ReSetAllStates",
    }),
    userLogin(formName) {
      // eslint-disable-next-line no-undef
      this.loginUser.loginFrom = JSON.stringify({
        user: this.loginUser.loginName,
        // eslint-disable-next-line no-undef
        ip: returnCitySN["cip"],
        // eslint-disable-next-line no-undef
        city: returnCitySN["cname"],
      });
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var initState = JSON.parse(localStorage.getItem("initState"));
          if (initState) {
            this.UserLogin(this.loginUser)
              .then((res) => {
                if (res.resultStatus == 1) {
                  if (res.data) {
                    localStorage.setItem("userInfo", JSON.stringify(res.data));

                    this.SetLoginStatus(res.data);
                    this.ReSetAllStates(initState);
                    this.$router.push({ path: "/main/productStorage" });
                  } else {
                    this.$message.error("账户密码不正确");
                  }
                } else {
                  this.$message.error(res.message);
                }
              })
              .catch((error) => {
                let messageStr =
                  typeof error == "undefined" || error == null
                    ? error
                    : error.message;
                this.$message.error(messageStr);
              });
          } else {
             this.$message.error("缺少必要cookie,页面即将刷新");
             location.reload()
          }
        } else {
          this.$message.warning("请填写准确信息");
          return false;
        }
      });
    },
  },
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
.login-card-content {
  display: flex;
  flex-direction: column;
}

.login-title {
  color: rgb(64, 136, 231);
  font-size: 2em;
  font-weight: bold;
  letter-spacing: 0.5em;
}

.login-clearfix:after {
  display: table;
  content: "";
  clear: both;
}

.login-box-card {
  width: 850px;
}
</style>
