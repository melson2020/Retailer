<template>
  <div>
    <div class="header-div">
      <span class="title-name">导入盘点单</span>
      <div>
        <el-button type="primary" size="small" @click="setUploadDialogVisible"
          >导入文件</el-button
        >
        <el-button
          type="primary" size="small"
          :disabled="!countedList.length > 0"
          @click="submitCountedList"
          >提交盘点单</el-button
        >
        <!-- <el-button size="small">返回</el-button> -->
        <el-dialog
          title="文件上传"
          :visible.sync="uploadDialogVisible"
          :close-on-click-modal="false"
          :show-close="false"
        >
          <el-upload
            ref="upload"
            class="upload-demo"
            drag
            :on-success="uploadOnSuccess"
            :on-error="uploadOnError"
            :data="uploadData"
            action="/api/storage/uploadCountedExcel"
            :auto-upload="false"
            :limit="1"
            accept=".xls"
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              将文件拖到此处，或
              <em>点击上传</em>
            </div>
            <div class="el-upload__tip" slot="tip">
              只能上传xls文件，且不超过500kb
            </div>
          </el-upload>
          <div slot="footer" class="dialog-footer">
            <el-button @click="setUploadDialogVisible">取 消</el-button>
            <el-button type="primary" @click="submitFile">确 定</el-button>
          </div>
        </el-dialog>
      </div>
    </div>
    <div class="table-div">
      <el-table
        :data="countedList"
        border
        size="small"
        :header-row-style="{height:'40px'}"
          :row-style="{height:'40px'}"
          :cell-style="{ padding: '2px', color: '#909399' }"
          :header-cell-style="{ background: '#808080', color: 'white'}"
        :span-method="objectSpanMethod"
      >
        <el-table-column
          v-for="(item, i) in tableColums"
          :key="i"
          :prop="item.field"
          :label="item.label"
          :width="item.width"
        ></el-table-column>
        <el-table-column prop="totalCounted" label="盘点总数" width="auto">
          <template slot-scope="scope">
            <span v-if="scope.row.dataCorrect">{{
              scope.row.totalCounted
            }}</span>
            <div v-else>
              {{ scope.row.totalCounted }} <span class="red">(*总数不符)</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="结果" width="150px">
          <template slot-scope="scope">
            <i v-if="scope.row.flag > 0" class="el-icon-top yellow"></i>
            <i v-else-if="scope.row.flag < 0" class="el-icon-bottom red"></i>
            <i v-else class="el-icon-check green"></i>
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
      uploadDialogVisible: false,
      tableColums: [
        { field: "productId", label: "ID", width: "auto" },
        { field: "productName", label: "名称", width: "auto" },
        { field: "type", label: "型号", width: "auto" },
        { field: "specification", label: "规格", width: "auto" },
        { field: "batchNo", label: "批次", width: "auto" },
        { field: "supplyName", label: "供应商", width: "auto" },
        { field: "count", label: "现有数量", width: "auto" },
        { field: "totalCount", label: "现有总数", width: "auto" },
        { field: "countUnit", label: "单位", width: "auto" },
        { field: "counted", label: "盘点数量", width: "auto" }
      ],
      countedList: [],
      uploadData: { ticketCode: "" }
    };
  },
  computed: {
    ...mapGetters([
      "currentStorageCountTicket",
      "userInfo",
      "previewStorageList"
    ]),
    spanArr() {
      let spanRowArr = [];
      let pos = 0;
      for (let index = 0; index < this.countedList.length; index++) {
        if (index == 0) {
          spanRowArr.push(1);
          pos = 0;
        } else {
          var pre = this.countedList[index - 1];
          var current = this.countedList[index];
          if (pre.productId === current.productId) {
            spanRowArr[pos] += 1;
            spanRowArr.push(0);
          } else {
            spanRowArr.push(1);
            pos = index;
          }
        }
      }
      return spanRowArr;
    }
  },
  methods: {
    ...mapActions({
      SetCurrentStorageCountTicket: "SetCurrentStorageCountTicket",
      SetActiveSteps: "SetActiveSteps",
      UpdateStorageAfterCount: "UpdateStorageAfterCount",
      SetUpdateBatchList: "SetUpdateBatchList",
      SetTicketStatus: "SetTicketStatus"
    }),
    setUploadDialogVisible() {
      this.uploadDialogVisible = !this.uploadDialogVisible;
    },
    submitFile() {
      let file = this.$refs.upload.$children[0].fileList[0];
      if (!file) {
        this.$message.warning("请添加文件");
        return;
      }
      var fileType = file.name.endWith(".xls");
      var size = file.size / 1024 > 500;
      if (!fileType) {
        this.$message.warning("文件类型不匹配");
        return;
      }
      if (size) {
        this.$message.warning("文件过大");
        return;
      }
      excelHelper
        .fileToExcel(file, true)
        .then(tabJson => {
          this.countedList = [];
          this.genrateExcelToResultList(tabJson, 3);
          this.setUploadDialogVisible();
        })
        .catch(err => {
          let mss = err.message ? err.message : err;
          this.$message.warning("解析excel错误：" + mss);
        });
    },
    genrateExcelToResultList(tabJson, headerRow) {
      var array = tabJson[0].sheet;
      for (let index = headerRow; index < array.length - 1; index++) {
        var item = array[index];
        var json = {
          productId: item[0],
          productName: item[1],
          type: item[2],
          specification: item[3],
          batchNo: item[4],
          supplyName: item[5],
          count: item[6],
          totalCount: item[7],
          countUnit: item[8],
          counted: item[9],
          totalCounted: item[10],
          flag: 0,
          dataCorrect: true
        };
        //检查批次数量是否匹配
        if (json.counted - json.count > 0) {
          json.flag = 1;
        }
        if (json.counted - json.count < 0) {
          json.flag = -1;
        }
        this.countedList.push(json);
      }
      this.checkCountedList();
    },
    checkCountedList() {
      if (this.countedList.length <= 0) return;
      this.countedList.map((item, index) => {
        var count = 0;
        this.countedList
          .filter(c => {
            return c.productId === item.productId;
          })
          .map(s => {
            var sc = parseInt(s.counted);
            if (isNaN(sc)) {
              sc = 0;
              s.counted = 0;
            }
            count += sc;
          });
        var tc = parseInt(item.totalCounted);
        var c = parseInt(item.totalCount);
        var preItem = this.countedList[index - 1];
        if (isNaN(tc)) {
          if (preItem && preItem.productId === item.productId) {
            item.totalCounted = preItem.totalCounted;
            tc = preItem.totalCounted;
          } else {
            item.totalCounted = 0;
            tc = 0;
          }
        }
        if (isNaN(c)) {
          if (preItem && preItem.productId === item.productId) {
            item.totalCount = preItem.totalCount;
          } else {
            item.totalCount = 0;
          }
        }
        item.dataCorrect = count === tc;
        //检查总数是否匹配
        if (item.totalCounted - item.totalCount > 0) {
          item.flag = 1;
        }
        if (item.totalCounted - item.totalCount < 0) {
          item.flag = -1;
        }
      });
    },
    checkfileContent() {
      var matched = this.previewStorageList.length === this.countedList.length;
      if (matched) {
        for (let index = 0; index < this.countedList.length; index++) {
          let item = this.countedList[index];
          var key = item.productId + "" + item.batchNo;
          var exist = this.previewStorageList.filter(p => {
            var batchNo=p.batchNo?p.batchNo:'';
            var pKey = p.productId + "" + batchNo;
            return pKey === key;
          }).length;
          if (exist <= 0) {
            matched = false;
            break;
          }
        }
      }
      return matched;
    },
    submitCountedList() {
      let file = this.$refs.upload.$children[0].fileList[0];
      if (!file) {
        this.$message.warning("请添加上传文件");
        return;
      }
      if (!this.checkfileContent()) {
        this.$message.warning("导入文件与盘点单不匹配");
        return;
      }
      if (
        this.countedList.length > 0 &&
        this.countedList.filter(item => {
          return !item.dataCorrect;
        }).length > 0
      ) {
        this.$message.warning("盘点数据不准确，请检查");
        return;
      }
      if (!this.currentStorageCountTicket.code) {
        this.$message.warning("暂无盘点单");
        return;
      }
      if (
        file.status === "success" ||
        this.currentStorageCountTicket.status >= 3
      ) {
        this.updaetStorage();
      } else {
        this.uploadData.ticketCode = this.currentStorageCountTicket.code;
        this.$refs.upload.submit();
      }
    },
    uploadOnSuccess(response) {
      if (response.resultStatus == 1) {
        this.$message.success("文件上传成功");
        this.SetCurrentStorageCountTicket(response.data);
        this.updaetStorage();
      } else {
        this.$message.error(response.messgae);
      }
    },
    updaetStorage() {
      var updateList = this.countedList.filter(item => {
        return item.flag != 0;
      });
      var params = {
        ticket: this.currentStorageCountTicket,
        dtoList: updateList
      };
      this.UpdateStorageAfterCount(params)
        .then(res => {
          if (res.resultStatus == 1) {
            //盘点完成
            this.SetActiveSteps(5);
            this.SetTicketStatus(5);
            this.$message.success("盘点成功");
            this.$router.push({ path: "/main/storageCount/complete" });
          } else if (res.resultStatus == 2) {
            //有批次信息
            this.SetActiveSteps(4);
            this.SetTicketStatus(4);
            this.SetUpdateBatchList(res.data);
            this.$router.push({ path: "/main/storageCount/updateBatch" });
          } else {
            this.$message.error(res.message);
          }
        })
        .catch(err => {
          this.$message.error(err.messgae ? err.message : err);
        });
    },
    uploadOnError(err) {
      this.$message.error(err.message ? err.message : err);
    },
    objectSpanMethod({ rowIndex, columnIndex }) {
      if (
        columnIndex <= 3 ||
        columnIndex === 7 ||
        columnIndex === 8 ||
        columnIndex === 10
      ) {
        const _row = this.spanArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col
        };
      }
    }
  }
};
</script>
<style>
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
.upload-demo {
  text-align: center;
}
.table-div {
  padding-top: 5px;
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