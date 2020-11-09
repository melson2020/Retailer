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
    <div>
      <el-table
        :data="storageCountTickets"
        :header-cell-style="{ background: '#606266', color: 'white' }"
        class="record-table"
      >
        <el-table-column
          prop="code"
          label="单号"
          width="350px"
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
          width="150px"
        ></el-table-column>
        <el-table-column
          prop="date"
          label="盘点日期"
          width="150px"
        ></el-table-column>
        <el-table-column prop="status" label="进度" width="150px">
          <template slot-scope="scope">
            <span class="yellow" v-if="scope.row.status == 1">已创建</span>
            <span class="yellow" v-else-if="scope.row.status == 2"
              >盘点单已导出</span
            >
            <span class="yellow" v-else-if="scope.row.status == 3"
              >盘点单已导入</span
            >
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
            <el-link type="primary" @click="downLoadOnClick(scope.row)">{{
              scope.row.excelExportFileName
            }}</el-link>
          </template>
        </el-table-column>
        <el-table-column
          prop="excelImportFileName"
          label="盘点单(完成)"
          width="200px"
        >
          <template slot-scope="scope">
            <el-link type="primary" @click="downLoadOnClick(scope.row)">{{
              scope.row.excelImportFileName
            }}</el-link>
          </template></el-table-column
        >
        <el-table-column
          prop="description"
          label="描述"
          width="auto"
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
      timeDefaultShow: "",
      startDateMin: null,
      pickerOptions: {
        onPick: obj => {
          this.startDateMin = new Date(obj.minDate).getTime();
        },
        disabledDate: time => {
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
            }
          },
          {
            text: "最近三天",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 3);
              picker.$emit("pick", [start, end]);
            }
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            }
          }
        ]
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
        { field: "description", label: "描述", width: "auto" }
      ]
    };
  },
  computed: {
    ...mapGetters(["userInfo", "storageCountTickets"])
  },
  methods: {
    ...mapActions({
      GetStorageCountTickets: "GetStorageCountTickets",
      DownLoadFile: "DownLoadFile"
    }),
    focusOn() {
      this.startDateMin = null;
    },
    searchOnClick() {
      let params = {
        storeCode: this.userInfo.storeCode,
        startDate: this.date[0],
        endDate: this.date[1]
      };
      this.GetStorageCountTickets(params);
    },
    downLoadOnClick(row) {
      let params = { path: row.excelExportPath };
      this.DownLoadFile(params)
        .then(response => {
          let blob = new Blob([response]);
          let fileName = row.excelExportFileName;
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
        .catch(error => {
          this.$message.error(error.message ? error.message : error);
        });
    }
  },
  beforeMount: function() {
    this.timeDefaultShow = new Date().setMonth(new Date().getMonth() - 1);
  }
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
.message-info {
  color: #79bbff;
  font-size: 20px;
}
.date-picker {
  margin: 0px 20px;
}
.record-table {
  margin-top: 30px;
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
</style>