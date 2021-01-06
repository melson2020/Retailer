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
      <el-header height="auto">
        <div>
          <el-dropdown trigger="click" class="main-icon-right">
            <span class="el-dropdown-link main-user-info">
              <i class="el-icon-user main-icon main-icon-right"></i>
              {{ generateStoreName }}
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="logout">注册</el-dropdown-item>
              <el-dropdown-item @click.native="resetPass"
                >登录</el-dropdown-item
              >
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <div class="productstorage-content-header">
          <div>
            <span class="productstorage-title-name">库存信息</span>
          </div>
          <div>
            <el-input
              class="productstorage-fliter-input"
              size="small"
              v-model="searchContent"
              placeholder="搜索名称 / 型号"
              suffix-icon="el-icon-search"
            ></el-input>
          </div>
        </div>
        <div class="productstorage-content">
          <el-table
            :data="storageListShow"
            border
            class="productstorage-storage-table"
            size="small"
            ref="tableStorage"
            :height="auto"
            :header-row-style="{ height: '40px' }"
            :row-style="{ height: '40px' }"
            :cell-style="{ padding: '2px', color: '#909399' }"
            :header-cell-style="{ background: '#808080', color: 'white' }"
            @expand-change="expandChanged"
          >
            <el-table-column type="expand">
              <template slot-scope="props">
                <el-table
                  v-if="props.row.batchList.length > 0"
                  :data="props.row.batchList"
                  class="productstorage-inner-table"
                  border
                  :show-header="false"
                  :header-row-style="{ height: '30px' }"
                  :header-cell-style="{ padding: '2px', background: '#606266' }"
                  :row-style="{ height: '30px' }"
                  :cell-style="{ padding: '2px', color: '#909399' }"
                >
                  <el-table-column
                    prop="batchNo"
                    label="批次号"
                    width="auto"
                  ></el-table-column>
                  <el-table-column
                    prop="supplyName"
                    label="供应商"
                    width="auto"
                  ></el-table-column>
                  <el-table-column label="税/回点" width="auto">
                    <template slot-scope="scope">
                      <el-tag v-if="scope.row.vat == 1" size="mini"
                        >税 ({{ scope.row.taxRate }}%)</el-tag
                      >
                      <el-tag
                        v-if="scope.row.discount > 0"
                        type="success"
                        size="mini"
                        >回点 ({{ scope.row.discount }}%)</el-tag
                      >
                    </template>
                  </el-table-column>
                  <el-table-column label="单价">
                    <template slot-scope="scope">
                      <span
                        >{{ scope.row.price }}{{ scope.row.priceUnit }}￥/{{
                          scope.row.countUnit
                        }}</span
                      >
                    </template>
                  </el-table-column>
                  <el-table-column label="数量">
                    <template slot-scope="scope">
                      <span
                        >{{ scope.row.count }}{{ scope.row.countUnit }}</span
                      >
                    </template>
                  </el-table-column>
                </el-table>
                <div v-else class="no-batch-info">
                  <span>无批次信息</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              v-for="(item, i) in tableColums"
              :key="i"
              :prop="item.field"
              :label="item.label"
              :width="item.width"
            ></el-table-column>
            <el-table-column label="标签" align="center" width="auto">
              <template slot-scope="scope">
                <el-tag
                  class="el-tag"
                  :key="tag"
                  v-for="tag in scope.row.feature"
                  :disable-transitions="false"
                  size="mini"
                  type="success"
                >
                  {{ tag }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <!-- <div class="productstorage-content-footer">
          <el-pagination
            background
            :current-page="storageTable.currentPage"
            layout="prev, pager, next, total, jumper"
            @current-change="pageChanged"
            :page-size="storageTable.pageSize"
            :total="storageListShow.length"
            v-if="storageListShow.length >= storageTable.pageSize"
          ></el-pagination>
        </div> -->
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
      // tableHeight: window.innerHeight - 1500,
      searchContent: "",
      // storageTable: {
      //   pageSize: 15,
      //   currentPage: 1,
      //   height: "",
      // },
      tableColums: [
        { field: "productName", label: "名称", width: "auto" },
        { field: "productType", label: "型号", width: "auto" },
        { field: "productSpecification", label: "规格", width: "auto" },
        { field: "count", label: "数量", width: "auto" },
        { field: "unit", label: "单位", width: "150px" },
      ],
    };
  },
  computed: {
    ...mapGetters(["shareProductStorage"]),
    generateStoreName() {
      return this.shareProductStorage
        ? this.shareProductStorage.store.storeName
        : "";
    },
    generateMenu() {
      return this.shareProductStorage ? this.shareProductStorage.menuList : "";
    },
    storageListShow () {
      return this.shareProductStorage.productStorageList.filter((item) => {
        let key = item.productName + item.productType;
        return (
          key.toUpperCase().indexOf(this.searchContent.toUpperCase()) != -1
        );
      });
    },
    // storages() {
    //   return this.storageListShow.slice(
    //     (this.storageTable.currentPage - 1) * this.storageTable.pageSize,
    //     this.storageTable.currentPage * this.storageTable.pageSize
    //   );
    // },
  },
  methods: {
    ...mapActions({
      FindShareProductStorage: "FindShareProductStorage",
      SetShareProductStorage: "SetShareProductStorage",
    }),
  },
  // pageChanged(page) {
  //   console.log(page);
  //   this.storageTable.currentPage = page;
  // },
  // setpageSize() {
  //   let rect = this.tableHeight - 40;
  //   this.storageTable.height = rect + 40;
  //   let pageSize = Math.floor(rect / 40);
  //   this.storageTable.pageSize = pageSize;
  // },
  // searchFocus() {
  //   this.storageTable.currentPage = 1;
  // },
  expandChanged(row, sec) {
    let index = sec.indexOf(row);
    // var expanded = index != -1; //获取是否展开 row为当前行
    // if (expanded) {
    //   let payload = {
    //     row: row,
    //     params: {
    //       storeCode: this.userInfo.storeCode,
    //       productId: row.productId,
    //     },
    //   };
    //   this.GetBatchList(payload);
    // }
  },
  beforeMount() {
    let fullPath = window.location.href;
    var pathCode = fullPath.substring(fullPath.indexOf("?") + 1).split("=")[1];
    if (!pathCode) {
      this.$message.error("请输入正确的连接");
      return;
    }
    var params = { shareCode: pathCode };
    this.FindShareProductStorage(params)
      .then((res) => {
        if (res.resultStatus == 1) {
          this.SetShareProductStorage(res.data);
        } else {
          this.$message.error(res.message);
        }
      })
      .catch((err) => {
        this.$message.error(err.message);
      });
  },
  // mounted: function () {
  //   this.$nextTick(function () {
  //     this.setpageSize();
  //   });
  // },
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
.productstorage-content-header {
  height: 60px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
}
.productstorage-content {
  margin-top: 5px;
}
.productstorage-content-footer {
  margin-top: 20px;
  height: 60px;
  align-items: center;
  justify-content: space-between;
}
.productstorage-title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  margin-left: 20px;
}
</style>