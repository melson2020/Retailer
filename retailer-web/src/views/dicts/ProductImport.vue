<template>
  <el-row
    v-loading="maskloading"
    element-loading-text="检查重复数据中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.6)"
  >
    <!-- <div class="productimport-header">
      <el-button @click.prevent.stop="saveExcelList" class="productimport-top-button" type="primary" icon="el-icon-plus" size="small" :disabled="compareExistingProduct.length<=0||excelCategroyList.length<=0">保存</el-button>
      <el-button @click.prevent.stop="choseFile" class="productimport-top-button" type="primary" size="small">数据导入</el-button>
      <el-button @click.prevent.stop="download" class="productimport-top-button" type="primary" size="small">模板下载</el-button>
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
    </div> -->

    <div>
      <!-- <el-col :span="8">
        <div class="productimport-show-categroy">
          <div class="productimport-content-header">
            <div>
              <span class="productimport-title-name">商品类别 ({{excelCategroyList.length}})</span>
              <el-link
                type="danger"
                class="productimport-duplicate-link"
                v-if="categroyDuplicateCount>0||isCategroyDuplicate"
                @click.prevent.stop="categroyDuplicateSreach"
              >类别重复:{{categroyDuplicateCount}} 个 {{isCategroyDuplicate?'显示全部':'点击查看'}}</el-link>
            </div>
            <div>
              <el-input
              class="productimport-fliter-input"
              size="small"
              v-model="categroySearchContent"
              placeholder="请输入内容"
              suffix-icon="el-icon-search"
              @focus="categroySearchFocus"
              ></el-input>
            </div>
          </div>

          <div class="productimport-content">
            <el-table
              border
              :row-class-name="tableRowClassName"
              class="productimport-table-top" size="small"
              :height="tableHeight"
              :data="categorys"
              :header-row-style="{height:'40px'}"
              :row-style="{height:'40px'}"
              :cell-style="{ padding: '2px', color: '#909399' }"
              :header-cell-style="{ background: '#808080', color: 'white'}">
              <el-table-column type="index" label="#" align="center"></el-table-column>
              <el-table-column
                v-for="(v,i) in categroyTableColums"
                :prop="v.field"
                :label="v.label"
                :width="v.width"
                :key="i"
              >
                <template slot-scope="scope">
                  <span v-if="scope.row.isSet">
                    <el-input size="mini" placeholder="请输入内容" v-model="scope.row[v.field]"></el-input>
                  </span>
                  <span v-else>{{scope.row[v.field]}}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    plain
                    circle
                    type="primary"
                    icon="el-icon-edit"
                    v-if="!scope.row.isSet"
                    @click="categroyEdit(scope.$index, scope.row)"
                  ></el-button>
                  <el-button
                    size="mini"
                    plain
                    circle
                    v-if="scope.row.isSet"
                    type="success"
                    icon="el-icon-s-claim"
                    @click="categroySave(scope.$index, scope.row)"
                  ></el-button>
                  <el-button
                    size="mini"
                    plain
                    circle
                    v-if="!scope.row.isSet"
                    type="danger"
                    icon="el-icon-delete"
                    @click="categroyDelete(scope.$index, scope.row)"
                  ></el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <div class="productimport-content-footer">
            <el-pagination
              background
              :current-page="productCategroyPage.currentPage"
              @current-change="categroyPageChanged"
              layout="prev, pager, next"
              :page-size="productCategroyPage.pageSize"
              :total="excelCategroyListShow.length"
              v-if="excelCategroyListShow.length>=productCategroyPage.pageSize"
            ></el-pagination>
          </div>

        </div>
      </el-col> -->

      <!-- <el-col :span="16"> -->
      <!-- <div class="productimport-show-dict"> -->
      <div class="productimport-content-header">
        <div>
          <span class="productimport-title-name"
            >导入目录 ({{ excelProductList.length }})</span
          >
          <el-link
            type="danger"
            class="productimport-duplicate-link"
            v-if="duplicateCount > 0 || isSrearchDuplicate"
            @click.prevent.stop="duplicateSreach"
            >名称重复:{{ duplicateCount }} 个
            {{ isSrearchDuplicate ? "显示全部" : "点击查看" }}</el-link
          >
        </div>
        <div>
          <el-input
            class="productimport-fliter-input"
            size="small"
            v-model="searchContent"
            placeholder="请输入内容"
            suffix-icon="el-icon-search"
            @focus="productSearchFocus"
          ></el-input>
          <el-button
            @click.prevent.stop="download"
            class="productimport-top-button"
            type="primary"
            size="small"
            >模板下载</el-button
          >
          <el-button
            @click.prevent.stop="choseFile"
            class="productimport-top-button"
            type="primary"
            size="small"
            >数据导入</el-button
          >
          <el-button
            @click.prevent.stop="saveExcelList"
            class="productimport-top-button"
            type="primary"
            icon="el-icon-plus"
            size="small"
            :disabled="
              compareExistingProduct.length <= 0 ||
              excelCategroyList.length <= 0
            "
            >保存</el-button
          >

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
              accept=".xls, .xlsx"
            >
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">
                将文件拖到此处，或
                <em>点击上传</em>
              </div>
              <div class="el-upload__tip" slot="tip">
                只能上传xls/xlsx文件，且不超过500kb
              </div>
            </el-upload>
            <div slot="footer" class="dialog-footer">
              <el-button @click="closeDialog" v-if="!readingFile"
                >取 消</el-button
              >
              <el-button
                type="primary"
                :loading="readingFile"
                @click="submitFile"
                >确 定</el-button
              >
            </div>
          </el-dialog>
        </div>
      </div>

      <div class="productimport-content">
        <el-table
          border
          :row-class-name="tableRowClassName"
          class="productimport-table-top"
          size="small"
          :height="tableHeight"
          :data="products"
          :header-row-style="{ height: '40px' }"
          :row-style="{ height: '40px' }"
          :cell-style="{ padding: '2px', color: '#909399' }"
          :header-cell-style="{ background: '#808080', color: 'white' }"
        >
          <el-table-column
            type="index"
            label="#"
            :index="indexMethod"
            align="center"
          ></el-table-column>
          <el-table-column
            v-for="(item, i) in productTableColums"
            :prop="item.field"
            :label="item.label"
            :width="item.width"
            :key="i"
          >
            <template slot-scope="scope">
              <span v-if="scope.row.isSet">
                <el-input
                  size="mini"
                  v-model="scope.row[item.field]"
                ></el-input>
              </span>
              <span v-else>{{ scope.row[item.field] }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="auto" align="center">
            <template slot-scope="scope">
              <div class="productimport-table-operation-template">
                <!-- <el-button
                      size="mini"
                      plain
                      circle
                      type="primary"
                      icon="el-icon-edit"
                      v-if="!scope.row.isSet"
                      @click="handleEdit(scope.$index, scope.row)"
                    ></el-button> -->
                <!-- <el-button
                      size="mini"
                      plain
                      circle
                      v-if="scope.row.isSet"
                      type="success"
                      icon="el-icon-s-claim"
                      @click="handleSave(scope.$index, scope.row)"
                    ></el-button> -->
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
      </div>

      <div class="productimport-content-footer">
        <el-pagination
          background
          :current-page="productTablePage.currentPage"
          layout="prev, pager, next"
          @current-change="pageChanged"
          :page-size="productTablePage.pageSize"
          :total="productListShow.length"
          v-if="productListShow.length >= productTablePage.pageSize"
        ></el-pagination>
      </div>
      <!-- </div> -->
      <!-- </el-col> -->
    </div>
  </el-row>
</template>
<script>
import * as excelHelper from "../../utils/excelHelper";
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
export default {
  data() {
    return {
      tableHeight: window.innerHeight - 210,
      readingFile: false,
      isSrearchDuplicate: false,
      // isCategroyDuplicate: false,
      maskloading: false,
      productTablePage: {
        pageSize: 12,
        currentPage: 1,
        height: "",
      },
      // productCategroyPage: {
      //   pageSize: 12,
      //   currentPage: 1,
      //   height:""
      // },
      searchContent: "",
      // categroySearchContent:"",
      // categroyTableColums: [
      //   { field: "name", label: "类别", width: "auto" },
      //   { field: "comment", label: "描述", width: "auto" }
      // ],
      productTableColums: [
        { field: "name", label: "名称", width: "auto" },
        { field: "type", label: "型号", width: "auto" },
        { field: "specification", label: "规格", width: "auto" },
        { field: "unit", label: "单位", width: "auto" },
        { field: "categoryName", label: "类别", width: "auto" },
        // { field: "feature", label: "特征", width: "auto" }
      ],
    };
  },
  computed: {
    ...mapGetters([
      "excelCategroyList",
      "uploadFileDialog",
      "excelProductList",
      "categroyDuplicateCount",
      "userInfo",
      "productList",
    ]),
    compareExistingProduct() {
      let mapExisting = new Map();
      let mapExistingCategoryId = new Map();
      let result = new Array();

      // console.log(this.productList);
      for (let i = 0; i < this.productList.length; i++) {
        if (!mapExisting.has(this.productList[i].name)) {
          mapExisting.set(this.productList[i].name, this.productList[i]);
          mapExistingCategoryId.set(
            this.productList[i].categoryName,
            this.productList[i].categoryId
          );
        }
      }
      // console.log(mapExistingCategoryId)
      for (let i = 0; i < this.excelProductList.length; i++) {
        // console.log(this.excelProductList[i].name);
        if (!mapExisting.has(this.excelProductList[i].name)) {
          // console.log(this.excelProductList[i].name);
          // console.log(mapExistingCategoryId.get(this.excelProductList[i].categoryName));
          // eslint-disable-next-line vue/no-side-effects-in-computed-properties
          // this.excelProductList[i].categoryId = mapExistingCategoryId.get(this.excelProductList[i].categoryName);
          // eslint-disable-next-line vue/no-side-effects-in-computed-properties
          this.excelProductList[i].storeCode = this.userInfo.storeCode;
          // console.log(this.excelProductList[i].categoryId);
          result.push(this.excelProductList[i]);
        }
      }
      return result;
    },
    productListShow: function () {
      // console.log(this.compareExistingProduct);

      if (this.isSrearchDuplicate) {
        // return this.excelProductList.filter(item => {
        return this.compareExistingProduct.filter((item) => {
          return item.isRepeat;
        });
      } else {
        // return this.excelProductList.filter(item => {
        return this.compareExistingProduct.filter((item) => {
          let key =
            item.name +
            item.type +
            item.specification +
            item.unit +
            item.categoryName;
          let index = key
            .toUpperCase()
            .indexOf(this.searchContent.toUpperCase());
          return index != -1;
        });
      }
    },
    products() {
      return this.productListShow.slice(
        (this.productTablePage.currentPage - 1) *
          this.productTablePage.pageSize,
        this.productTablePage.currentPage * this.productTablePage.pageSize
      );
    },
    // compareExistingCategory(){
    //   let mapExisting = new Map();
    //   let result=new Array();
    //   for(let i=0;i<this.productList.length;i++){
    //     if(!mapExisting.has(this.productList[i].categoryName))
    //     {
    //       mapExisting.set(this.productList[i].categoryName,this.productList[i])
    //     }
    //   }
    //   for(let i=0;i<this.excelCategroyList.length;i++)
    //   {
    //     if(!mapExisting.has(this.excelCategroyList[i].name))
    //     {
    //       result.push(this.excelCategroyList[i]);
    //     }
    //   }
    //   return result;
    // },
    // excelCategroyListShow: function() {
    //   if (this.isCategroyDuplicate) {
    //     // return this.excelCategroyList.filter(item => {
    //     return this.compareExistingCategory.filter(item => {
    //       return item.isRepeat;
    //     });
    //   } else {
    //     // return this.excelCategroyList.filter(item=>{
    //     return this.compareExistingCategory.filter(item=>{
    //       let key=item.name+item.comment;
    //     let index=key.toUpperCase().indexOf(this.categroySearchContent.toUpperCase())
    //      return index != -1;
    //   })
    // }},
    // categorys(){
    //   return this.excelCategroyListShow.slice((this.productCategroyPage.currentPage-1)*this.productCategroyPage.pageSize,this.productCategroyPage.currentPage*this.productCategroyPage.pageSize)
    // },
    // categroyDuplicateCount: function() {
    //   return this.compareExistingCategory.filter(item => {
    //     return item.isRepeat;
    //   }).length;
    // },
    duplicateCount: function () {
      return this.compareExistingProduct.filter((item) => {
        return item.isRepeat;
      }).length;
    },
  },
  methods: {
    ...mapActions({
      GenrateCategroyListAndProductList: "GenrateCategroyListAndProductList",
      SetUploadDialog: "SetUploadDialog",
      DownloadProductDictTem: "DownloadProductDictTem",
      DeleteOneInImportedList: "DeleteOneInImportedList",
      CheckDuplicateList: "CheckDuplicateList",
      // SaveExcelList: "SaveExcelList",
      SaveExcelListNew: "SaveExcelListNew",
      DeleteOneInCategroyList: "DeleteOneInCategroyList",
      GetProductList: "GetProductList",
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
      this.excelProductList.map((item) => {
        item.isSet = false;
      });
      this.productTablePage.currentPage = 1;
      this.isSrearchDuplicate = !this.isSrearchDuplicate;
    },
    // categroyDuplicateSreach() {
    //   this.excelCategroyList.map(item => {
    //     item.isSet = false;
    //   });
    //   this.productCategroyPage.currentPage = 1;
    //   this.isCategroyDuplicate = !this.isCategroyDuplicate;
    // },
    choseFile() {
      this.SetUploadDialog(true);
    },
    onExceed() {
      this.$message.warning("只允许加载一个文件");
    },
    closeDialog() {
      this.SetUploadDialog(false);
    },
    //导入excel 至界面
    submitFile() {
      let params = { storeCode: this.userInfo.storeCode };
      this.GetProductList(params);
      let file = this.$refs.upload.$children[0].fileList[0];
      if (!file) {
        this.$message.warning("请添加文件");
        return;
      }
      excelHelper
        .fileToExcel(file, false)
        .then((tabJson) => {
          if (tabJson && tabJson.length > 0) {
            console.log("tabjson", tabJson);
            var formateCheckRes = this.checkExcelFormat(tabJson, [
              "别名",
              "型号",
              "规格",
              "单位",
            ]);
            if (!formateCheckRes) {
              this.$message.warning("导入文件格式错误");
              return;
            }
            this.GenrateCategroyListAndProductList(tabJson);
            this.$message.warning("系统已为你过滤掉已存在商品！");
          }
        })
        .catch((err) => {
          let mss = err.message ? err.message : err;
          this.$message.warning("解析excel错误：" + mss);
        });
    },
    checkExcelFormat(tabJson, fields) {
      for (let index = 0; index < tabJson.length; index++) {
        const sheet = tabJson[index];
        var tempData = sheet.sheet[0];
        for (let i = 0; i < fields.length; i++) {
          const filed = fields[i];
          if (tempData) {
            var value = tempData[filed];
            if (typeof value == "undefined") {
              return false;
            }
          }
        }
      }
      return true;
    },
    uploadFileDialogOnClose() {
      this.$refs.upload.clearFiles();
    },
    download() {
      this.DownloadProductDictTem();
    },
    // categroyPageChanged(page) {
    //   this.excelCategroyList.map(item => {
    //     item.isSet = false;
    //   });
    //   this.productCategroyPage.currentPage = page;
    //   this.CheckDuplicateList(true);
    // },
    pageChanged(page) {
      this.excelProductList.map((item) => {
        item.isSet = false;
      });
      this.productTablePage.currentPage = page;
      this.CheckDuplicateList(true);
    },
    CheckHaveUnSaveItem() {
      return (
        this.productListShow.filter((item) => {
          return item.isSet == true;
        }).length > 0
      );
    },
    // CheckUnSaveCategroy() {
    //   return (
    //     this.excelCategroyListShow.filter(item => {
    //       return item.isSet;
    //     }).length > 0
    //   );
    // },
    handleDelete(index, row) {
      this.DeleteOneInImportedList(row);
    },
    // handleEdit(index, row) {
    //   if (this.CheckHaveUnSaveItem()) {
    //     this.$message.warning("有未保存项，请先保存");
    //     return;
    //   }
    //   row.isSet = !row.isSet;
    // },
    // handleSave(index, row) {
    //   row.isSet = !row.isSet;
    //   this.CheckDuplicateList();
    // },
    // categroyDelete(index, row) {
    //   this.DeleteOneInCategroyList(row);
    // },
    // categroyEdit(index, row) {
    //   if (this.CheckUnSaveCategroy()) {
    //     this.$message.warning("有未保存项，请先保存");
    //     return;
    //   }
    //   row.isSet = !row.isSet;
    // },
    // categroySave(index, row) {
    //   row.isSet = !row.isSet;
    //   this.CheckDuplicateList();
    // },
    saveExcelList() {
      this.excelProductList.map((item) => {
        item.isSet = false;
      });
      this.CheckDuplicateList();
      if (this.duplicateCount > 0) {
        this.$message.warning("存在重复名称，请修改");
        return;
      }
      this.isSrearchDuplicate=false;
      // let params={storeCode:this.userInfo.storeCode,productList:this.excelProductList,categoryList:this.excelCategroyList}
      // let params={storeCode:this.userInfo.storeCode,productList:this.productListShow,categoryList:this.excelCategroyListShow}
      let params = {
        storeCode: this.userInfo.storeCode,
        productList: this.productListShow,
      };
      this.SaveExcelListNew(params);
    },
    setpageSize() {
      let rect = this.tableHeight - 40;
      this.productTablePage.height = rect + 40;
      // this.productCategroyPage.height=rect+40;
      let pageSize = Math.floor(rect / 40);
      // this.productCategroyPage.pageSize=pageSize;
      this.productTablePage.pageSize = pageSize;
    },
    // categroySearchFocus(){
    //   this.productCategroyPage.currentPage=1;
    // },
    productSearchFocus() {
      this.productTablePage.currentPage = 1;
    },
  },
  mounted: function () {
    this.$nextTick(function () {
      this.setpageSize();
    });
  },
};
</script>
<style>
.productimport-header {
  height: 60px;
  display: flex;
  flex-direction: row-reverse;
}
.productimport-top-button {
  margin-left: 20px;
}
.productimport-content-header {
  height: 60px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
.productimport-title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  margin-left: 20px;
}
.productimport-fliter-input {
  width: 400px;
}
.productimport-content {
  margin-top: 5px;
}
.productimport-content-footer {
  margin-top: 20px;
  height: 60px;
  align-items: center;
  justify-content: space-between;
}
.productimport-show-categroy {
  min-height: 100%;
  height: auto;
  padding: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
  margin-top: 15px;
}
/* .productimport-show-dict {
  min-height: 100%;
  height: auto;
  padding: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
  margin-left: 30px;
  margin-top: 15px;
} */

.productimport-show-dict {
  min-height: 100%;
  height: auto;
  padding: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
  margin-top: 15px;
}

/* .title-name {
  float: left;
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
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
  height: 80vh;
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
} */
</style>
