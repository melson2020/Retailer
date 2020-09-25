<template>
  <div>
    <div class="top-area">
      <div class="title-div">
        <div class="title-div-left">
          <span class="title-name">商品目录</span>
          <el-input
            class="fliter-input"
            v-model="searchContent"
            placeholder="搜索名称 / 型号"
            suffix-icon="el-icon-search"
            @focus="searchFocus"
          ></el-input>
        </div>
        <el-button
          plain
          circle
          type="primary"
          icon="el-icon-plus"
          @click="handleNewProduct('newProduct')"
        />
      </div>
    </div>
    <el-table :data="list" border class="product-table">
      <el-table-column prop="name" label="名字" align="left"></el-table-column>
      <el-table-column prop="type" label="型号" align="center"></el-table-column>
      <el-table-column prop="specification" label="规格" align="center"></el-table-column>
      <el-table-column prop="unit" label="单位" align="center"></el-table-column>
      <el-table-column prop="categoryName" label="类别" align="center"></el-table-column>
      <!-- <el-table-column prop="feature" label="特征" align="center"></el-table-column> -->
      <el-table-column label="特征" align="center" width="auto">
        <template slot-scope="scope">
            <el-tag
              :key="tag"
              v-for="tag in scope.row.feature"
              :disable-transitions="false"
              size="small">
            {{tag}}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleEdit(scope.$index,scope.row)"
            plain
            circle
            type="primary"
            icon="el-icon-edit"
          />
          <el-button
            size="mini"
            @click="handleDelete(scope.$index,scope.row)"
            plain
            circle
            type="danger"
            icon="el-icon-delete"
          />
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
        background
        :current-page="listQuery.page"
        layout="prev, pager, next, total, jumper"
        :page-size="listQuery.limit"
        :total="productLishPageShow.length"
        v-show="productLishPageShow.length>listQuery.limit"
        @current-change="pageChange"
      ></el-pagination>
    </div>

    <el-dialog
      title="编辑"
      :visible.sync="editProductFormVisible"
      :close-on-click-modal="false"
      :show-close="false"
    >
      <el-form :model="editProduct" :rules="rules" ref="editProduct" label-width="120px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="名称" prop="name">
              <el-input v-model="editProduct.name" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="型号" prop="type">
              <el-input v-model="editProduct.type" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="8" prop="specification">
            <el-form-item label="规格" prop="specification">
              <el-input v-model="editProduct.specification" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>


        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="editProduct.unit" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="8" prop="categoryName">
            <el-form-item label="类别" prop="categoryName">
              <!-- <el-input v-model="editProduct.categoryName" autocomplete="off" style="width: 100%;"></el-input> -->
              <template>
                <el-select v-model="editProduct.categoryId" filterable placeholder="请选择" @change="CategoryChange">
                  <el-option
                    v-for="item in calCategoryList"
                    :key="item.categoryId"
                    :label="item.name"
                    :value="item.categoryId">
                  </el-option>
                </el-select>
              </template>

            </el-form-item>
          </el-col>

          <el-col :span="2">
            <el-button
              plain
              circle
              size="mini"
              type="primary"
              icon="el-icon-edit"
              @click="handleEditCategory()"
            />
          </el-col>

        </el-row>

        <el-tag
          :key="tag"
          v-for="tag in editTags"
          closable
          :disable-transitions="false"
          size="small"
          @close="handleEditTagsClose(tag)"
        >{{tag}}</el-tag>
        <el-input
          class="input-new-tag"
          v-if="inputEditVisible"
          v-model="inputEditValue"
          ref="saveEditTagInput"
          size="small"
          @keyup.enter.native="handleEditInputConfirm"
          @blur="handleEditInputConfirm"
        ></el-input>
        <el-button v-else class="button-new-tag" size="small" @click="showEditInput">+ 标签</el-button>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editProductFormVisible = false" v-if="!loading">取消</el-button>
        <el-button type="primary" @click="onEditProduct('editProduct')" :loading="loading">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="新增"
      :visible.sync="newProductFormVisible"
      :close-on-click-modal="false"
      :show-close="false"
    >
    <el-form :model="newProduct" :rules="rules" ref="newProduct" label-width="120px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="名称" prop="name">
              <el-input v-model="newProduct.name" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="型号" prop="type">
              <el-input v-model="newProduct.type" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="8" prop="specification">
            <el-form-item label="规格" prop="specification">
              <el-input v-model="newProduct.specification" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>


        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="newProduct.unit" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="8" prop="categoryId">
            <el-form-item label="类别" prop="categoryId">
              <!-- <el-input v-model="editProduct.categoryName" autocomplete="off" style="width: 100%;"></el-input> -->
              <template>
                <el-select v-model="newProduct.categoryId" filterable placeholder="请选择" @change="CategoryChange">
                  <el-option
                    v-for="item in calCategoryList"
                    :key="item.categoryId"
                    :label="item.name"
                    :value="item.categoryId">
                  </el-option>
                </el-select>
              </template>

            </el-form-item>
          </el-col>

          <el-col :span="2">
            <el-button
              plain
              circle
              size="mini"
              type="primary"
              icon="el-icon-edit"
              @click="handleEditCategory()"
            />
          </el-col>

        </el-row>

        <el-tag
          :key="tag"
          v-for="tag in editTags"
          closable
          :disable-transitions="false"
          size="small"
          @close="handleEditTagsClose(tag)"
        >{{tag}}</el-tag>
        <el-input
          class="input-new-tag"
          v-if="inputEditVisible"
          v-model="inputEditValue"
          ref="saveEditTagInput"
          size="small"
          @keyup.enter.native="handleEditInputConfirm"
          @blur="handleEditInputConfirm"
        ></el-input>
        <el-button v-else class="button-new-tag" size="small" @click="showEditInput">+ 标签</el-button>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="newProductFormVisible = false" v-if="!loading">取消</el-button>
      <el-button type="primary" @click="onNewProduct('newProduct')" :loading="loading">确定</el-button>
    </div>

    </el-dialog>


    <el-dialog
      title="编辑分类"
      :visible.sync="editCategoryVisible"
      :close-on-click-modal="false"
      :show-close="true"
      @close="cateClose"
    >

        <div class="title-div">
          <div class="title-div-left">
            <el-input
              class="fliter-input"
              v-model="searchCategory"
              placeholder="查询或添加新的分类"
              @focus="searchCate"
            ></el-input>
            <el-button v-if="showCategoryList<1" plain circle type="primary" size="mini" icon="el-icon-plus" @click="addCategory()"/>
          </div>
        </div>

      <el-table :data="showCategoryList" border class="category-table">
              <!-- <el-table-column prop="name" label="名称" align="center"></el-table-column> -->
        <el-table-column type="index" label="#"></el-table-column>
          <el-table-column
            v-for="(v,i) in categroyTableColums"
            :prop="v.field"
            :label="v.label"
            :width="v.width"
            :align="v.align"
            :key="i"
          >
          <template slot-scope="scope">
            <span v-if="scope.row.isSet">
              <el-input size="mini" v-model="scope.row[v.field]"></el-input>
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

      <div class="pagination">
        <el-pagination
          background
          :current-page="categoryQuery.page"
          layout="prev, pager, next, total, jumper"
          :page-size="categoryQuery.limit"
          :total="calCategoryList.length"
          v-show="calCategoryList.length>categoryQuery.limit"
          @current-change="pageChangeCate"
        ></el-pagination>

      </div>

    </el-dialog>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
