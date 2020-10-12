<template>
  <div>
    <div class="header-div">
      <span class="title-name">出库单详细</span>
      <el-button type="text" @click="direct">返回</el-button>
    </div>
    <el-scrollbar style="height:350px" class="content-scrollbar">
      <el-table
        :data="storageOutDetails.ticketDetails"
        style="width: 100%"
        border
        :header-row-style="{ height: '30px' }"
        :header-cell-style="{
          padding: '0px',
          background: '#C0C4CC',
          color: 'white'
        }"
      >
        <el-table-column prop="productName" label="商品名称"> </el-table-column>
        <el-table-column
          prop="storageInBatchNo"
          label="入库批次号"
        ></el-table-column>
        <el-table-column label="税">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.vat == 1" size="mini">
              税 ({{ scope.row.taxRate }}%)
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="出库数量">
          <template slot-scope="scope">
            {{ scope.row.outCount }}{{ scope.row.countUnit }}
          </template>
        </el-table-column>
        <el-table-column prop="outPrice" label="出库单价"> </el-table-column>
        <el-table-column prop="totalPrice" label="出库总价"> </el-table-column>
      </el-table>
    </el-scrollbar>
    <div class="header-div">
      <span class="title-name">出库单详细核算</span>
    </div>
    <el-scrollbar style="height:350px" class="content-scrollbar">
      <el-table
        :data="storageOutDetails.billDetails"
        border
        show-summary
        :summary-method="getSummaries"
        :header-row-style="{ height: '30px' }"
        :header-cell-style="{
          padding: '0px',
          background: '#C0C4CC',
          color: 'white'
        }"
      >
        <el-table-column prop="productName" label="商品名称"> </el-table-column>
        <el-table-column prop="batchNo" label="入库批次号"></el-table-column>
        <el-table-column label="入库税">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.vatIn == 1" size="mini">
              税 ({{ scope.row.taxRateIn }}%)
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="unitPriceIn" label="入库单价"> </el-table-column>
        <el-table-column prop="discount" label="入库回点"> </el-table-column>
        <el-table-column prop="unitPriceOut" label="出库单价">
        </el-table-column>
        <el-table-column label="出库税">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.vatOut == 1" size="mini">
              税 ({{ scope.row.taxRateOut }}%)
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="出库数量">
          <template slot-scope="scope">
            {{ scope.row.outCount }}{{ scope.row.countUnit }}
          </template>
        </el-table-column>
        <el-table-column prop="profit" label="总利润">
          <template slot-scope="scope">
            <span class="color-orange"> {{ scope.row.profit }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-scrollbar>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
export default {
  data() {
    return {};
  },
  methods: {
    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = "总价";
          return;
        }
        if (column.property == "profit") {
          const values = data.map(item => Number(item[column.property]));
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return this.NumberAdd(prev, curr).toFixed(2);
              } else {
                return prev;
              }
            }, 0);
          } else {
            sums[index] = "N/A";
          }
        }
      });
      return sums;
    },
    direct() {
      this.$router.replace({ path: "/main/storageOutRecord" });
    }
  },
  computed: {
    ...mapGetters(["storageOutDetails"])
  }
};
</script>
<style>
.color-orange {
  color: orange;
  font-weight: bold;
}
.header-div {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.title-name {
  font-size: 30px;
  font-weight: bold;
  color: #409eff;
}
.content-scrollbar /deep/.el-scrollbar__wrap {
  overflow-x: hidden;
}
</style>