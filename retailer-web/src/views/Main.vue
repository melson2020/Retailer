<template>
  <el-container class="main-container">
    <el-aside width="auto">
      <el-menu
        default-active="1-1"
        class="el-menu-vertical"
        background-color="#2c61b1"
        text-color="#fff"
        router
        active-text-color="#ffd04b"
        :collapse="isCollapse"
      >
        <template v-for="menu in userInfo.menuList">
          <el-submenu v-if="menu.subs === 1" :key="menu.id" :index="menu.index">
            <template slot="title">
              <i :class="'el-icon-' + menu.icon"></i>
              <span>{{ menu.name }}</span>
            </template>
            <el-menu-item
              class="el-submenu-item"
              v-for="subMenu in menu.subMenus"
              :index="subMenu.index"
              :key="subMenu.id"
            >{{ subMenu.name }}</el-menu-item>
          </el-submenu>
          <el-menu-item v-else :index="menu.index" :key="menu.id">
            <i :class="'el-icon-' + menu.icon"></i>
            <span slot="title">{{ menu.name }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header height="auto">
        <div>
          <i class="main-icon main-icon-left" :class="menuArrow" @click="menuCollapseChange"></i>
          <el-dropdown trigger="click" class="main-icon-right">
            <span class="el-dropdown-link main-user-info">
              <i class="el-icon-user main-icon main-icon-right"></i>
              {{ userInfo.userName }}
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="logout">注销</el-dropdown-item>
              <el-dropdown-item @click.native="resetPass">修改密码</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-dialog
            title="重置密码"
            :visible.sync="restPassFormVisible"
            @close="dailogOnClose('resetform')"
            :close-on-click-modal="false"
            :show-close="false"
          >
            <el-form :model="systemUser" :rules="rules" ref="resetform">
              <el-form-item label="登录账户" label-width="120px" prop="loginName">
                <el-input v-model="systemUser.loginName" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item label="用户密码" label-width="120px" prop="oldPass">
                <el-input type="password" v-model="systemUser.oldPass" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item label="用户新密码" label-width="120px" prop="newPass">
                <el-input type="password" v-model="systemUser.newPass" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item label="确认新密码" label-width="120px" prop="checkNewPass">
                <el-input type="password" v-model="systemUser.checkNewPass" autocomplete="off"></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="restPassFormVisible = false">取 消</el-button>
              <el-button type="primary" @click="resetConfirm('resetform')">确 定</el-button>
            </div>
          </el-dialog>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>
<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
export default {
  data() {
    var validateNewPass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.systemUser.checkNewPass !== "") {
          this.$refs.resetform.validateField("checkNewPass");
        }
        callback();
      }
    };
    var validateCheckNewPass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.systemUser.newPass) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      isCollapse: false,
      menuArrow: "el-icon-s-fold",
      restPassFormVisible: false,
      systemUser: {
        userId: "",
        oldPass: "",
        newPass: "",
        checkNewPass: "",
        loginName: ""
      },
      rules: {
        loginName: [
          { required: true, message: "请输入登录账户", trigger: "blur" }
        ],
        oldPass: [{ required: true, message: "请输入密码", trigger: "blur" }],
        newPass: [{ validator: validateNewPass, trigger: "blur" }],
        checkNewPass: [{ validator: validateCheckNewPass, trigger: "blur" }]
      }
    };
  },
  computed: {
    ...mapGetters(["userInfo"])
  },
  methods: {
    ...mapActions({
      SetLoginStatus: "SetLoginStatus",
      ResetPassword: "ResetPassword"
    }),
    menuCollapseChange() {
      this.isCollapse = !this.isCollapse;
      this.menuArrow = this.isCollapse ? "el-icon-s-unfold" : "el-icon-s-fold";
    },
    logout: function() {
      this.$messageBox
        .confirm("确定退出？", "系统退出", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "info"
        })
        .then(() => {
          localStorage.removeItem("userInfo");
          this.$router.replace({ path: "/" });
        })
        .catch(e => e);
    },
    resetPass: function() {
      this.systemUser.loginName = this.userInfo.loginName;
      this.systemUser.userId = this.userInfo.userId;
      this.restPassFormVisible = true;
    },
    resetConfirm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.ResetPassword(this.systemUser)
            .then(res => {
              if (res.resultStatus == 1) {
                this.$message.success("重置成功");
                this.userInfo.loginName=this.systemUser.loginName
                this.SetLoginStatus(this.userInfo)
                localStorage.setItem("userInfo",JSON.stringify(this.userInfo))
                this.restPassFormVisible = false;
              } else {
                this.$message.error("密码错误");
              }
            })
            .catch(error => {
              this.$message.error(error.message ? error.message : error);
            });
        } else {
          this.$message.warning("请填写准确信息");
          return false;
        }
      });
    },
    dailogOnClose: function(formName) {
      this.$refs[formName].resetFields()
    }
  },
  beforeMount: function() {
    let userInfo = localStorage.getItem("userInfo");
    if (userInfo) {
      this.SetLoginStatus(JSON.parse(userInfo));
    }
  }
};
</script>
<style>
.el-menu-vertical:not(.el-menu--collapse) {
  width: 400px;
  min-height: 400px;
  letter-spacing: 10px;
  font-size: 25px;
  font-weight: bold;
}
.el-submenu-item {
  background-color: #5d89d4 !important;
  letter-spacing: 6px;
  font-size: 18px;
}
.el-submenu-item:hover {
  outline: 0 !important;
  background: #3c78cc !important;
}
.el-submenu-item.is-active {
  color: #ffd04b !important;
}

.el-header {
  border-bottom: rgb(224, 222, 222) solid 0.1px;
  padding: 0;
  margin: 0;
  padding-right: 20px;
}

.el-aside {
  background-color: #2c61b1;
  text-align: center;
}

.el-main {
  background-color: white;
  text-align: center;
  padding: 12px;
}

.main-container {
  height: 100vh;
}
.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

.main-icon-left {
  float: left;
}
.main-icon-right {
  float: right;
}
.main-icon {
  font-size: 50px;
  margin: 20px;
  color: rgb(180, 176, 176);
}
.main-user-info {
  float: right;
  color: #909399;
  height: 100px;
  margin-left: 10px;
  margin-right: 10px;
  display: flex;
  align-items: center;
  font-size: 25px;
  letter-spacing: 5px;
}
</style>
