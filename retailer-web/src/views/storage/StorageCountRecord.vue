<template>
  <div>
    <div class="count-record-content-header">
      <div>
        <span class="count-record-title-name">盘点记录</span>
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
          :default-value="timeDefaultShow"
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
      </div>
    </div>
    <div class="storageCountRecord-content">
      <el-table
        :data="storageCountTickets"
        size="small"
        :header-row-style="{ height: '40px' }"
        :row-style="{ height: '40px' }"
        :cell-style="{ padding: '2px', color: '#909399' }"
        :header-cell-style="{ background: '#909399', color: 'white' }"
        class="record-table"
      >
        <el-table-column
          prop="code"
          label="单号"
          width="280px"
        ></el-table-column>
        <el-table-column prop="type" label="盘点类型" width="150px">
          <template slot-scope="scope">
            <span v-if="scope.row.type == 'monthly'">月度</span>
            <span class="red" v-else>临时</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="employeeName"
          label="盘点人员"
          width="auto"
        ></el-table-column>
        <el-table-column
          prop="date"
          label="盘点日期"
          width="150px"
        ></el-table-column>
        <el-table-column prop="status" label="进度" width="auto">
          <template slot-scope="scope">
            <span class="yellow" v-if="scope.row.status == 1">已创建</span>
            <span class="red" v-else-if="scope.row.status == -1"
              >已作废</span
            >
            <span class="yellow" v-else-if="scope.row.status == 2">已导出</span>
            <span class="yellow" v-else-if="scope.row.status == 3">已导入</span>
            <span class="red" v-else-if="scope.row.status == 4"
              >批次信息未完整</span
            >
            <span class="green" v-else>已完成</span>
          </template>
        </el-table-column>
        <el-table-column prop="productType" label="产品类型" width="150px">
          <template slot-scope="scope">
            <span v-if="scope.row.productType == 'normal'">常用</span>
            <span v-else-if="scope.row.productType == 'all'">所有</span>
            <span v-else>数量大于零</span>
          </template></el-table-column
        >
        <el-table-column
          prop="excelExportFileName"
          label="盘点单(初始)"
          width="200px"
        >
          <template slot-scope="scope">
            <el-link
              type="primary"
              @click="downLoadOnClick(scope.row, 'exprot')"
              >{{ scope.row.excelExportFileName }}</el-link
            >
          </template>
        </el-table-column>
        <el-table-column
          prop="excelImportFileName"
          label="盘点单(完成)"
          width="200px"
        >
          <template slot-scope="scope">
            <el-link
              type="primary"
              @click="downLoadOnClick(scope.row, 'import')"
              >{{ scope.row.excelImportFileName }}</el-link
            >
          </template></el-table-column
        >
        <el-table-column label="操作" width="150px">
          <template slot-scope="scope">
            <span v-if="scope.row.status<0"></span>
            <el-dropdown v-else>
              <span class="el-dropdown-link">
                操作<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  v-if="scope.row.status == 5"
                  @click.native="detailOnClick(scope.row)"
                  ><span class="el-dropdown-link">详细</span></el-dropdown-item
                >
                <el-dropdown-item
                  @click.native="loadTicketOnClick(scope.row)"
                  v-if="scope.row.status < 5"
                  ><span class="yellow">加载</span></el-dropdown-item
                >
                <el-dropdown-item
                  @click.native="invalid(scope.row)"
                  v-if="scope.row.status < 5"
                  ><span class="red">作废</span></el-dropdown-item
                >
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
        <el-table-column
          prop="description"
          label="描述"
          width="auto"
        ></el-table-column>
      </el-table>
      <el-dialog title="盘点详细(变动项)" :visible.sync="dialogTableVisible">
        <el-table
          v-if="countTicketDetails.length > 0"
          :data="countTicketDetails"
        >
          <el-table-column
            property="productName"
            label="商品名称"
          ></el-table-column>
          <el-table-column
            property="supplyName"
            label="供应商"
          ></el-table-column>
          <el-table-column property="batchNo" label="批次号"></el-table-column>
          <el-table-column property="totalCountChange" label="只修改总数">
            <template slot-scope="scope">
              <span v-if="scope.row.totalCountChange == 1" class="yellow"
                >是</span
              >
              <span v-else class="green">否</span>
            </template>
          </el-table-column>
          <el-table-column property="type" label="+/-">
            <template slot-scope="scope">
              <i v-if="scope.row.type == 'plus'" class="el-icon-plus"></i>
              <i v-else class="el-icon-minus"></i>
            </template>
          </el-table-column>
          <el-table-column property="count" label="数量"></el-table-column>
        </el-table>
        <span v-else>盘点无数量变动,盘点数量与原数量一致</span>
      </el-dialog>
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
      timeDefaultShow: "",
      startDateMin: null,
      countTicketDetails: [],
      dialogTableVisible: false,
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
      tableColums: [
        { field: "code", label: "单号", width: "350px" },
        { field: "type", label: "盘点类型", width: "150px" },
        { field: "employeeName", label: "盘点人员", width: "150px" },
        { field: "date", label: "盘点日期", width: "150px" },
        { field: "status", label: "进度", width: "150px" },
        { field: "productType", label: "产品类型", width: "150px" },
        { field: "excelExportFileName", label: "盘点单(初始)", width: "200px" },
        { field: "excelImportFileName", label: "盘点单(完成)", width: "200px" },
        { field: "description", label: "描述", width: "auto" },
      ],
    };
  },
  computed: {
    ...mapGetters(["userInfo", "storageCountTickets"]),
  },
  methods: {
    ...mapActions({
      GetStorageCountTickets: "GetStorageCountTickets",
      DownLoadFile: "DownLoadFile",
      UpdateCountTicket: "UpdateCountTicket",
      UpdateOneInCountTickets: "UpdateOneInCountTickets",
      GetStorageCountTiketDetails: "GetStorageCountTiketDetails",
      SetCurrentStorageCountTicket: "SetCurrentStorageCountTicket",
    }),
    focusOn() {
      this.startDateMin = null;
    },
    searchOnClick() {
      let params = {
        storeCode: this.userInfo.storeCode,
        startDate: this.date[0],
        endDate: this.date[1],
      };
      this.GetStorageCountTickets(params);
    },
    downLoadOnClick(row, type) {
      let fileName =
        type === "exprot" ? row.excelExportFileName : row.excelImportFileName;
      let path = type === "exprot" ? row.excelExportPath : row.excelImportPath;
      let params = { path: path };
      this.DownLoadFile(params)
        .then((response) => {
          let blob = new Blob([response]);
          if (window.navigator.msSaveOrOpenBlob) {
            navigator.msSaveBlob(blob, fileName);
          } else {
            var link = document.createElement("a");
            link.href = window.URL.createObjectURL(blob);
            link.download = fileName;
            link.click();
            //释放内存
            window.URL.revokeObjectURL(link.href);
          }
        })
        .catch((error) => {
          this.$message.error(error.message ? error.message : error);
        });
    },
    detailOnClick(row) {
      let params = { ticketCode: row.code };
      this.GetStorageCountTiketDetails(params)
        .then((res) => {
          if (res.resultStatus == 1) {
            this.countTicketDetails = res.data;
            this.dialogTableVisible = true;
          } else {
            this.$message.error(res.message);
          }
        })
        .catch((err) => {
          this.$message.error(err.message);
        });
    },
    loadTicketOnClick(ticket) {
      this.SetCurrentStorageCountTicket(ticket);
      this.$router.push({
        path: "/main/storageCount",
        query: {
          type: "fromRecord",
        },
      });
    },
    invalid(ticket) {
      ticket.status = -1;
      this.UpdateCountTicket(ticket)
        .then((res) => {
          if (res.resultStatus == 1) {
            this.UpdateOneInCountTickets(res.data);
            this.$message.success("更新成功");
          } else {
            this.$message.warning(res.message);
          }
        })
        .catch((err) => {
          this.$message.error(err.message);
        });
    },
  },
  beforeMount: function () {
    this.timeDefaultShow = new Date().setMonth(new Date().getMonth() - 1);
  },
};
</script>
<style>
.count-record-content-header {
  height: 60px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
.count-record-title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  margin-left: 20px;
}
.storageCountRecord-content {
  margin-top: 5px;
}
.message-info {
  color: #79bbff;
  font-size: 20px;
}
.date-picker {
  margin: 0px 20px;
}
.record-table {
  margin-top: 5px;
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
.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}
.el-icon-arrow-down {
  font-size: 12px;
}
</style>