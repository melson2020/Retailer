<template>
  <div>
    <div class="header-div">
      <span class="title-name">导入盘点单</span>
      <div>
        <el-button type="primary" @click="setUploadDialogVisible"
          >导入文件</el-button
        >
        <el-button
          type="primary"
          :disabled="!countedList.length > 0"
          @click="submitCountedList"
          >提交盘点单</el-button
        >
        <el-button>返回</el-button>
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
        :cell-style="{ color: '#909399' }"
        class="storage-table"
        :header-cell-style="{ background: '#606266', color: 'white' }"
      >
        <el-table-column
          v-for="(item, i) in tableColums"
          :key="i"
          :prop="item.field"
          :label="item.label"
          :width="item.width"
        ></el-table-column>
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
        { field: "id", label: "ID", width: "auto" },
        { field: "productName", label: "名称", width: "auto" },
        { field: "productType", label: "型号", width: "auto" },
        { field: "productSpecification", label: "规格", width: "auto" },
        { field: "count", label: "数量", width: "150px" },
        { field: "unit", label: "单位", width: "150px" },
        { field: "counted", label: "盘点数量", width: "150px" }
      ],
      countedList: [],
      uploadData: { ticketCode: "2f841758-69d0-4a5d-a058-3e46dde60d18"}
    };
  },
  computed: {
    ...mapGetters(["currentStorageCountTicket", "userInfo"])
  },
  methods: {
    ...mapActions({
      SetCurrentStorageCountTicket: "SetCurrentStorageCountTicket",
      SetActiveSteps: "SetActiveSteps",
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
          id: item[0],
          productName: item[1],
          productType: item[2],
          productSpecification: item[3],
          count: item[4],
          unit: item[5],
          counted: item[6],
          flag: 0
        };
        if (json.counted - json.count > 0) {
          json.flag = 1;
        }
        if (json.counted - json.count < 0) {
          json.flag = -1;
        }
        this.countedList.push(json);
      }
    },
    submitCountedList() {
       let file = this.$refs.upload.$children[0].fileList[0];
       if(!file){
         this.$message.warning("请添加上传文件")
         return
       }
       if(file.status=='success'){
         this.$message.info("文件已上传")
         return
       }
      this.$refs.upload.submit();
    },
    uploadOnSuccess(response) {
     if(response.resultStatus==1){
       this.$message.success("文件上传成功")
       this.SetCurrentStorageCountTicket(response.data);
     }else{
        this.$message.error(response.messgae)
     }
    },
    uploadOnError(err) {     
      this.$message.error(err.message?err.message:err)
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
  padding-top: 30px;
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