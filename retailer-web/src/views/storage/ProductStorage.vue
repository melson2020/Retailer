<template>
  <div style="height: 100%">
    <div
      class="productstorage-info-div"
      v-if="
        productAndStorageCount.productCount !=
        productAndStorageCount.storageCount
      "
    >
      <el-card class="productstorage-box-card" shadow="always">
        <div slot="header" class="clearfix">
          <span class="productstorage-title">提示</span>
        </div>
        <div class="productstorage-message-div">
          <span class="productstorage-message"
            >已有商品数量：{{ productAndStorageCount.productCount }}</span
          >
        </div>
        <div class="productstorage-message-div">
          <span class="productstorage-message"
            >库存数量：{{ productAndStorageCount.storageCount }}</span
          >
        </div>
        <div class="productstorage-message-button">
          <el-button
            type="success"
            v-if="
              (productAndStorageCount.productCount > 0) &
              (productAndStorageCount.storageCount == 0)
            "
            @click.prevent.stop="GenerateAll"
            >生成库存</el-button
          >
          <el-button
            type="primary"
            v-if="productAndStorageCount.productCount == 0"
            >导入商品</el-button
          >
          <el-button
            type="primary"
            v-if="
              (productAndStorageCount.productCount > 0) &
              (productAndStorageCount.storageCount > 0) &
              (productAndStorageCount.productCount >
                productAndStorageCount.storageCount)
            "
            >添加库存</el-button
          >
          <el-button
            type="primary"
            v-if="
              (productAndStorageCount.productCount > 0) &
              (productAndStorageCount.storageCount > 0) &
              (productAndStorageCount.productCount >
                productAndStorageCount.storageCount)
            "
            >跳过提示</el-button
          >
        </div>
      </el-card>
    </div>
    <div v-else>
      <div class="productstorage-content-header">
        <div>
          <span class="productstorage-title-name">库存信息</span>
        </div>
        <div>
          <el-button type="primary" icon="el-icon-refresh" size="small" @click="productTypeChanged"
            >刷新</el-button
          >
          <el-select
            v-model="productType"
            size="small"
            placeholder="请选产品类型"
            class="product-type-select"
            @change="productTypeChanged"
          >
            <el-option label="全部" value="all"></el-option>
            <el-option label="常用" value="normal"></el-option>
            <el-option label="数量大于0" value="morethanzero"></el-option>
          </el-select>
          <el-input
            class="productstorage-fliter-input"
            size="small"
            v-model="searchContent"
            placeholder="搜索名称 / 型号"
            suffix-icon="el-icon-search"
            @focus="searchFocus"
          ></el-input>
        </div>
      </div>
      <div class="productstorage-content">
        <el-table
          :data="storages"
          border
          class="productstorage-storage-table"
          size="small"
          ref="tableStorage"
          :height="tableHeight"
          :header-row-style="{ height: '40px' }"
          :row-style="{ height: '40px' }"
          :cell-style="{ padding: '2px', color: '#909399' }"
          :header-cell-style="{ background: '#909399', color: 'white' }"
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
                    <span>{{ scope.row.count }}{{ scope.row.countUnit }}</span>
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
      <div class="productstorage-content-footer">
        <el-pagination
          background
          :current-page="storageTable.currentPage"
          layout="prev, pager, next, total, jumper"
          @current-change="pageChanged"
          :page-size="storageTable.pageSize"
          :total="storageListShow.length"
          v-if="storageListShow.length >= storageTable.pageSize"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";

export default {
  data() {
    return {
      tableHeight: window.innerHeight - 150,
      productType: "normal",
      storageTable: {
        pageSize: 15,
        currentPage: 1,
        height: "",
      },
      searchContent: "",
      tableColums: [
        { field: "productName", label: "名称", width: "auto" },
        { field: "productType", label: "型号", width: "auto" },
        { field: "productSpecification", label: "规格", width: "auto" },
        { field: "count", label: "数量", width: "150px" },
        { field: "unit", label: "单位", width: "150px" },
      ],
    };
  },
  computed: {
    ...mapGetters(["productAndStorageCount", "userInfo", "productStorageList"]),
    storageListShow: function () {
      return this.productStorageList.filter((item) => {
        let key = item.productName + item.productType;
        return (
          key.toUpperCase().indexOf(this.searchContent.toUpperCase()) != -1
        );
      });
    },
    storages() {
      return this.storageListShow.slice(
        (this.storageTable.currentPage - 1) * this.storageTable.pageSize,
        this.storageTable.currentPage * this.storageTable.pageSize
      );
    },
  },
  methods: {
    ...mapActions({
      GetProductAndStorageCount: "GetProductAndStorageCount",
      GenerateProductStorageList: "GenerateProductStorageList",
      GetProductStorageList: "GetProductStorageList",
      GetBatchList: "GetBatchList",
    }),
    searchFocus() {
      this.storageTable.currentPage = 1;
    },
    GenerateAll() {
      let params = { storeCode: this.userInfo.storeCode };
      this.GenerateProductStorageList(params);
    },
    expandChanged(row, sec) {
      let index = sec.indexOf(row);
      var expanded = index != -1; //获取是否展开 row为当前行
      if (expanded) {
        let payload = {
          row: row,
          params: {
            storeCode: this.userInfo.storeCode,
            productId: row.productId,
          },
        };
        this.GetBatchList(payload);
      }
    },
    pageChanged(page) {
      this.storageTable.currentPage = page;
    },
    setpageSize() {
      let rect = this.tableHeight - 40;
      this.storageTable.height = rect + 40;
      let pageSize = Math.floor(rect / 40);
      this.storageTable.pageSize = pageSize;
    },
    productTypeChanged() {
      this.storageTable.currentPage=1;
      this.GetProductStorageList({
        storeCode: this.userInfo.storeCode,
        searchType: this.productType,
      });
    },
  },
  beforeMount: function () {
    let params = { storeCode: this.userInfo.storeCode };
    this.GetProductAndStorageCount(params);
    this.GetProductStorageList({
      storeCode: this.userInfo.storeCode,
      searchType: this.productType,
    });
  },
  mounted: function () {
    this.$nextTick(function () {
      this.setpageSize();
    });
  },
};
</script>
<style>
.productstorage-info-div {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 800px;
}
.productstorage-box-card {
  width: 800px;
}
.productstorage-clearfix:after {
  display: table;
  content: "";
  clear: both;
}
.productstorage-title {
  color: rgb(228, 116, 25);
  font-size: 35px;
  font-weight: bold;
  letter-spacing: 0.5em;
}
.productstorage-message-div {
  padding: 15px;
  font-weight: bold;
}
.productstorage-message {
  font-size: 30px;
  color: #606266;
}
.productstorage-message-button {
  margin-top: 40px;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}
.productstorage-content-header {
  height: 60px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
}
.productstorage-title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  margin-left: 20px;
}
.productstorage-fliter-input {
  margin-left: 20px;
  width: 400px;
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

.el-table__expanded-cell {
  padding-top: 0px !important;
  padding-right: 0px !important;
  padding-bottom: 0px !important;
}
.productstorage-inner-table {
  font-size: 16px;
}
.productstorage-no-batch-info {
  padding: 10px;
  font-size: 20px;
  color: #e6a23c;
  font-weight: bold;
}
.el-tag {
  margin: 0px 10px;
}
.product-type-select {
  margin-left: 20px;
}
</style>