export default {
  data() {
    return {
      searchContent: "",
      searchCategory:"",
      editProductFormVisible: false,
      newProductFormVisible: false,
      editCategoryVisible:false,
      loading: false,
      newProduct: {
        id: "",
        name: "",
        type: "",
        specification: "",
        unit: "",
        feature: "",
        categoryId: "",
        categoryName: "",
      },
      listQuery: {
        page: 1,
        limit: 12
      },
      categoryQuery: {
        page: 1,
        limit: 8
      },
      editProduct: {
        id: "",
        name: "",
        type: "",
        specification: "",
        unit: "",
        feature: "",
        categoryId: "",
        categoryName: "",

      },
      editTags: [],
      inputEditVisible: false,
      inputEditValue: "",
      rules: {
        name: [
          { required: true, message: "请输入名称", trigger: "blur" },
          { min: 2, max: 55, message: "长度在 2 到 55 个字符", trigger: "blur" }
        ],
        type: [{ required: true, message: "请输入型号", trigger: "blur" }],
        categoryId: [
          { required: true, message: "请输入类别", trigger: "blur" }
        ]
      },
      category:{
        id:"",
        name:"",
        comment:"",
        storeCode:"",
        categoryId:"",
      },
      categroyTableColums: [
        { field: "name", label: "类别", width: "auto",align:"center"},
      ],
    };
  },
  methods: {
    ...mapActions({
      GetProductList: "GetProductList",
      DeleteProduct: "DeleteProduct",
      QueryProductObj: "QueryProductObj",
      SaveProduct: "SaveProduct",
      GetCategoryList:"GetCategoryList",
      SaveCategory:"SaveCategory",
      PushCategoryList:"PushCategoryList",
      DeleteCategory:"DeleteCategory",
    }),
    searchFocus() {
      this.listQuery.page = 1;
    },
    pageChange(page) {
      this.listQuery.page = page;
    },
    handleDelete(index, row) {
      let prod = { id: row.id, index: index };
      this.$messageBox
        .confirm("确认删除？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(() => {
          this.DeleteProduct(prod);
        });
    },
    handleEdit(index, row) {
      let prod = { id: row.id, index: index };
      this.QueryProductObj(prod)
        .then(res => {
          if (res.resultStatus == 1) {
            let params = { storeCode: this.userInfo.storeCode };
            this.GetCategoryList(params);
            this.editProduct = res.data;
            this.editProductFormVisible = true;
            this.editTags = row.feature;
          } else {
            this.$message.error(res.message);
          }
        })
        .catch(err => {
          let alert = err.message ? err.message : err;
          this.$message.error(alert);
        });
    },
    handleNewProduct(newForm){
      let params = { storeCode: this.userInfo.storeCode };
      this.GetCategoryList(params);
      if (this.$refs[newForm]) {
        this.$refs[newForm].resetFields();
      }

      this.newProductFormVisible = true;
    },
    onEditProduct(editForm) {
      this.$refs[editForm].validate(valid => {
        if (valid) {
          let fe = JSON.stringify(this.editTags);
          this.editProduct.feature = fe;
          this.editTags=[];
          this.SaveProduct(this.editProduct)
            .then(res => {
              if (res.resultStatus == 1) {
                let params = { storeCode: this.userInfo.storeCode };
                this.GetProductList(params);
                this.editProductFormVisible = false;
                this.$message({
                  showClose: true,
                  message: "操作成功",
                  type: "success"
                });
              } else {
                this.$message.error(res.error);
              }
            })
            .catch(err => {
              let alert = err.message ? err.message : err;
              this.$message.error(alert);
            });
        } else {
          this.$message.warning("请填写准确信息！");
          return false;
        }
      });
    },
    onNewProduct(newForm){
      this.$refs[newForm].validate(valid => {
      if (valid) {
          let fe = JSON.stringify(this.editTags);
          this.newProduct.feature = fe;
          this.newProduct.storeCode=this.userInfo.storeCode;
          this.editTags=[];
console.log(this.newProduct);
          this.SaveProduct(this.newProduct)
            .then(res => {
              if (res.resultStatus == 1) {
                          let params = { storeCode: this.userInfo.storeCode };
                this.GetProductList(params);
                this.newProductFormVisible = false;
                this.$message({
                  showClose: true,
                  message: "操作成功",
                  type: "success"
                });
              } else {
                this.$message.error(res.error);
              }
            })
            .catch(err => {
              let alert = err.message ? err.message : err;
              this.$message.error(alert);
            });
      }
      else{
        this.$message.warning("请填写准确信息！");
          return false
      }
      });
    },
    handleEditCategory(){
      this.editCategoryVisible=true;
    },
    addCategory(){
      this.category.storeCode=this.userInfo.storeCode;
      this.category.name=this.searchCategory.toUpperCase();
      this.SaveCategory(this.category)
        .then(res=>{
          if(res.resultStatus==1){
            this.PushCategoryList(res.data);

            this.searchCategory="",
            this.$message({
              showClose:true,
              message:"操作成功",
              type:"success",
            });
          }
          else{
            this.$message.error(res.error);
          }
        })
        .catch(err=>{
          let alert=err.message?err.message:err;
          this.$message.error(alert);
        });
    },
    searchCate(){
      this.categoryQuery.page=1;
    },
    pageChangeCate(page){
      this.categoryQuery.page=page;
    },
    categroyEdit(index,row){
      if (this.CheckUnSaveCategroy()) {
        this.$message.warning("有未保存项，请先保存");
        return;
      }
      row.isSet = !row.isSet;
    },
    CheckUnSaveCategroy() {
      return (
        this.editCategoryList.filter(item => {
          return item.isSet;
        }).length > 0
      );
    },
    CheckDuplicate(payrod){
      return(
        this.calCategoryList.filter(item=>{
          let key=item.name;
          let index=key.toUpperCase().indexOf(payrod.toUpperCase());
          return index != -1;
        }).length>1
      )
    },
    categroySave(index,row){
      if(this.CheckDuplicate(row.name)){
        this.$message.warning("有重复数据");
        return;
      }
      else
      {
        row.isSet = !row.isSet;
        this.category.storeCode=this.userInfo.storeCode;
        this.category.id=row.id;
        this.category.name=row.name;
        this.category.comment=row.comment;
        this.category.categoryId=row.categoryId;
        this.SaveCategory(this.category)
          .then(res=>{
            if(res.resultStatus==1){
              // this.GetCategoryList(this.userInfo.storeCode);
  // console.log(this.categoryList);
              this.searchCategory="",
              this.$message({
                showClose:true,
                message:"操作成功",
                type:"success",
              });
            }
            else{
              this.$message.error(res.error);
            }
          })
          .catch(err=>{
            let alert=err.message?err.message:err;
            this.$message.error(alert);
          });
      }
    },
    categroyDelete(index,row){
      // let cate={id:row.id}
      this.$messageBox.confirm('确认删除？',"提示",{
          confirmButtonText:'确定',
          cancelButtonText:'取消',
          type:'warning'
        })
          .then(()=>{
            this.DeleteCategory(row);
          })
    },
    CategoryChange(value){
console.log("value:_"+value)
      this.editProduct.categoryId=value;
    },
    handleEditTagsClose(tag) {
      this.editTags.splice(this.editTags.indexOf(tag), 1);
    },

    showEditInput() {
      this.inputEditVisible = true;
      // eslint-disable-next-line no-unused-vars
      this.$nextTick(_ => {
        this.$refs.saveEditTagInput.$refs.input.focus();
      });
    },

    handleEditInputConfirm() {
      let value = this.inputEditValue;
      if (value) {
        if (!(this.editTags.indexOf(value) > -1)) {
          if (this.editTags.length < 5) {
            this.editTags.push(value);
          } else {
            this.$message.warning("标签不能多于5条！");
          }
        }
      }
      this.inputEditVisible = false;
      this.inputEditValue = "";
    },
    cateClose(){
      this.searchCategory="";
    },
  },
  computed: {
    ...mapGetters(["userInfo", "productList","categoryList"]),
    productLishPageShow() {
      return this.productList.filter(item => {
        let key =
          item.name +
          item.type +
          item.specification +
          item.unit +
          item.categoryName;
        if (item.feature) {
          key = key + item.feature;
        } else {
          key = key + "";
        }
        let index = key.toUpperCase().indexOf(this.searchContent.toUpperCase());
        return index != -1;
      });
    },
    list() {
      return this.productLishPageShow.slice(
        (this.listQuery.page - 1) * this.listQuery.limit,
        this.listQuery.page * this.listQuery.limit
      );
    },
    calCategoryList(){
      return this.categoryList;
    },
    editCategoryList(){  
      return this.categoryList.filter(item=>{
        let key=item.name;
        let index=key.toUpperCase().indexOf(this.searchCategory.toUpperCase());
        return index != -1;
      })
    },
    showCategoryList(){
      return this.editCategoryList.slice((this.categoryQuery.page - 1) * this.categoryQuery.limit, this.categoryQuery.page * this.categoryQuery.limit);
    },
  },
  beforeMount() {
    let params = { storeCode: this.userInfo.storeCode };
    this.GetProductList(params);
    // console.log(this.productList);
    // this.GetCategoryList(params);
  }
};
</script>
<style>
.title-div {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 80;
}
.title-div-left {
  display: flex;
  align-items: center;
}
.el-tag + .el-tag {
  margin-left: 10px;
}
.button-new-tag {
  margin-left: 10px;
  height: 45px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 250px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>
