<template>
<el-main>
  <el-row>
    <!-- <el-col :span="24"> -->
      <div class="grid-content bg-purple">
        <div class="title-div">
          <span class="title-name">测试</span>
          <el-button icon="el-icon-plus" @click="resetForm('addNewSupply')">添加</el-button>
        </div>
        <!-- <el-input placeholder="请输入要搜索的联系人" prefix-icon="el-icon-search" v-model="searchFile"></el-input> -->
        <el-table :data="supplyList" border class="supply-table">
          <el-table-column prop="name" label="供应商名" align="left"></el-table-column>
          <el-table-column prop="address" label="联系地址" align="center"></el-table-column>
          <el-table-column prop="contact" label="联系人员" align="center"></el-table-column>
          <el-table-column prop="phone" label="联系电话" align="center"></el-table-column>
          <el-table-column prop="discount" label="厂家折扣" align="center"></el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button size="mini" @click="handleEdit(scope.$index,scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" @click="handleDelete(scope.$index,scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>


        <!-- <pagination
              v-show="total>0"
              :total="total"
              :page.sync="listQuery.page"
              :limit.sync="listQuery.limit"
              @pagination="GetSupplyList"
          /> -->


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
                <el-form-item label="厂家折扣" prop="discount">
                  <el-input v-model="newSupply.discount" placeholder="折扣：0.0001-1" autocomplete="off" style="width: 100%;"></el-input>
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
          <div slot="footer" class="dialog-footer">
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
                <el-form-item label="厂家折扣" prop="discount">
                  <el-input v-model="editSupply.discount" placeholder="折扣：0.0001-1" autocomplete="off" style="width: 100%;"></el-input>
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
          <div slot="footer" class="dialog-footer">
            <el-button @click="editSupplyFormVisible = false" v-if="!loading">取消</el-button>
            <el-button type="primary" @click="onEditSupply('editSupply')" :loading="loading">确定</el-button>
          </div>
        </el-dialog>

      </div>
    <!-- </el-col> -->
  </el-row>
</el-main>
</template>


<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
export default {
  data() {
    return {
      newSupplyFormVisible: false,
      editSupplyFormVisible:false,
      state2: "",
      loading: false,
      // total: 0,
      // listQuery: {
      //     page: 1,
      //     limit: 10
      // },
      // searchFile:"",
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
      }
    };
  },
  created(){
      this.GetSupplyList(this.userInfo);
  },
  computed: {
    ...mapGetters(["userInfo", "supplyList"])
  },
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
      let sup={id:row.id,index:index}
        this.$messageBox.confirm('确认删除？',"提示",{
          confirmButtonText:'确定',
          cancelButtonText:'取消',
          type:'warning'
        })
          .then(()=>{
            this.DeleteSupply(sup);
          })
    },
    handleEdit(index,row){
      let sup={id:row.id,index:index}
      this.QuerySupplyObj(sup)
        .then(res=>{
          if (res.resultStatus == 1) {
            console.log(res);
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
    }
  },
  // beforeMount: function() {
  //   this.GetSupplyList(this.userInfo);
  // }
};
</script>

<style>
.el-row {
  height: 100%;
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
}
.title-name {
  font-size: 30px;
  font-weight: bold;
  color: gray;
}
.supply-table {
  margin-top: 20px;
}
</style>
