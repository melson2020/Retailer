<template>
  <el-container class="container">
    <el-aside width="auto">
      <el-menu
        default-active="0"
        class="el-menu-vertical"
        background-color="#2c61b1"
        text-color="#fff"
        active-text-color="#ffd04b"
        :collapse="isCollapse"
      >
        <el-submenu
          v-for="menu in userInfo.menuList"
          :key="menu.id"
          :index="menu.index"
        >
          <template slot="title">
            <i :class="'el-icon-' + menu.icon"></i>
            <span>{{ menu.name }}</span>
          </template>
          <el-menu-item
            class="el-submenu-item"
            v-for="subMenu in menu.subMenus"
            :index="subMenu.index"
            :key="subMenu.id"
            >{{ subMenu.name }}</el-menu-item
          >
        </el-submenu>

        <!-- <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-location"></i>
            <span>导航一</span>
          </template>
          <el-menu-item class="el-submenu-item" index="1-1">选项1</el-menu-item>
          <el-menu-item class="el-submenu-item" index="1-2">选项2</el-menu-item>
          <el-menu-item class="el-submenu-item" index="1-3">选项3</el-menu-item>
          <el-menu-item class="el-submenu-item" index="1-4">选项4</el-menu-item>
        </el-submenu>
        <el-menu-item index="2">
          <i class="el-icon-menu"></i>
          <span slot="title">导航二</span>
        </el-menu-item>
        <el-menu-item index="3">
          <i class="el-icon-document"></i>
          <span slot="title">导航三</span>
        </el-menu-item>
        <el-menu-item index="4">
          <i class="el-icon-setting"></i>
          <span slot="title">导航四</span>
        </el-menu-item>-->
      </el-menu>
    </el-aside>
    <el-container>
      <el-header height="auto">
        <div>
          <i
            class="icon icon-left"
            :class="menuArrow"
            @click="menuCollapseChange"
          ></i>
          <i class="el-icon-setting icon icon-right"></i>
          <div class="user-info">{{ userInfo.userName }}</div>
          <i class="el-icon-user icon icon-right"></i>
        </div>
      </el-header>
      <el-main>Main</el-main>
    </el-container>
  </el-container>
</template>
<script>
import { mapGetters } from "vuex";
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
    menuCollapseChange() {
      this.isCollapse = !this.isCollapse;
      this.menuArrow = this.isCollapse ? "el-icon-s-unfold" : "el-icon-s-fold";
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

.el-header,
.el-footer {
  color: #333;
  padding: 0;
  margin: 0;
}

.el-aside {
  background-color: #2c61b1;
  text-align: center;
}

.el-main {
  background-color: #e9eef3;
  color: #333;
  text-align: center;
  line-height: 160px;
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
