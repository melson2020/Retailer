<template>
  <el-container class="container">
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
          <i class="icon icon-left" :class="menuArrow" @click="menuCollapseChange"></i>
          <i class="el-icon-setting icon icon-right"></i>
          <div class="user-info">{{ userInfo.userName }}</div>
          <i class="el-icon-user icon icon-right"></i>
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
    return {
      isCollapse: false,
      menuArrow: "el-icon-s-fold"
    };
  },
  computed: {
    ...mapGetters(["userInfo"])
  },
  methods: {
    ...mapActions({
      SetLoginStatus: "SetLoginStatus"
    }),
    menuCollapseChange() {
      this.isCollapse = !this.isCollapse;
      this.menuArrow = this.isCollapse ? "el-icon-s-unfold" : "el-icon-s-fold";
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

.container {
  height: 100vh;
}
.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

.icon-left {
  float: left;
}
.icon-right {
  float: right;
}
.icon {
  font-size: 60px;
  margin: 20px;
  color: rgb(180, 176, 176);
}
.user-info {
  float: right;
  color: rgb(33, 123, 241);
  height: 100px;
  margin-left: 10px;
  margin-right: 10px;
  display: flex;
  align-items: center;
  font-size: 30px;
  letter-spacing: 5px;
  font-weight: bold;
}
</style>
