<template>
  <el-row
    v-loading="maskloading"
    element-loading-text="检查重复数据中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.6)"
  >
    <div class="header">
      <el-button @click.prevent.stop="saveExcelList" class="top-button" size="small">保存</el-button>
      <el-button @click.prevent.stop="choseFile" class="top-button" size="small">数据导入</el-button>
      <el-button @click.prevent.stop="download" class="top-button" size="small">模板下载</el-button>
      <el-dialog
        title="文件加载"
        :visible.sync="uploadFileDialog"
        :close-on-click-modal="false"
        :show-close="false"
        @close="uploadFileDialogOnClose"
      >
        <el-upload
          ref="upload"
          class="upload-demo"
          drag
          action
          :auto-upload="false"
          :limit="1"
          :on-exceed="onExceed"
          :on-change="uploadChange"
          accept=".xls, .xlsx"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">
            将文件拖到此处，或
            <em>点击上传</em>
          </div>
          <div class="el-upload__tip" slot="tip">只能上传xls/xlsx文件，且不超过500kb</div>
        </el-upload>
        <div slot="footer" class="dialog-footer">
          <el-button @click="closeDialog" v-if="!readingFile">取 消</el-button>
          <el-button type="primary" :loading="readingFile" @click="submitFile">确 定</el-button>
        </div>
      </el-dialog>
    </div>
    <el-col :span="8">
      <div class="show-categroy">
        <div class="title-div">
          <span class="table-title">商品类别</span>
          <!-- <el-button type="primary" size="medium">保存修改</el-button> -->
        </div>
        <el-table border class="table-top" :data="excelCategroyList" style="width: 100%">
          <el-table-column type="index" label="#"></el-table-column>
          <el-table-column
            v-for="(v,i) in categroyTableColums"
            :prop="v.field"
            :label="v.label"
            :width="v.width"
            :key="i"
          >
            <template slot-scope="scope">
              <span v-if="scope.row.isSet">
                <el-input size="mini" placeholder="请输入内容" v-model="selectedItem[v.field]"></el-input>
              </span>
              <span v-else>{{scope.row[v.field]}}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <span
                class="el-tag el-tag--info el-tag--mini"
                style="cursor: pointer;"
                @click="pwdChange(scope.row,scope.$index,true)"
              >{{scope.row.isSet?'保存':"修改"}}</span>
              <span
                v-if="!scope.row.isSet"
                class="el-tag el-tag--danger el-tag--mini"
                style="cursor: pointer;"
              >删除</span>
              <span
                v-else
                class="el-tag el-tag--mini"
                style="cursor: pointer;"
                @click="pwdChange(scope.row,scope.$index,false)"
              >取消</span>
            </template>
          </el-table-column>
        </el-table>
        <div class="el-table-add-row">
          <i class="el-icon-plus" @click="categroyAdd"></i>
        </div>
      </div>
    </el-col>
    <el-col :span="16">
      <div class="show-dict">
        <div class="title-div">
          <div class="title-div-left">
            <span class="table-title">商品目录 ({{excelProductList.length}})</span>
            <el-link
              type="danger"
              class="duplicate-link"
              v-if="duplicateCount>0||isSrearchDuplicate"
              @click.prevent.stop="duplicateSreach"
            >名称重复:{{duplicateCount}} 个 {{isSrearchDuplicate?'显示全部':'点击查看'}}</el-link>
          </div>
          <el-input
            class="fliter-input"
            v-model="searchContent"
            placeholder="请输入内容"
            suffix-icon="el-icon-search"
          ></el-input>
        </div>
        <el-table
          border
          class="table-top"
          :data="productListShow.slice((productTablePage.currentPage-1)*productTablePage.pageSize,productTablePage.currentPage*productTablePage.pageSize)"
          style="width: 100%"
          :row-class-name="tableRowClassName"
        >
          <el-table-column type="index" label="#" :index="indexMethod"></el-table-column>
          <el-table-column
            v-for="(item,i) in productTableColums"
            :prop="item.field"
            :label="item.label"
            :width="item.width"
            :key="i"
          >
            <template slot-scope="scope">
              <span v-if="scope.row.isSet">
                <el-input size="mini" v-model="scope.row[item.field]"></el-input>
              </span>
              <span v-else>{{scope.row[item.field]}}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="auto">
            <template slot-scope="scope">
              <div class="table-operation-template">
                <el-button
                  size="mini"
                  plain
                  circle
                  type="primary"
                  icon="el-icon-edit"
                  v-if="!scope.row.isSet"
                  @click="handleEdit(scope.$index, scope.row)"
                ></el-button>
                <el-button
                  size="mini"
                  plain
                  circle
                  v-if="scope.row.isSet"
                  type="success"
                  icon="el-icon-s-claim"
                 @click="handleSave(scope.$index, scope.row)"
                ></el-button>
                <el-button
                  size="mini"
                  plain
                  circle
                  v-if="!scope.row.isSet"
                  type="danger"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.$index, scope.row)"
                ></el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <div class="el-table-pagination-row">
          <el-pagination
            background
            :current-page="productTablePage.currentPage"
            layout="prev, pager, next"
            @current-change="pageChanged"
            :page-size="productTablePage.pageSize"
            :total="productListShow.length"
            v-if="productListShow.length>=productTablePage.pageSize"
          ></el-pagination>
        </div>
      </div>
    </el-col>
  </el-row>
