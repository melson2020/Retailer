<template>
  <div style="height:100%">
    <div class="content-header">
          <div>
            <span class="title-name">商品目录</span>
          </div>
          <div>
            <el-input
              class="fliter-input"
              v-model="searchContent"
              placeholder="搜索名称 / 型号"
              suffix-icon="el-icon-search"
              @focus="searchFocus"
            ></el-input>
            <el-button
            plain
            circle
            type="primary"
            icon="el-icon-plus"
            @click="handleNewProduct('newProduct')"
            />
        </div>
    </div>


    <el-table :data="productList" border class="product-table" :header-row-style="{ height: '50px' }"
        :row-style="{ height: '45px' }"
        :cell-style="{ padding: '2px', color: '#909399' }"
        :header-cell-style="{ background: '#808080', color: 'white' }">
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
      this.editTags=[];
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
                this.editTags=[];
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
  }
};
</script>
<style>
.content-header {
  height: 80px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
.title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
}
.fliter-input {
  width: 400px;
  height: 80px;
}
.product-table{
  height: 80vh;
}
/* .title-div-left {
  display: flex;
  align-items: left;
}
.title-div-right{  
  align-items: right;
  justify-content: space-between;
} */
</style>
