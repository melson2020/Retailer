<template>
  <div style="height:100%">
    <div class="customer-content-header">
      <div>
        <span class="customer-title-name">客户列表</span>
      </div>
      <div>
        <el-input
          class="customer-fliter-input"
          size="small"
          v-model="searchContent"
          placeholder="请输入内容"
          suffix-icon="el-icon-search"
          @focus="searchFocus"
        ></el-input>
        <el-button size="small" type="primary" icon="el-icon-plus" @click="resetForm('addNewcustomer')">添加</el-button>
      </div>
    </div>

    <div class="customer-content">
      <el-table :data="list" border class="customer-customertable" size="small"
          :height="tableHeight"
          :header-row-style="{height:'40px'}"
          :row-style="{height:'40px'}"
          :cell-style="{ padding: '2px', color: '#909399' }"
          :header-cell-style="{ background: '#808080', color: 'white'}">
        <el-table-column prop="name" label="客户名" align="left"></el-table-column>
        <el-table-column prop="address" label="联系地址" align="left"></el-table-column>
        <el-table-column prop="phone" label="联系电话" align="left" width="200px"></el-table-column>
        <el-table-column prop="comment" label="备注" align="left"></el-table-column>
        <el-table-column label="操作" align="center" width="100px">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.$index,scope.row)" plain circle type="primary" icon="el-icon-edit"/>
            <!-- <el-button size="mini" @click="handleDelete(scope.$index,scope.row)" plain circle type="danger" icon="el-icon-delete"/> -->
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="customer-content-footer">
      <el-pagination
        background
        :current-page="listQuery.page"
        layout="prev, pager, next, total, jumper"
        :page-size="listQuery.limit"
        :total="customerListShow.length"
        v-show="customerListShow.length>listQuery.limit"
        @current-change="pageChange"
      ></el-pagination>

    </div>

    <el-dialog
      title="新增客户"
      :visible.sync="newcustomerFormVisible"
      :close-on-click-modal="false"
      :show-close="false"
    >
      <el-form :model="newcustomer" :rules="rules" ref="addNewcustomer" label-width="120px">
        <el-form-item label="客户名" prop="name">
          <el-input v-model="newcustomer.name" autocomplete="off" style="width: 100%;"></el-input>
        </el-form-item>
        <el-form-item label="联系地址" prop="address">
          <el-input v-model="newcustomer.address" autocomplete="off" style="width: 100%;"></el-input>
        </el-form-item>

        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="newcustomer.phone" autocomplete="off" style="width: 100%;"></el-input>
        </el-form-item>

        <el-form-item label="备注信息" prop="comment">
          <el-input
            v-model="newcustomer.comment"
            autocomplete="off"
            style="width: 100%;"
            rows="3"
            type="textarea"
            maxlength="200"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="customer-dialog-footer">
        <el-button @click="newcustomerFormVisible = false" v-if="!loading">取消</el-button>
        <el-button type="primary" @click="onAddNewcustomer('addNewcustomer')" :loading="loading">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="编辑"
      :visible.sync="editcustomerFormVisible"
      :close-on-click-modal="false"
      :show-close="false"
    >
      <el-form :model="editcustomer" :rules="rules" ref="editcustomer" label-width="120px">
        <el-form-item label="客户名" prop="name">
          <el-input v-model="editcustomer.name" autocomplete="off" style="width: 100%;"></el-input>
        </el-form-item>
        <el-form-item label="联系地址" prop="address">
          <el-input v-model="editcustomer.address" autocomplete="off" style="width: 100%;"></el-input>
        </el-form-item>

        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="editcustomer.phone" autocomplete="off" style="width: 100%;"></el-input>
        </el-form-item>

        <el-form-item label="备注信息" prop="comment">
          <el-input
            v-model="editcustomer.comment"
            autocomplete="off"
            style="width: 100%;"
            rows="3"
            type="textarea"
            maxlength="200"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="customer-dialog-footer">
        <el-button @click="editcustomerFormVisible = false" v-if="!loading">取消</el-button>
        <el-button type="primary" @click="onEditcustomer('editcustomer')" :loading="loading">确定</el-button>
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
      newcustomerFormVisible: false,
      editcustomerFormVisible:false,
      state2: "",
      loading: false,
      searchContent: "",
      newcustomer: {
        id:"",
        name: "",
        address: "",
        phone: "",
        comment: "",
        storeCode: "",
      },
      editcustomer: {
        id:"",
        name: "",
        address: "",
        phone: "",
        comment: "",
        storeCode: "",
      },
      rules: {
        name: [
          { required: true, message: "请输入客户名称", trigger: "blur" },
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
    ...mapGetters(["userInfo", "customerList"]),
    customerListShow(){
      return this.customerList.filter(item=>{
        let key=
        item.name+
        item.address;
        if(item.phone){
          key=key+item.phone;
        }
        else{
          key=key+"";
        }
        if(item.comment){
          key=key+item.comment;
        }
        else{
          key=key+"";
        }
        let index = key.toUpperCase().indexOf(this.searchContent.toUpperCase());
        return index != -1;
      })
    },
    list(){
      return this.customerListShow.slice((this.listQuery.page - 1) * this.listQuery.limit, this.listQuery.page * this.listQuery.limit);
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
      GetCustomerList: "GetCustomerList",
      SaveCustomer: "SaveCustomer",
      PushCustomerList: "PushCustomerList",
      // DeleteCustomer:"DeleteCustomer",
      QueryCustomerObj:"QueryCustomerObj"
    }),
    resetForm(formName) {
      if (this.$refs[formName]) {
        this.$refs[formName].resetFields();
      }
      this.newcustomerFormVisible = true;
    },
    onAddNewcustomer(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.newcustomer.storeCode = this.userInfo.storeCode;
          this.SaveCustomer(this.newcustomer)
            .then(res => {
              if (res.resultStatus == 1) {
                this.newcustomerFormVisible = false;
                this.PushCustomerList(res.data);
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
    onEditcustomer(formName){
      this.$refs[formName].validate(valid=>{
        if(valid){
          this.SaveCustomer(this.editcustomer)
          .then(res=>{
            if(res.resultStatus==1){
              this.GetCustomerList(this.userInfo)
              this.editcustomerFormVisible=false;
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
    // handleDelete(index,row){
    //   // let cus={id:row.id}
    //     this.$messageBox.confirm('确认删除？',"提示",{
    //       confirmButtonText:'确定',
    //       cancelButtonText:'取消',
    //       type:'warning'
    //     })
    //       .then(()=>{
    //         this.Deletecustomer(row);
    //       })
    // },
    handleEdit(index,row){
      let cus={id:row.id,index:index}
      this.QueryCustomerObj(cus)
        .then(res=>{
          if (res.resultStatus == 1) {
            this.editcustomer=res.data;
            this.editcustomerFormVisible = true;
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
    //   this.GetcustomerList(this.userInfo);
      
      // this.list = customerList.slice((this.listQuery.page - 1) * this.listQuery.limit, this.listQuery.page * this.listQuery.limit);
    // }



  },
  beforeMount: function() {
    this.GetCustomerList(this.userInfo);
  },
  mounted: function() {
  this.$nextTick(function() {
    this.setpageSize();
  })
  },
};
</script>

<style>
.customer-content-header {
  height: 60px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
.customer-title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  margin-left: 20px;
}
.customer-fliter-input {
  width: 400px;
  margin: 0px 20px;
}
.customer-content{
  margin-top: 5px;
}
.customer-content-footer{
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
.customer-table {
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