</template>
<script>
import * as excelHelper from "../../utils/excelHelper";
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
export default {
  data() {
    return {
      readingFile: false,
      selectedItem: {
        name: "",
        comment: ""
      },
      isSrearchDuplicate: false,
      maskloading: false,
      productTablePage: {
        pageSize: 12,
        currentPage: 1,
        total: 0
      },
      searchContent: "",
      categroyTableColums: [
        { field: "name", label: "类别", width: "auto" },
        { field: "comment", label: "描述", width: "auto" }
      ],
      productTableColums: [
        { field: "name", label: "名称", width: "auto" },
        { field: "type", label: "型号", width: "auto" },
        { field: "specification", label: "规格", width: "auto" },
        { field: "unit", label: "单位", width: "auto" },
        { field: "feature", label: "特征", width: "auto" }
      ]
    };
  },
  watch: {
    // 如果 `needToRecheckList` 发生改变，这个函数就会运行
    needToRecheckList: function() {
      if (this.needToRecheckList) {
        this.checkProductionList();
        this.RecheckList(false);
      }
    }
  },
  computed: {
    ...mapGetters([
      "excelCategroyList",
      "uploadFileDialog",
      "excelProductList",
      "needToRecheckList"
    ]),
    productListShow: function() {
      if (this.isSrearchDuplicate) {
        return this.excelProductList.filter(item => {
          return item.isRepeat;
        });
      } else {
        return this.excelProductList.filter(item => {
          let key =
            item.name +
            item.type +
            item.specification +
            item.unit +
            (item.feature ? "" : item.feature);
          let index = key.indexOf(this.searchContent);
          return index != -1;
        });
      }
    },
    duplicateCount: function() {
      return this.excelProductList.filter(item => {
        return item.isRepeat;
      }).length;
    }
  },
  methods: {
    ...mapActions({
      GenrateCategroyListAndProductList: "GenrateCategroyListAndProductList",
      SetUploadDialog: "SetUploadDialog",
      DownloadProductDictTem: "DownloadProductDictTem",
      DeleteOneInImportedList: "DeleteOneInImportedList",
      RecheckList:"RecheckList",
      SaveExcelList:"SaveExcelList"
    }),
    indexMethod(index) {
      index =
        index +
        1 +
        (this.productTablePage.currentPage - 1) *
          this.productTablePage.pageSize;
      return index;
    },
    tableRowClassName({ row }) {
      if (row.isRepeat) {
        return "warning-row";
      } else {
        return "";
      }
    },
    duplicateSreach() {
      this.excelProductList.map(item=>{item.isSet=false})
      this.isSrearchDuplicate = !this.isSrearchDuplicate;
    },
    pwdChange(row, index, cg) {
      if (!cg) {
        //取消按钮点击
        row.isSet = false;
        if (row.id == 0) {
          this.excelCategroyList.splice(index, 1);
        }
      } else {
        if (row.isSet) {
          //保存操作
          console.log("保存");
          row.isSet = false;
          row.name = this.selectedItem.name;
          row.comment = this.selectedItem.comment;
          row.id = index;
          this.selectedItem.name = "";
          this.selectedItem.comment = "";
        } else {
          //修改按钮点击
          console.log("修改");
          this.selectedItem.name = row.name;
          this.selectedItem.comment = row.comment;
          row.isSet = true;
        }
      }
    },
    categroyAdd() {
      this.selectedItem.name = "";
      this.selectedItem.comment = "";
      this.excelCategroyList.push({
        id: 0,
        name: "",
        comment: "",
        isSet: true
      });
    },
    choseFile() {
      this.SetUploadDialog(true);
    },
    onExceed() {
      this.$message.warning("只允许加载一个文件");
    },
    closeDialog() {
      this.SetUploadDialog(false);
    },
    submitFile() {
      let file = this.$refs.upload.$children[0].fileList[0];
      if (!file) {
        this.$message.warning("请添加文件");
        return;
      }
      excelHelper
        .fileToExcel(file)
        .then(tabJson => {
          if (tabJson && tabJson.length > 0) {
            this.GenrateCategroyListAndProductList(tabJson);
          }
        })
        .catch(err => {
          let mss = err.message ? err.message : err;
          this.$message.warning("解析excel错误：" + mss);
        });
    },
    checkProductionList() {
      if (this.excelProductList.length > 0) {
        this.searchContent = "";
        this.maskloading = true;
        //重置检查结果
        this.excelProductList.map(item => {
          item.isRepeat = false;
        });
        for (let i = 0; i < this.excelProductList.length; i++) {
          const pi = this.excelProductList[i];
          for (let j = i + 1; j < this.excelProductList.length - i - 1; j++) {
            const pj = this.excelProductList[j];
            if (pj) {
              if (pi.name === pj.name) {
                pi.isRepeat = true;
                pj.isRepeat = true;
              }
            }
          }
        }
        this.maskloading = false;
      }
    },
    uploadChange(event) {
      console.log("uploadChange");
      console.log(event);
    },
    uploadFileDialogOnClose() {
      this.$refs.upload.clearFiles();
    },
    download() {
      this.DownloadProductDictTem();
    },
    pageChanged(page) {
      this.excelProductList.map(item=>{item.isSet=false})    
      this.productTablePage.currentPage = page;
      this.RecheckList(true)
    },
    CheckHaveUnSaveItem(){
      return this.productListShow.filter(item=>{return item.isSet==true}).length>0
    },
    handleDelete(index, row) {
      this.DeleteOneInImportedList(row);
    },
    handleEdit(index,row){   
      if(this.CheckHaveUnSaveItem()){
         this.$message.warning("有未保存项，请先保存")
         return
      }
      row.isSet=!row.isSet
    },
    handleSave(index,row){
      row.isSet=!row.isSet
      this.RecheckList(true)
    },
    saveExcelList(){
      this.excelProductList.map(item=>{item.isSet=false})
      this.checkProductionList()
      if(this.duplicateCount>0){
         this.$message.warning("存在重复名称，请修改")
         return
      }
      this.SaveExcelList(this.excelProductList)
    }
  }
};
</script>
<style>
.table-title {
  float: left;
  font-size: 30px;
  color: #909399;
  font-weight: bold;
}
.title-div-left {
  display: flex;
  align-items: center;
}
.duplicate-link {
  margin-left: 10px;
  font-size: 20px;
}
.title-div {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 80px;
}
.table-top {
  margin-top: 20px;
}
.show-categroy {
  min-height: 100%;
  height: auto;
  padding: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
}
.show-dict {
  min-height: 100%;
  height: auto;
  padding: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
  margin-left: 30px;
}
.header {
  height: auto;
  padding: 12px 0px;
  display: flex;
  flex-direction: row-reverse;
}
.el-row {
  height: 100%;
}
.el-col {
  height: 1550px;
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
.top-button {
  margin-left: 20px;
}
.fliter-input {
  width: 400px;
  height: 80px;
}
.el-table .warning-row {
  background: #f8bdbd;
}
.table-operation-template {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
