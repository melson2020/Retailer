<template>
  <div>
    <div class="InboundReport-content-header">
      <div>
        <span class="InboundReport-title-name">入库核算</span>
      </div>
      <div>
        <el-popover placement="left" width="500" trigger="click">
          <div class="InboundReport-popover-items-area">
            <div class="InboundReport-message-info">*时间跨度最多30天</div>
            <el-date-picker
              v-model="date"
              class="InboundReport-margin-top"
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
              v-model="supplyId"
              placeholder="供应商"
              filterable
              class="InboundReport-margin-top"
              size="small"
              :clearable="true"
            >
              <el-option
                v-for="supply in supplyList"
                :key="supply.id"
                :label="supply.name"
                :value="supply.id"
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
          class="InboundReport-margin-left"
          icon="el-icon-search"
          @click="searchOnClick"
          >查询</el-button
        >
      </div>
    </div>

    <div class="InboundReport-content">

      <el-table
        :data="inboundReports"
        border
        size="small"
        show-summary
        :summary-method="getSummaries"
        :header-row-style="{ height: '40px' }"
        :row-style="{ height: '40px' }"
        :cell-style="{ padding: '2px', color: '#909399' }"
        :header-cell-style="{ background: '#909399', color: 'white',align:'center'}"
      >
        }" >
        <el-table-column prop="createTime" label="日期" width="120px" >
        </el-table-column>
        <el-table-column
          prop="batchNo"
          label="批次"
          width="180px" 
        ></el-table-column>

        <el-table-column
          prop="employeeName"
          label="操作人"
          width="120px"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="supplyName"
          label="供应商"
          width="auto"
        >
        </el-table-column>
        <el-table-column prop="productName" label="产品" width="auto" >
        </el-table-column>

        <el-table-column label="数量" width="120px" align="center">
          <template slot-scope="scope">
            {{ scope.row.count }}{{ scope.row.countUnit }}
          </template>
        </el-table-column>
        <el-table-column prop="taxRate" label="税点" width="80px" >
        </el-table-column>

        <el-table-column
          prop="totalPrice"
          label="总价"
          width="140px"
          align="center"
        ></el-table-column>

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
      date: "",
      startDateMin: null,
      supplyId: "",
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
      GetInboundReportVos: "GetInboundReportVos",
      GetSupplyList: "GetSupplyList",
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
        startDate: this.date[0],
        endDate: this.date[1],
        supplyId: this.supplyId,
      };

      this.GetInboundReportVos(params);
    },
    focusOn() {
      this.startDateMin = null;
    },
  },
  computed: {
    ...mapGetters(["userInfo", "supplyList", "inboundReports"]),
    list(){
      return this.inboundReports;
    }
    // summarys() {
    //   var summary = { salesTotal: 0, returnTotal: 0, profit: 0 };
    //   if (!this.InboundReport.InboundReportVoList) return summary;
    //   this.InboundReport.InboundReportVoList.map((item) => {
    //     summary.salesTotal = Number(
    //       this.NumberAdd(summary.salesTotal, item.totalPrice)
    //     ).toFixed(2);
    //     summary.profit = Number(
    //       this.NumberAdd(summary.profit, item.salesProfit)
    //     ).toFixed(2);
    //     if (item.returnCount > 0) {
    //       summary.returnTotal = Number(
    //         this.NumberAdd(
    //           summary.returnTotal,
    //           this.NumberMul(item.returnCount, item.priceOut)
    //         )
    //       ).toFixed(2);
    //     }
    //   });
    //   return summary;
    // },

  },
  beforeMount: function() {
    this.GetSupplyList(this.userInfo);
  },
};
</script>
<style>
.color-orange {
  color: orange;
  font-weight: bold;
}
.InboundReport-content-header {
  height: 60px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
.InboundReport-title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  margin-left: 20px;
}
.InboundReport-content {
  margin-top: 5px;
}
.content-scrollbar /deep/.el-scrollbar__wrap {
  overflow-x: hidden;
}
.InboundReport-message-info {
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
.InboundReport-popover-items-area {
  display: flex;
  flex-direction: column;
  padding: 20px;
}
.InboundReport-margin-top {
  margin-top: 20px;
  width: auto;
}
.InboundReport-margin-left {
  margin-left: 20px;
}
.InboundReport-Summart-card {
  border-radius: 10px;
  height: 7vh;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 20px;
}
.InboundReport-Summary-area {
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
.color-black {
  color: #303133;
}
.InboundReport-Summary-card-title {
  width: 200px;
  font-size: 35px;
  font-weight: bold;
}
.InboundReport-Summary-card-content {
  width: 100%;
  font-size: xx-large;
}
.InboundReport-goods-return-record-summary {
  padding: 20px;
  text-align: right;
  font-size: large;
}
</style>
