<template>
  <div>
    <div class="OutboundDelivery-content-header">
      <div>
        <span class="OutboundDelivery-title-name">出库核算</span>
      </div>
      <div>
        <span class="message-info">*时间跨度最多30天</span>
        <el-date-picker
          v-model="date"
          class="date-picker"
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
        <el-button
          :disabled="date ? false : true"
          type="primary"
          size="small"
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
    <div class="ticket-title">{{ userInfo.store.storeName }}出库清单</div>
    <div class="OutboundDelivery-content">
      <el-table
        :data="outBoundList"
        ref="outBoundTable"
        border
        size="small"
        show-summary
        :summary-method="getSummaries"
        :header-row-style="{ height: '40px' }"
        :row-style="{ height: '40px' }"
        :cell-style="{ padding: '2px', color: '#909399' }"
        :header-cell-style="{ background: '#808080', color: 'white' }"
      >
        }" >
        <el-table-column prop="date" label="销售日期" width="90%">
        </el-table-column>
        <el-table-column
          prop="outBoundNo"
          label="出库单号"
          width="130%"
        ></el-table-column>
        <el-table-column prop="salesName" label="销售人员" width="auto">
        </el-table-column>
        <el-table-column prop="product" label="产品名称" width="auto">
        </el-table-column>
        <el-table-column
          prop="supply"
          label="供应商"
          width="200px"
        ></el-table-column>
        <el-table-column
          prop="batchNo"
          label="入库批次"
          width="130%"
        ></el-table-column>
        <el-table-column label="入库单价" width="200px">
          <template slot-scope="scope">
            {{ scope.row.priceIn }}{{ scope.row.countUnit }}
          </template>
        </el-table-column>
        <el-table-column label="出库单价" width="200px">
          <template slot-scope="scope">
            {{ scope.row.priceOut }}{{ scope.row.countUnit }}
          </template>
        </el-table-column>

        <el-table-column label="出库数量" width="200px">
          <template slot-scope="scope">
            {{ scope.row.outCount }}{{ scope.row.countUnit }}
          </template>
        </el-table-column>
        <el-table-column prop="profit" label="总利润" width="auto">
          <template slot-scope="scope">
            <span class="color-orange"> {{ scope.row.profit }}</span>
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
    }),
    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = "总价";
          return;
        }
        if (column.property == "profit") {
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
        salesName: "销售人员",
        product: "产品名称",
        supply: "供应商",
        batchNo: "入库批次",
        priceIn: "入库单价",
        priceOut: "出库单价",
        outCount: "出库数量",
        profit: "总利润",
      };
      var json = [];
      this.outBoundList.map((item) => {
        json.push(item);
      });
      var table = this.$refs.outBoundTable;
      var sum = this.getSummaries(table);
      if(!this.date){
        this.$message.warning('请重新选择查询时间范围')
        return
      }
      var fileName='出库清单'+'('+this.date[0]+'至'+this.date[1]+')'
      json.push({
        date: "总价",
        outBoundNo: "",
        salesName: "",
        product: "",
        supply: "",
        batchNo: "",
        priceIn: "",
        priceOut: "",
        outCount: "",
        profit: sum[9],
      });
      excelHelper.export_json_to_excel({
        json: json,
        header: [{ value: this.userInfo.store.storeName + "出库清单" }],
        keys: keys,
        border: true,
        sheetName: "sheet1",
        fileName: fileName,
      });
    },
  },
  computed: {
    ...mapGetters(["userInfo", "outBoundList"]),
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
  margin-top: 5px;
}
.content-scrollbar /deep/.el-scrollbar__wrap {
  overflow-x: hidden;
}
.message-info {
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
</style>