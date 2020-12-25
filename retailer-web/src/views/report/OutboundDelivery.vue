<template>
  <div>
    <div class="OutboundDelivery-content-header">
      <div>
        <span class="OutboundDelivery-title-name">销售核算</span>
      </div>
      <div>
        <el-popover placement="left" width="500" trigger="click">
          <div class="outbound-popover-items-area">
            <div class="outbound-message-info">*时间跨度最多30天</div>
            <el-date-picker
              v-model="date"
              class="outbound-margin-top"
              type="daterange"
              align="right"
              size="small"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              format="yyyy 年 MM 月 dd 日"
              :picker-options="pickerOptions"
              @focus="focusOn"
            ></el-date-picker>
            <el-select
              v-if="this.userInfo.permission > 2"
              v-model="employeeId"
              placeholder="销售人员"
              class="outbound-margin-top"
              size="small"
              :clearable="true"
            >
              <el-option
                v-for="employee in employeeList"
                :key="employee.id"
                :label="employee.userName"
                :value="employee.userId"
              >
              </el-option>
            </el-select>
            <el-select
              v-model="customerId"
              placeholder="客户"
              class="outbound-margin-top"
              size="small"
              :clearable="true"
            >
              <el-option
                v-for="customer in customerList"
                :key="customer.id"
                :label="customer.name"
                :value="customer.id"
              >
              </el-option>
            </el-select>
            <el-select
              v-model="productId"
              placeholder="产品"
              class="outbound-margin-top"
              size="small"
              :clearable="true"
            >
              <el-option
                v-for="product in productList"
                :key="product.id"
                :label="product.name"
                :value="product.id"
              >
              </el-option>
            </el-select>
          </div>
          <el-button slot="reference" type="primary" size="small"
            >查询选项</el-button
          >
        </el-popover>

        <el-button
          :disabled="date ? false : true"
          type="primary"
          size="small"
          class="outbound-margin-left"
          icon="el-icon-search"
          @click="searchOnClick"
          >查询</el-button
        >
        <el-button
          :disabled="outBoundList.length <= 0"
          type="primary"
          size="small"
          @click="exportOnClick"
          >导出报表</el-button
        >
      </div>
    </div>
    <div class="OutboundDelivery-content">
      <div class="OutboundDelivery-Summary-area">
        <el-row :gutter="30">
          <el-col :span="8">
            <div class="OutboundDelivery-Summart-card color-bk-green">
              <span class="OutboundDelivery-Summary-card-title color-green"
                >销售额</span
              >
              <div
                class="OutboundDelivery-Summary-card-content color-dark-green"
              >
                {{ summarys.salesTotal }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="OutboundDelivery-Summart-card color-bk-gray">
              <span class="OutboundDelivery-Summary-card-title color-gray"
                >退货额</span
              >
              <div class="OutboundDelivery-Summary-card-content">
                {{ summarys.returnTotal }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="OutboundDelivery-Summart-card color-bk-yellow">
              <span class="OutboundDelivery-Summary-card-title color-yellow"
                >销售利润</span
              >
              <div
                class="OutboundDelivery-Summary-card-content color-dark-yellow"
              >
                {{ summarys.profit }}
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <el-table
        :data="outBoundList"
        ref="outBoundTable"
        border
        size="mini"
        show-summary
        :summary-method="getSummaries"
        :header-row-style="{ height: '40px' }"
        :row-style="{ height: '40px' }"
        :cell-style="{ padding: '2px', color: '#909399' }"
        :header-cell-style="{ background: '#808080', color: 'white' }"
      >
        }" >
        <el-table-column prop="date" label="日期" width="90%" sortable>
        </el-table-column>
        <el-table-column
          prop="outBoundNo"
          label="单号"
          sortable
        ></el-table-column>
        <el-table-column
          prop="customerName"
          label="客户名称"
          width="auto"
          sortable
        >
        </el-table-column>
        <el-table-column
          prop="salesName"
          label="销售人员"
          width="120px" align="center"
          sortable
        >
        </el-table-column>
        <el-table-column prop="product" label="产品" width="auto" sortable>
        </el-table-column>
        <el-table-column
          prop="supply"
          label="供应商"
          sortable
        ></el-table-column>
        <el-table-column
          prop="batchNo"
          label="批次"
          sortable
        ></el-table-column>
        <!-- <el-table-column label="入库单价">
          <template slot-scope="scope">
            {{ scope.row.priceIn }}{{ scope.row.countUnit }}
          </template>
        </el-table-column>
        <el-table-column label="出库单价">
          <template slot-scope="scope">
            {{ scope.row.priceOut }}{{ scope.row.countUnit }}
          </template>
        </el-table-column> -->
        <el-table-column label="数量" width="70px" align="center">
          <template slot-scope="scope">
            {{ scope.row.outCount }}{{ scope.row.countUnit }}
          </template>
        </el-table-column>
        <el-table-column
          prop="totalPrice"
          label="总价" width="80px" align="center"
          sortable
        ></el-table-column>
        <el-table-column
          prop="unitProfit"
          label="单个利润"
          width="100px" align="center"
          sortable
        ></el-table-column>
        <el-table-column prop="returnCount" label="退货量" width="70px" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.returnCount > 0" class="color-green">
              {{ scope.row.returnCount }}{{ scope.row.countUnit }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="salesProfit"
          label="总利润"
          width="100px"
          align="center"
          sortable
        >
          <template slot-scope="scope">
            <span class="color-orange">
              {{ scope.row.salesProfit.toFixed(2) }}
            </span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
import * as excelHelper from "../../utils/excelHelper";
export default {
  data() {
    return {
      date: "",
      startDateMin: null,
      employeeId: "",
      customerId: "",
      productId: "",
      pickerOptions: {
        onPick: (obj) => {
          this.startDateMin = new Date(obj.minDate).getTime();
        },
        disabledDate: (time) => {
          if (this.startDateMin) {
            let maxDate = this.startDateMin + 3600 * 1000 * 24 * 31;
            let minDate = this.startDateMin - 3600 * 1000 * 24 * 31;
            return (
              time.getTime() > maxDate ||
              time.getTime() > Date.now() ||
              time.getTime() < minDate
            );
          } else {
            return time.getTime() > Date.now();
          }
        },
        shortcuts: [
          {
            text: "今天",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近三天",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 3);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
    };
  },
  methods: {
    ...mapActions({
      GetOutBoundList: "GetOutBoundList",
      GetEmployeeList: "GetEmployeeList",
      GetCustomerList: "GetCustomerList",
      GetProductList: "GetProductList",
    }),
    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = "总计";
          return;
        }
        if (
          column.property == "salesProfit" ||
          column.property == "totalPrice"
        ) {
          const values = data.map((item) => Number(item[column.property]));
          if (!values.every((value) => isNaN(value))) {
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
    searchOnClick() {
      let params = {
        storeCode: this.userInfo.storeCode,
        permission: this.userInfo.permission,
        userId: this.userInfo.userId,
        startDate: this.date[0],
        endDate: this.date[1],
        customerId: this.customerId,
        productId: this.productId,
        employeeId: this.employeeId,
      };
      this.GetOutBoundList(params);
    },
    focusOn() {
      this.startDateMin = null;
    },
    exportOnClick() {
      var keys = {
        date: "销售日期",
        outBoundNo: "出库单号",
        customerName: "客户名",
        salesName: "销售人员",
        product: "产品名称",
        supply: "供应商",
        batchNo: "入库批次",
        priceIn: "入库单价",
        priceOut: "出库单价",
        outCount: "出库数量",
        totalPrice: "销售额",
        unitProfit: "单个利润",
        returnCount: "退货数量",
        salesProfit: "销售利润",
        profit: "出库利润",
        countUnit:"单位"
      };
      var json = [];
      var table = this.$refs.outBoundTable;
      const { data } = table;
      var sum = this.getSummaries(table);
      data.map((item) => {
        json.push(item);
      });
      if (!this.date) {
        this.$message.warning("请选择时间范围");
        return;
      }
      var fileName =
        "销售报表" + "(" + this.date[0] + "至" + this.date[1] + ")";
      json.push({
        date: "总计",
        outBoundNo: "",
        salesName: "",
        customerName: "",
        product: "",
        supply: "",
        batchNo: "",
        priceIn: "",
        priceOut: "",
        unitProfit: "",
        outCount: "",
        profit: "",
        returnCount: "",
        totalPrice: sum[8],
        salesProfit: sum[11],
        countUnit:""
      });
      excelHelper.export_json_to_excel({
        json: json,
        header: [{ value: this.userInfo.store.storeName + "销售报表" }],
        keys: keys,
        border: true,
        sheetName: "sheet1",
        fileName: fileName,
      });
    },
  },
  computed: {
    ...mapGetters([
      "userInfo",
      "outBoundList",
      "employeeList",
      "customerList",
      "productList",
    ]),
    summarys() {
      var summary = { salesTotal: 0, returnTotal: 0, profit: 0 };
      this.outBoundList.map((item) => {
        summary.salesTotal = this.NumberAdd(
          summary.salesTotal,
          item.totalPrice
        );
        summary.profit = this.NumberAdd(summary.profit, item.salesProfit);
        if (item.returnCount > 0) {
          summary.returnTotal = this.NumberAdd(
            summary.returnTotal,
            this.NumberMul(item.returnCount, item.priceOut)
          );
        }
      });
      summary.profit = summary.profit.toFixed(2);
      summary.returnTotal = summary.returnTotal.toFixed(2);
      summary.salesTotal = summary.salesTotal.toFixed(2);
      return summary;
    },
  },
  mounted: function () {
    if (this.userInfo.permission > 2) {
      this.GetEmployeeList(this.userInfo);
    }
    this.GetCustomerList(this.userInfo);
    let params = { storeCode: this.userInfo.storeCode };
    this.GetProductList(params);
  },
};
</script>
<style>
.color-orange {
  color: orange;
  font-weight: bold;
}
.OutboundDelivery-content-header {
  height: 60px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
}
.OutboundDelivery-title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
}
.OutboundDelivery-content {
  padding: 20px;
}
.content-scrollbar /deep/.el-scrollbar__wrap {
  overflow-x: hidden;
}
.outbound-message-info {
  color: #79bbff;
  font-size: 20px;
}
.ticket-title {
  height: 80px;
  font-size: 35px;
  font-weight: bold;
  display: flex;
  align-items: center;
  color: #303133;
  justify-content: center;
}
.outbound-popover-items-area {
  display: flex;
  flex-direction: column;
  padding: 20px;
}
.outbound-margin-top {
  margin-top: 20px;
}
.outbound-margin-left {
  margin-left: 20px;
}
.OutboundDelivery-Summart-card {
  border-radius: 10px;
  height: 200px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 20px;
}
.OutboundDelivery-Summary-area {
  margin-bottom: 20px;
}
.color-bk-green {
  background: #f0f9eb;
}
.color-bk-yellow {
  background: #faecd8;
}
.color-bk-red {
  background: #fde2e2;
}
.color-bk-gray {
  background: #e9e9eb;
}
.color-green {
  color: #67c23a;
}
.color-yellow {
  color: #e6a23c;
}
.color-gray {
  color: #909399;
}
.color-dark-green {
  color: #008000;
}
.color-dark-yellow {
  color: #ff8c00;
}
.OutboundDelivery-Summary-card-title {
  width: 200px;
  font-size: 35px;
  font-weight: bold;
}
.OutboundDelivery-Summary-card-content {
  width: 100%;
  font-size: xx-large;
}
</style>