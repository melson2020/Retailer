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
      >
        <template v-for="menu in generateMenu">
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
              disabled
              >{{ subMenu.name }}</el-menu-item
            >
          </el-submenu>
          <el-menu-item v-else :index="menu.index" :key="menu.id">
            <i :class="'el-icon-' + menu.icon"></i>
            <span slot="title">{{ menu.name }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>
    <el-container>
      <el-main>
        <div class="shareView-content-header">
          <div>
            <span class="shareView-main-user-info shareView-title-name">
              {{ generateStoreName }} -- 库存信息
            </span>
          </div>
          <div class="shareView-content-header-right-area">
            <el-button type="text" @click="directionTo('/login')"
              >登录</el-button
            >
            <el-button type="text" @click="directionTo('/register')"
              >注册</el-button
            >
            <el-input
              class="shareView-search-box"
              size="small"
              v-model="searchContent"
              placeholder="搜索名称 / 型号"
              suffix-icon="el-icon-search"
            ></el-input>
          </div>
        </div>
        <div class="shareView-content">
          <el-table
            :data="storageListShow"
            border
            stripe
             :header-cell-style="{ background: '#909399', color: 'white' }"
            ref="tableStorage"
            :span-method="objectSpanMethod"
          >
            <el-table-column prop="productName" label="名称"></el-table-column>
            <el-table-column label="类型">
              <template slot-scope="scope">
                <span class="shareView-sub-title">{{
                  scope.row.productType
                }}</span>
              </template>
            </el-table-column>
            <el-table-column label="规格">
              <template slot-scope="scope">
                <span class="shareView-sub-title">{{
                  scope.row.specification
                }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="supplyName" label="供货商"></el-table-column>
            <el-table-column label="数量">
              <template slot-scope="scope">
                <span class="shareView-color-warning"
                  >{{ scope.row.subCount }}  {{ scope.row.unit }}</span
                >
              </template>
            </el-table-column>
            <!-- <el-table-column prop="count" label="总数量"></el-table-column> -->
          </el-table>
        </div>
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
      searchContent: "",
    };
  },
  computed: {
    ...mapGetters(["shareProductStorage"]),
    spanArr() {
      let spanRowArr = [];
      let pos = 0;
      for (let index = 0; index < this.storageListShow.length; index++) {
        if (index == 0) {
          spanRowArr.push(1);
          pos = 0;
        } else {
          var pre = this.storageListShow[index - 1];
          var current = this.storageListShow[index];
          if (pre.productName === current.productName) {
            spanRowArr[pos] += 1;
            spanRowArr.push(0);
          } else {
            spanRowArr.push(1);
            pos = index;
          }
        }
      }
      return spanRowArr;
    },

    generateStoreName() {
      return this.shareProductStorage
        ? this.shareProductStorage.store.storeName
        : "";
    },
    generateMenu() {
      return this.shareProductStorage ? this.shareProductStorage.menuList : "";
    },
    storageListShow() {
      return this.shareProductStorage.productStorageList.filter((item) => {
        let key = item.productName + item.productType + item.supplyName;
        return (
          key.toUpperCase().indexOf(this.searchContent.toUpperCase()) != -1
        );
      });
    },
  },
  methods: {
    ...mapActions({
      FindShareProductStorage: "FindShareProductStorage",
      SetShareProductStorage: "SetShareProductStorage",
    }),
    directionTo(path) {
      this.$router.push({ path: path });
    },
    objectSpanMethod({ rowIndex, columnIndex }) {
      if (columnIndex <= 2 || columnIndex == 5) {
        const _row = this.spanArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col,
        };
      }
    },
  },
  beforeMount() {
    let fullPath = window.location.href;
    var pathCode = fullPath.substring(fullPath.indexOf("?") + 1).split("=")[1];
    if (!pathCode) {
      this.$router.push({ path: "/404" });
      return;
    }
    var params = { shareCode: pathCode };
    this.FindShareProductStorage(params)
      .then((res) => {
        if (res.resultStatus == 1) {
          this.SetShareProductStorage(res.data);
        } else {
          this.$router.push({ path: "/404" });
        }
      })
      .catch((err) => {
        this.$router.push({ path: "/404" });
        this.$message.error(err.message);
      });
  },
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

.shareView-main-icon-left {
  float: left;
}
.shareView-main-icon-right {
  float: right;
}
.shareView-main-icon {
  font-size: 50px;
  margin: 20px;
  color: rgb(180, 176, 176);
}
.shareView-main-user-info {
  float: left;
  color: #909399;
  height: 100px;
  margin-right: 10px;
  display: flex;
  align-items: center;
  font-size: 25px;
  letter-spacing: 5px;
}
.shareView-content-header {
  height: 80px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
}
.shareView-content {
  margin-top: 5px;
}

.shareView-title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
}
.shareView-content-header-right-area {
  display: flex;
  flex-direction: row;
  align-items: center;
}
.shareView-search-box {
  margin-left: 20px;
}
.shareView-color-warning {
  color: #f56c6c;
}
.shareView-sub-title {
  color: #c0c4cc;
}
</style>