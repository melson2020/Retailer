<template>
  <div>
    <div class="ProductStorageRecord-content-header">
      <div>
        <span class="ProductStorageRecord-title-name">库存记录</span>
      </div>
      <div>
        <el-radio-group v-model="radio">
          <el-radio :label="12">一年内</el-radio>
          <el-radio :label="6">半年内</el-radio>
          <el-radio :label="1">一个月</el-radio>
        </el-radio-group>
        <el-select
          v-model="selectPid"
          placeholder="选择商品"
          size="medium"
          filterable
          class="margin-left"
          @change="productChanged"
        >
          <el-option
            v-for="p in productList"
            :key="p.id"
            :label="p.name"
            :value="p.id"
          ></el-option>
        </el-select>
        <el-button
         class="margin-left"
          type="primary"
          :disabled="selectPid == ''"
          size="medium"
          @click="fleshOnClick"
          >刷新</el-button
        >
      </div>
    </div>
    <div class="ProductStorageRecord-content">
      <el-table
        border
        :data="productStorageRecords"
        :span-method="objectSpanMethod"
      >
        <el-table-column prop="date" label="日期"> </el-table-column>
        <el-table-column
          prop="code"
          label="单号"
          width="300px"
        ></el-table-column>
        <el-table-column prop="productName" label="产品名称" width="auto">
        </el-table-column>
        <el-table-column prop="batchNo" label="批次号" width="auto">
        </el-table-column>
        <el-table-column prop="supplyName" label="供货商" width="auto">
        </el-table-column>
        <el-table-column
          prop="beforeCount"
          label="变动前数量"
        ></el-table-column>
        <el-table-column prop="action" label="+/-">
          <template slot-scope="scope">
            <i v-if="scope.row.action == 'plus'" class="el-icon-plus green blod"></i>
            <i v-else class="el-icon-minus yellow blod"></i>
          </template>
        </el-table-column>
        <el-table-column prop="count" label="数量"></el-table-column>
        <el-table-column prop="afterCount" label="变动后数量"></el-table-column>
        <el-table-column prop="type" label="类型">
          <template slot-scope="scope">
            <span v-if="scope.row.type == 'C'" class="blod blue">盘点</span>
            <span v-else-if="scope.row.type == 'IN'" class="blod green"
              >入库</span
            >
            <span v-else class="blod yellow"> 出库</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
export default {
  data() {
    return {
      selectPid: "",
      radio: 1,
      testList: [{ date: "test", code: "test" }],
    };
  },
  computed: {
    ...mapGetters(["userInfo", "productList", "productStorageRecords"]),
    spanArr() {
      let spanRowArr = [];
      let pos = 0;
      for (let index = 0; index < this.productStorageRecords.length; index++) {
        if (index == 0) {
          spanRowArr.push(1);
          pos = 0;
        } else {
          var pre = this.productStorageRecords[index - 1];
          var current = this.productStorageRecords[index];
          if (
            pre.date + pre.code === current.date + current.code &&
            current.type == "C"
          ) {
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
  },
  methods: {
    ...mapActions({
      GetProductList: "GetProductList",
      GetProductRecords: "GetProductRecords",
    }),
    findProductStorageRecords() {
      var dayNow = new Date();
      dayNow.setDate(dayNow.getDate() + 1);
      var endDate = dayNow.format("yyyy-MM-dd");
      dayNow.setMonth(dayNow.getMonth() - this.radio);
      var startDate = dayNow.format("yyyy-MM-dd");
      var params = {
        productId: this.selectPid,
        storeCode: this.userInfo.storeCode,
        startDate: startDate,
        endDate: endDate,
      };
      this.GetProductRecords(params);
    },
    productChanged() {
      this.findProductStorageRecords();
    },
    fleshOnClick() {
      if (this.selectPid !== "") {
        this.findProductStorageRecords();
      }
    },
    objectSpanMethod({ rowIndex, columnIndex }) {
      if (columnIndex <= 2||columnIndex==5||columnIndex==6||columnIndex>7) {
        const _row = this.spanArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col,
        };
      }
    },
  },
  beforeMount: function () {
    let params = { storeCode: this.userInfo.storeCode };
    this.GetProductList(params);
  },
};
</script>
<style>
.ProductStorageRecord-content-header {
  height: 60px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
}
.ProductStorageRecord-title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
}
.ProductStorageRecord-content {
  margin-top: 5px;
}
.margin-left {
  margin-left: 10px;
}
.red {
  color: #f56c6c;
}
.green {
  color: #67c23a;
}
.yellow {
  color: #e6a23c;
}
.blue {
  color: #409eff;
}
.blod {
  font-weight: bold;
}
</style>