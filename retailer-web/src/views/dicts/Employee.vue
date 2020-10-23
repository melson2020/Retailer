<template>

  <div style="height:100%">
    <div class="content-header">
      <div>
        <span class="title-name">{{ userInfo.store.storeName }}</span>
      </div>
      <div>
        <el-input
          class="fliter-input"
          size="small"
          v-model="searchContent"
          placeholder="请输入内容"
          suffix-icon="el-icon-search"
          @focus="searchFocus"
        ></el-input>
        <el-button
          plain circle type="primary" 
          size="small"
          icon="el-icon-plus"
          :disabled="addEmployeePermission"
          @click="resetForm('createEmployeeForm')"
        />
      </div>
    </div>

    <div class="content">
      <el-table :data="list" border class="employeetable" size="small"
          :height="tableHeight"
          :header-row-style="{height:'40px'}"
          :row-style="{height:'40px'}"
          :cell-style="{ padding: '2px', color: '#909399' }"
          :header-cell-style="{ background: '#808080', color: 'white'}">
        <el-table-column prop="userName" label="姓名"></el-table-column>
        <el-table-column prop="gender" label="性别" align="left">
          <template slot-scope="scope">
            {{
            scope.row.gender == 1
            ? "男"
            : scope.row.gender == 2
            ? "女"
            : "未知"
            }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="联系电话" align="left"></el-table-column>
        <el-table-column prop="permission" :formatter="roleFormatter" label="角色" align="left"></el-table-column>
        <el-table-column prop="loginName" label="账户" align="left"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              :disabled="scope.row.permission >= userInfo.permission&&scope.row.userId!=userInfo.userId"
              @click="handleEdit(scope.$index, scope.row)"
            plain circle type="primary" icon="el-icon-edit"/>
            <el-button
              size="mini"
              :disabled="scope.row.permission >= userInfo.permission&&scope.row.userId!=userInfo.userId"
              @click="handleDelete(scope.$index, scope.row)"
            plain circle type="danger" icon="el-icon-delete"/>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="content-footer">
      <el-pagination
        background
        :current-page="listQuery.page"
        layout="prev, pager, next, total, jumper"
        :page-size="listQuery.limit"
        :total="employeeListShow.length"
        v-show="employeeListShow.length>listQuery.limit"
        @current-change="pageChange"
      ></el-pagination>

    </div>

    <el-dialog
      title="编辑"
      :visible.sync="editFormVisible"
      :close-on-click-modal="false"
      :show-close="false"
    >
      <el-form :model="editEmployee" ref="editEmployeeForm">
        <el-form-item label="用户姓名" :label-width="formLabelWidth" prop="userName">
          <el-input v-model="editEmployee.userName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" :label-width="formLabelWidth" prop="loginName">
          <el-input v-model="editEmployee.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户角色" :label-width="formLabelWidth" prop="permission">
          <el-col :span="11">
            <el-select v-model="editEmployee.permission" placeholder="性别" style="width: 100%;">
              <el-option
                v-for="permission in permissionList"
                :label="permission.name"
                :value="permission.level"
                :key="permission.id"
                :disabled="permission.level>userInfo.permission"
              ></el-option>
            </el-select>
          </el-col>
          <el-col class="line" :span="2">性别</el-col>
          <el-col :span="11">
            <el-select v-model="editEmployee.gender" placeholder="性别" style="width: 100%;">
              <el-option label="男" :value="1"></el-option>
              <el-option label="女" :value="2"></el-option>
              <el-option label="未知" :value="0"></el-option>
            </el-select>
          </el-col>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateEmoployee()">提 交</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="新增员工"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
      :show-close="false"
    >
      <el-form :model="newEmployee" :rules="rules" ref="createEmployeeForm">
        <el-form-item label="用户姓名" :label-width="formLabelWidth" prop="userName">
          <el-col :span="11">
            <el-input v-model="newEmployee.userName" autocomplete="off" style="width: 100%;"></el-input>
          </el-col>
          <el-col class="line" :span="2">性别</el-col>
          <el-col :span="11">
            <el-select v-model="newEmployee.gender" placeholder="性别" style="width: 100%;">
              <el-option label="男" value="1"></el-option>
              <el-option label="女" value="2"></el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="联系电话" :label-width="formLabelWidth" prop="phone">
          <el-input v-model="newEmployee.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="登录账户" :label-width="formLabelWidth" prop="loginName">
          <el-input v-model="newEmployee.loginName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="登录密码" :label-width="formLabelWidth" prop="password">
          <el-input type="password" v-model="newEmployee.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" :label-width="formLabelWidth" prop="checkPass">
          <el-input type="password" v-model="newEmployee.checkPass" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false" v-if="!loading">取 消</el-button>
        <el-button
          type="primary"
          @click="onCreateEmployee('createEmployeeForm')"
          :loading="loading"
        >确 定</el-button>
      </div>
    </el-dialog>
  </div>

</template>
<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.newEmployee.checkPass !== "") {
          this.$refs.createEmployeeForm.validateField("checkPass");
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.newEmployee.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    var validateLoginName = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入登录账户"));
      } else {
        let loginName = { loginName: value };
        this.loading = true;
        this.CheckLoginName(loginName)
          .then(res => {
            if (res.data) {
              callback();
            } else {
              callback(new Error("该账户已存在"));
            }
            this.loading = false;
          })
          .catch(error => {
            this.$message.error(error.message);
            this.loading = false;
          });
      }
    };
    return {
      tableHeight: window.innerHeight  - 150,
      dialogFormVisible: false,
      editFormVisible: false,
      tableLoading:true,
      searchContent: "",
      newEmployee: {
        userName: "",
        gender: "",
        phone: "",
        permission: 0,
        loginName: "",
        password: "",
        checkPass: "",
        storeCode: "",
        userId: ""
      },
      editEmployee: {
        userId: "",
        userName: "",
        phone: "",
        permission: 0,
        gender: ""
      },
      loading: false,
      formLabelWidth: "120px",
      rules: {
        userName: [
          { required: true, message: "请输入用户名称", trigger: "blur" }
        ],
        phone: [{ required: true, message: "请输入联系电话", trigger: "blur" }],
        loginName: [{ validator: validateLoginName, trigger: "blur" }],
        password: [{ validator: validatePass, trigger: "blur" }],
        checkPass: [{ validator: validatePass2, trigger: "blur" }]
      },
      listQuery:{
        page:1,
        limit:19,
        height:""
      }
    };
  },
  computed: {
    ...mapGetters([
      "userInfo",
      "employeeList",
      "closeDialog",
      "permissionList"
    ]),
    addEmployeePermission: function() {
      return this.userInfo.permission ? !this.userInfo.permission > 1 : true;
    },
    employeeListShow(){
      return this.employeeList.filter(item=>{
        let key=item.userName+item.phone+item.loginName;
        let index=key.toUpperCase().indexOf(this.searchContent.toUpperCase());
        return index != -1;
      })
    },
    list(){
      return this.employeeListShow.slice((this.listQuery.page - 1) * this.listQuery.limit, this.listQuery.page * this.listQuery.limit);
    }
  },
  methods: {
    ...mapActions({
      GetEmployeeList: "GetEmployeeList",
      CreateEmployee: "CreateEmployee",
      AddEmployee: "AddEmployee",
      CheckLoginName: "CheckLoginName",
      GetPermissList: "GetPermissList",
      UpdateEmployee: "UpdateEmployee",
      ReplaceEmployee: "ReplaceEmployee",
      DeleteEmployee:"DeleteEmployee"
    }),
    roleFormatter(row) {
      if (this.permissionList.length <= 0) {
        return "";
      }
      let permission = row.permission;
      if (permission) {
        let permissionName = this.permissionList.find(per => {
          return per.level == permission;
        }).name;
        return permissionName;
      }
      return "";
    },
    onCreateEmployee(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.newEmployee.storeCode = this.userInfo.storeCode;
          let payload = {
            employee: this.newEmployee,
            token: this.userInfo.userId
          };
          this.CreateEmployee(payload)
            .then(res => {
              if (res.resultStatus == 1) {
                this.dialogFormVisible = false;
                this.AddEmployee(res.data);
                this.$message({
                  showClose: true,
                  message: "添加成功",
                  type: "success"
                });
              } else {
                this.$messgae.error(res.message);
              }
            })
            .catch(err => {
              let alert = err.message ? err.message : err;
              this.$messgae.error(alert);
            });
        } else {
          this.$message.warning("请填写准确信息");
          return false;
        }
      });
    },
    resetForm(formName) {
      if (this.$refs[formName]) {
        this.$refs[formName].resetFields();
      }
      this.dialogFormVisible = true;
    },
    handleEdit(index, row) {
      this.editFormVisible = true;
      let employee = {
        userId: row.userId,
        userName: row.userName,
        phone: row.phone,
        gender: row.gender,
        permission: row.permission,
        index:index
      };
      this.editEmployee = employee;
    },
    handleDelete(index, row) {
      let emp={userId:row.userId,index:index}
       this.$messageBox.confirm('确认删除？',"提示",{
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            this.DeleteEmployee(emp)
          })
          .catch(e=>e);
      },
    updateEmoployee() {
      this.UpdateEmployee(this.editEmployee)
        .then(res => {
          if (res.resultStatus == 1) {
            this.ReplaceEmployee(this.editEmployee)
            this.editFormVisible=false    
            this.$message.success("更新成功");
          } else {
            this.$message.error("更新失败:"+res.message);
          }
        })
        .catch(error => {
          let al = error.message ? error.message : error;
          this.$message.error(al);
        });
    },
    pageChange(page){
      this.listQuery.page=page;
    },
    setpageSize() {
      let rect = this.tableHeight-40;
      this.listQuery.height=rect+40;
      let pageSize = Math.floor(rect / 40);
      this.listQuery.limit=pageSize;
    },
    searchFocus(){
      this.listQuery.page=1;
    },
  },
  beforeMount: function() {
    this.GetEmployeeList(this.userInfo);
    this.GetPermissList();
  },
  mounted: function() {
  this.$nextTick(function() {
    this.setpageSize();
  })
  },
};
</script>
<style>
.content-header {
  height: 60px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
.title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  margin-left: 20px;
}
.fliter-input {
  width: 400px;
}
.content{
  margin-top: 5px;
}
.content-footer{
  margin-top: 20px;
  height: 60px;
  align-items: center;
  justify-content: space-between;
}

/* .employee-table {
  height: 80vh;
}
.title-div {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 80px;
}
.title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
}
.el-row {
  height: 100%;
}
.el-col {
  border-radius: 4px;
  height: 100%;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
  height: 100%;
} */
</style>
