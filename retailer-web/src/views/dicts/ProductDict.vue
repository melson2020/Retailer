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
        <el-button plain circle type="primary" icon="el-icon-plus" @click="resetForm('addNewSupply')"/>
      </div>
    </div>
    <el-table :data="list" border class="product-table">
      <el-table-column prop="name" label="名字" align="left"></el-table-column>
      <el-table-column prop="type" label="型号" align="center"></el-table-column>
      <el-table-column prop="specification" label="规格" align="center"></el-table-column>
      <el-table-column prop="unit" label="单位" align="center"></el-table-column>
      <el-table-column prop="feature" label="特征" align="center"></el-table-column>
      <el-table-column prop="categoryName" label="类别" align="center"></el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.$index,scope.row)" plain circle type="primary" icon="el-icon-edit"/>
          <el-button size="mini" @click="handleDelete(scope.$index,scope.row)" plain circle type="danger" icon="el-icon-delete"/>
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
          
          <el-col :span="8" prop="categoryName">
            <el-form-item label="类别" prop="categoryName">
              <el-input v-model="editProduct.categoryName" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

          <el-row>
          <el-col :span="8">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="editProduct.unit" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="8" prop="specification">
            <el-form-item label="规格" prop="specification">
              <el-input v-model="editProduct.specification" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>

        </el-row>

        <el-tag
          :key="tag"
          v-for="tag in editTags"
          closable
          :disable-transitions="false"
          size="small"
          @close="handleEditTagsClose(tag)">
          {{tag}}
        </el-tag>
        <el-input
          class="input-new-tag"
          v-if="inputEditVisible"
          v-model="inputEditValue"
          ref="saveEditTagInput"
          size="small"
          @keyup.enter.native="handleEditInputConfirm"
          @blur="handleEditInputConfirm"
        >
        </el-input>
        <el-button v-else class="button-new-tag" size="small" @click="showEditInput">+ 标签</el-button>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editProductFormVisible = false" v-if="!loading">取消</el-button>
        <el-button type="primary" @click="onEditProduct('editProduct')" :loading="loading">确定</el-button>
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
      editProductFormVisible:false,
      newProductFormVisible:false,
      loading: false,
      newProduct:{
        id:"",
        name:"",
        type:"",
        specification:"",
        unit:"",
        feature:"",
        categoryId:"",
        categoryName:"",
        categoryComment:"",
        categoryKeyId:""
      },
      listQuery:{
        page:1,
        limit:13
      },
      editProduct:{
        id:"",
        name:"",
        type:"",
        specification:"",
        unit:"",
        feature:"",
        categoryId:"",
        categoryName:"",
        categoryComment:"",
        categoryKeyId:""
      },
      editTags: [],
      inputEditVisible: false,
      inputEditValue: '',
      rules: {
        name: [
          { required: true, message: "请输入名称", trigger: "blur" },
          { min: 2, max: 55, message: "长度在 2 到 55 个字符", trigger: "blur" }
        ],
        type: [
          { required: true, message: "请输入类型", trigger: "blur" }
        ],
        categoryName:[
          { required: true, message: "请输入类别", trigger: "blur" }
        ]
      },
    };
  },
  methods: {
      ...mapActions({
      GetProductList: "GetProductList",
      DeleteProduct:"DeleteProduct",
      QueryProductObj:"QueryProductObj",
      SaveProduct:"SaveProduct",
    }),
    searchFocus() {
      this.listQuery.page=1;
    },
    pageChange(page){
      this.listQuery.page=page;
    },
    handleDelete(index,row){
      let prod={id:row.id,index:index}
      this.$messageBox.confirm('确认删除？',"提示",{confirmButtonText:'确定',cancelButtonText:'取消',type:'warning'})
      .then(()=>{
             this.DeleteProduct(prod)   
      })
    },
    handleEdit(index,row){
      let prod={id:row.id,index:index}
      this.QueryProductObj(prod)
      .then(res=>{
        if(res.resultStatus==1){
          this.editProduct=res.data;
          this.editProductFormVisible=true;
        }
        else{
          this.$message.error(res.message);
        }
      })
      .catch(err=>{
        let alert=err.message?err.message:err;
        this.$message.error(alert);
      })
    },
    onEditProduct(editForm){
      this.$refs[editForm].validate(valid=>{
        if(valid)
        {
          let fe=JSON.stringify(this.editTags);
          this.editProduct.feature=fe;
          console.log(this.editProduct);   //明天继续存储。
          this.SaveProduct(this.editProduct)
          .then(res=>{
            if(res.resultStatus==1){
              let params = { storeCode: this.userInfo.storeCode };
              this.GetProductList(params);
              this.editProductFormVisible=false;
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
        else
        {
          this.$message.warning("请填写准确信息！");
          return false;
        }
      })
    },
    handleEditTagsClose(tag) {
        this.editTags.splice(this.editTags.indexOf(tag), 1);
      },

      showEditInput() {
        this.inputEditVisible = true;
        // eslint-disable-next-line no-unused-vars
        this.$nextTick(_=> {
          this.$refs.saveEditTagInput.$refs.input.focus();
        });
      },

      handleEditInputConfirm() {
        let value = this.inputEditValue;
        if (value) {
          if(!(this.editTags.indexOf(value)>-1))
          {
            if(this.editTags.length<5)
            {
              this.editTags.push(value);
            }
            else
            {
              this.$message.warning("标签不能多于5条！")
            }
          }
        }
        this.inputEditVisible = false;
        this.inputEditValue = '';
      }
  },
  computed: {
    ...mapGetters(["userInfo", "productList"]),
    productLishPageShow(){
        return this.productList.filter(item=>{
          let key=item.name+item.type+item.specification+item.unit+item.categoryName;
          if(item.feature){
          key=key+item.feature;
          }
          else{
            key=key+"";
          }
          let index=key.toUpperCase().indexOf(this.searchContent.toUpperCase());
          return index!=-1;
        })
    },
    list(){
      return this.productLishPageShow.slice((this.listQuery.page - 1) * this.listQuery.limit, this.listQuery.page * this.listQuery.limit);
    }
  },
  beforeMount() {
    let params = { storeCode: this.userInfo.storeCode };
    this.GetProductList(params);
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
