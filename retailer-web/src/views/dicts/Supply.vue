<template>
  <div style="height:100%">
    <div class="supply-content-header">
      <div>
        <span class="supply-title-name">供应商列表</span>
      </div>
      <div>
        <el-input
          class="supply-fliter-input"
          size="small"
          v-model="searchContent"
          placeholder="请输入内容"
          suffix-icon="el-icon-search"
          @focus="searchFocus"
        ></el-input>
        <el-button size="small" type="primary" icon="el-icon-plus" @click="resetForm('addNewSupply')">添加</el-button>
      </div>
    </div>

    <div class="supply-content">
      <el-table :data="list" border class="supply-supplytable" size="small"
          :height="tableHeight"
          :header-row-style="{height:'40px'}"
          :row-style="{height:'40px'}"
          :cell-style="{ padding: '2px', color: '#909399' }"
          :header-cell-style="{ background: '#808080', color: 'white'}">
        <el-table-column prop="name" label="供应商名" align="left"></el-table-column>
        <el-table-column prop="address" label="联系地址" align="left"></el-table-column>
        <el-table-column prop="contact" label="联系人员" align="left"></el-table-column>
        <el-table-column prop="phone" label="联系电话" align="left"></el-table-column>
        <el-table-column prop="discount" label="厂家返点" align="left"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.$index,scope.row)" plain circle type="primary" icon="el-icon-edit"/>
            <el-button size="mini" @click="handleDelete(scope.$index,scope.row)" plain circle type="danger" icon="el-icon-delete"/>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="supply-content-footer">
      <el-pagination
        background
        :current-page="listQuery.page"
        layout="prev, pager, next, total, jumper"
        :page-size="listQuery.limit"
        :total="supplyListShow.length"
        v-show="supplyListShow.length>listQuery.limit"
        @current-change="pageChange"
      ></el-pagination>

    </div>

    <el-dialog
      title="新增供应商"
      :visible.sync="newSupplyFormVisible"
      :close-on-click-modal="false"
      :show-close="false"
    >
      <el-form :model="newSupply" :rules="rules" ref="addNewSupply" label-width="120px">
        <el-form-item label="供应商名" prop="name">
          <el-input v-model="newSupply.name" autocomplete="off" style="width: 100%;"></el-input>
        </el-form-item>
        <el-form-item label="联系地址" prop="address">
          <el-input v-model="newSupply.address" autocomplete="off" style="width: 100%;"></el-input>
        </el-form-item>

        <el-row>
          <el-col :span="8">
            <el-form-item label="厂家返点" prop="discount">
              <el-input v-model="newSupply.discount" placeholder="返点:1-99" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>
            <el-col :span="8">
            <el-form-item label="联系人员" prop="contact">
              <el-input v-model="newSupply.contact" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8" prop="phone">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="newSupply.phone" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注信息" prop="comment">
          <el-input
            v-model="newSupply.comment"
            autocomplete="off"
            style="width: 100%;"
            rows="3"
            type="textarea"
            maxlength="200"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="supply-dialog-footer">
        <el-button @click="newSupplyFormVisible = false" v-if="!loading">取消</el-button>
        <el-button type="primary" @click="onAddNewSupply('addNewSupply')" :loading="loading">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="编辑"
      :visible.sync="editSupplyFormVisible"
      :close-on-click-modal="false"
      :show-close="false"
    >
      <el-form :model="editSupply" :rules="rules" ref="editSupply" label-width="120px">
        <el-form-item label="供应商名" prop="name">
          <el-input v-model="editSupply.name" autocomplete="off" style="width: 100%;"></el-input>
        </el-form-item>
        <el-form-item label="联系地址" prop="address">
          <el-input v-model="editSupply.address" autocomplete="off" style="width: 100%;"></el-input>
        </el-form-item>

        <el-row>
          <el-col :span="8">
            <el-form-item label="厂家返点" prop="discount">
              <el-input v-model="editSupply.discount" placeholder="返点：0.0001-1" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>
            <el-col :span="8">
            <el-form-item label="联系人员" prop="contact">
              <el-input v-model="editSupply.contact" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8" prop="phone">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="editSupply.phone" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注信息" prop="comment">
          <el-input
            v-model="editSupply.comment"
            autocomplete="off"
            style="width: 100%;"
            rows="3"
            type="textarea"
            maxlength="200"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="supply-dialog-footer">
        <el-button @click="editSupplyFormVisible = false" v-if="!loading">取消</el-button>
        <el-button type="primary" @click="onEditSupply('editSupply')" :loading="loading">确定</el-button>
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
      tableHeight: window.innerHeight  - 150,
      newSupplyFormVisible: false,
      editSupplyFormVisible:false,
      state2: "",
      loading: false,
      searchContent: "",
      newSupply: {
        id:"",
        name: "",
        address: "",
        contact: "",
        phone: "",
        comment: "",
        storeCode: "",
        discount:""
      },
      editSupply: {
        id:"",
        name: "",
        address: "",
        contact: "",
        phone: "",
        comment: "",
        storeCode: "",
        discount:""
      },
      rules: {
        name: [
          { required: true, message: "请输入厂商名称", trigger: "blur" },
          { min: 2, max: 55, message: "长度在 2 到 55 个字符", trigger: "blur" }
        ],
        address: [
          { required: true, message: "请输入联系地址", trigger: "blur" }
        ]
      },
      listQuery:{
        page:1,
        limit:19,
        height:""
      }
    };
  },
  computed: {
    ...mapGetters(["userInfo", "supplyList"]),
    supplyListShow(){
      return this.supplyList.filter(item=>{
        let key=
        item.name+
        item.address;
        if(item.contact){
          key=key+item.contact;
        }
        else{
          key=key+"";
        }
        if(item.phone){
          key=key+item.phone;
        }
        else{
          key=key+"";
        }
        if(item.discount){
          key=key+item.discount;
        }
        else{
          key=key+"";
        }
        let index = key.toUpperCase().indexOf(this.searchContent.toUpperCase());
        return index != -1;
      })
    },
    list(){
      return this.supplyListShow.slice((this.listQuery.page - 1) * this.listQuery.limit, this.listQuery.page * this.listQuery.limit);
    },
    // total(){
    //   return this.list.length;
    // }
  },
  // created() {
  //       this.getList();
  // },
  methods: {
    ...mapActions({
      GetSupplyList: "GetSupplyList",
      SaveSupply: "SaveSupply",
      PushSupplyList: "PushSupplyList",
      DeleteSupply:"DeleteSupply",
      QuerySupplyObj:"QuerySupplyObj"
    }),
    resetForm(formName) {
      if (this.$refs[formName]) {
        this.$refs[formName].resetFields();
      }
      this.newSupplyFormVisible = true;
    },
    onAddNewSupply(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.newSupply.storeCode = this.userInfo.storeCode;
          this.SaveSupply(this.newSupply)
            .then(res => {
              if (res.resultStatus == 1) {
                this.newSupplyFormVisible = false;
                this.PushSupplyList(res.data);
                this.$message({
                  showClose: true,
                  message: "操作成功",
                  type: "success"
                });
              } else {
                this.$message.error(res.message);
              }
            })
            .catch(err => {
              let alert = err.message ? err.message : err;
              this.$message.error(alert);
            });
        } else {
          this.$message.warning("请填写准确信息");
          return false;
        }
      });
    },
    onEditSupply(formName){
      this.$refs[formName].validate(valid=>{
        if(valid){
          this.SaveSupply(this.editSupply)
          .then(res=>{
            if(res.resultStatus==1){
              this.GetSupplyList(this.userInfo)
              this.editSupplyFormVisible=false;
              this.$message({
                showClose:true,
                message:"操作成功",
                type:"success"
              });
            }
            else{
              this.$message.error(res.message);
            }
          })
          .catch(err=>{
              let alert = err.message ? err.message : err;
              this.$message.error(alert);
          });
        }
        else{
          this.$message.warning("请填写准确信息");
          return false;
        }
      })
    },
    handleDelete(index,row){
      // let sup={id:row.id}
        this.$messageBox.confirm('确认删除？',"提示",{
          confirmButtonText:'确定',
          cancelButtonText:'取消',
          type:'warning'
        })
          .then(()=>{
            this.DeleteSupply(row);
          })
    },
    handleEdit(index,row){
      let sup={id:row.id,index:index}
      this.QuerySupplyObj(sup)
        .then(res=>{
          if (res.resultStatus == 1) {
            this.editSupply=res.data;
            this.editSupplyFormVisible = true;
          }
          else{
            this.$message.error(res.message);
          }
      })
      .catch(err=>{
            let alert = err.message ? err.message : err;
            this.$message.error(alert);
      });
    },
    pageChange(page){
      this.listQuery.page=page;
    },
    searchFocus(){
      this.listQuery.page=1;
    },
    setpageSize() {
      let rect = this.tableHeight-40;
      this.listQuery.height=rect+40;
      let pageSize = Math.floor(rect / 40);
      this.listQuery.limit=pageSize;
    },
    // getList(){
    //   this.GetSupplyList(this.userInfo);
      
      // this.list = supplyList.slice((this.listQuery.page - 1) * this.listQuery.limit, this.listQuery.page * this.listQuery.limit);
    // }



  },
  beforeMount: function() {
    this.GetSupplyList(this.userInfo);
  },
  mounted: function() {
  this.$nextTick(function() {
    this.setpageSize();
  })
  },
};
</script>

<style>
.supply-content-header {
  height: 60px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
.supply-title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  margin-left: 20px;
}
.supply-fliter-input {
  width: 400px;
  margin: 0px 20px;
}
.supply-content{
  margin-top: 5px;
}
.supply-content-footer{
  margin-top: 20px;
  height: 60px;
  align-items: center;
  justify-content: space-between;
}

/* .el-row {
  height: 100%;
}
.content-header {
  height: 80px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
.fliter-input {
  width: 400px;
  height: 80px;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
  height: 100%;
}
.title-div {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 80px;
  flex-direction: row;
}
.title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
}
.supply-table {
  height: 80vh;
}
.title-div-left {
  display: flex;
  align-items: center;
}
.table-title {
  float: left;
  font-size: 25px;
  color: #909399;
  font-weight: bold;
} */

</style>
