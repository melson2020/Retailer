<template>
  <div>
    <div
      class="info-div"
      v-if="productAndStorageCount.productCount!=productAndStorageCount.storageCount"
    >
      <el-card class="box-card" shadow="always">
        <div slot="header" class="clearfix">
          <span class="title">提示</span>
        </div>
        <div class="message-div">
          <span class="message">已有商品数量：{{productAndStorageCount.productCount}}</span>
        </div>
        <div class="message-div">
          <span class="message">库存数量：{{productAndStorageCount.storageCount}}</span>
        </div>
        <div class="message-button">
          <el-button
            type="success"
            v-if="productAndStorageCount.productCount>0&productAndStorageCount.storageCount==0"
            @click.prevent.stop="GenerateAll"
          >生成库存</el-button>
          <el-button type="primary" v-if="productAndStorageCount.productCount==0">导入商品</el-button>
          <el-button
            type="primary"
            v-if="productAndStorageCount.productCount>0&productAndStorageCount.storageCount>0&productAndStorageCount.productCount>productAndStorageCount.storageCount"
          >添加库存</el-button>
          <el-button
            type="primary"
            v-if="productAndStorageCount.productCount>0&productAndStorageCount.storageCount>0&productAndStorageCount.productCount>productAndStorageCount.storageCount"
          >跳过提示</el-button>
        </div>
      </el-card>
    </div>
    <div v-else>
      <div class="top-area">
        <span class="title-name">商品库存</span>
        <el-input
          class="fliter-input"
          v-model="searchContent"
          placeholder="搜索名称 / 型号"
          suffix-icon="el-icon-search"
        ></el-input>
      </div>
      <el-table
        :data="storageListShow.slice((storageTable.currentPage-1)*storageTable.pageSize,storageTable.currentPage*storageTable.pageSize)"
        style="width: 100%"
        border
         :header-cell-style="{background:'#606266',color:'white'}"
        @expand-change="expandChanged"
      >
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-table
              v-if="props.row.batchList.length>0"
              :data="props.row.batchList"
              class="inner-table"
              border
              :show-header="false"
              :header-row-style="{height:'30px'}"
              :header-cell-style="{padding:'2px',background:'#606266'}"
              :row-style="{height:'30px'}"
              :cell-style="{padding:'2px',color:'#909399'}"
            >
              <el-table-column prop="batchNo" label="批次号" width="auto"></el-table-column>
              <el-table-column prop="supplyName" label="供应商" width="auto"></el-table-column>
              <el-table-column label="税/回点" width="auto">
                 <template slot-scope="scope">
                   <el-tag v-if="scope.row.vat==1"  size="mini">税 ({{scope.row.taxRate}}%)</el-tag>
                   <el-tag v-if="scope.row.discount>0" type="success"  size="mini">回点 ({{scope.row.discount}}%)</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="单价">
                <template slot-scope="scope">
                  <span>{{scope.row.price}}{{scope.row.priceUnit}}￥/{{scope.row.countUnit}}</span>
                </template>
              </el-table-column>
              <el-table-column label="数量">
                <template slot-scope="scope">
                  <span>{{scope.row.count}}{{scope.row.countUnit}}</span>
                </template>
              </el-table-column>
            </el-table>
            <div v-else class="no-batch-info">
              <span>无批次信息</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          v-for="(item,i) in tableColums"
          :key="i"
          :prop="item.field"
          :label="item.label"
          :width="item.width"
        ></el-table-column>
      </el-table>
      <div class="el-table-pagination-row">
        <el-pagination
          background
          :current-page="storageTable.currentPage"
          layout="prev, pager, next"
          @current-change="pageChanged"
          :page-size="storageTable.pageSize"
          :total="storageListShow.length"
          v-if="storageListShow.length>=storageTable.pageSize"
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
      storageTable: {
        pageSize: 15,
        currentPage: 1
      },
      searchContent: "",
      tableColums: [
        { field: "productName", label: "名称", width: "auto" },
        { field: "productType", label: "型号", width: "auto" },
        { field: "productSpecification", label: "规格", width: "auto" },
        { field: "count", label: "数量", width: "150px" },
        { field: "unit", label: "单位", width: "150px" }
      ]
    };
  },
  computed: {
    ...mapGetters(["productAndStorageCount", "userInfo", "productStorageList"]),
    storageListShow: function() {
      return this.productStorageList.filter(item => {
        let key = item.productName + item.productType;
        return key.indexOf(this.searchContent) != -1;
      });
    }
  },
  methods: {
    ...mapActions({
      GetProductAndStorageCount: "GetProductAndStorageCount",
      GenerateProductStorageList: "GenerateProductStorageList",
      GetProductStorageList: "GetProductStorageList",
      GetBatchList: "GetBatchList"
    }),
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
            productId: row.productId
          }
        };
        this.GetBatchList(payload);
      }
    },
    pageChanged(page) {
      this.storageTable.currentPage = page;
    }
  },
  beforeMount: function() {
    let params = { storeCode: this.userInfo.storeCode };
    this.GetProductAndStorageCount(params);
    this.GetProductStorageList(params);
  }
};
</script>
<style>
.info-div {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 800px;
}
.box-card {
  width: 800px;
}
.clearfix:after {
  display: table;
  content: "";
  clear: both;
}
.title {
  color: rgb(228, 116, 25);
  font-size: 35px;
  font-weight: bold;
  letter-spacing: 0.5em;
}
.message-div {
  padding: 15px;
  font-weight: bold;
}
.message {
  font-size: 30px;
  color: #606266;
}
.message-button {
  margin-top: 40px;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}
.top-area {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.title-name {
  font-size: 30px;
  font-weight: bold;
  color:#409EFF;
}
.fliter-input {
  width: 400px;
  height: 80px;
}
.el-table-pagination-row {
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
  margin-top: 20px;
  height: 80px;
}
.el-table__expanded-cell {
  padding-top: 0px !important;
  padding-right: 0px !important;
  padding-bottom: 0px !important;
}
.inner-table {
  font-size: 16px;
}
.no-batch-info {
  padding: 10px;
  font-size: 20px;
  color: #e6a23c;
  font-weight: bold;
}
.el-tag{
  margin: 0px 10px;
}
</style>
