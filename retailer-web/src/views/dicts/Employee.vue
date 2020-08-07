<template>
  <el-row>
    <el-col :span="24">
      <div class="grid-content bg-purple">
        <div class="title-div">
          <span class="title-name">{{ userInfo.store.storeName }}</span>
          <el-button
            icon="el-icon-plus"
            :disabled="!userInfo.permission > 1"
            @click="resetForm('createEmployeeForm')"
            >添加</el-button
          >
        </div>
        <el-table
          :data="employeeList"
          stripe
          style="width: 100%"
          border
          class="employee-table"
        >
          <el-table-column prop="userName" label="姓名"></el-table-column>
          <el-table-column prop="gender" label="性别"></el-table-column>
          <el-table-column prop="phone" label="联系电话"></el-table-column>
          <el-table-column prop="permission" label="角色"></el-table-column>
          <el-table-column prop="loginName" label="账户"></el-table-column>
        </el-table>
        <el-dialog title="新增员工" :visible.sync="dialogFormVisible">
          <el-form :model="newEmployee" :rules="rules" ref="createEmployeeForm">
            <el-form-item
              label="用户姓名"
              :label-width="formLabelWidth"
              prop="userName"
            >
              <el-col :span="11">
                <el-input
                  v-model="newEmployee.userName"
                  autocomplete="off"
                  style="width: 100%;"
                ></el-input>
              </el-col>
              <el-col class="line" :span="2">性别</el-col>
              <el-col :span="11">
                <el-select
                  v-model="newEmployee.gender"
                  placeholder="性别"
                  style="width: 100%;"
                >
                  <el-option label="男" value="1"></el-option>
                  <el-option label="女" value="2"></el-option>
                </el-select>
              </el-col>
            </el-form-item>
            <el-form-item
              label="联系电话"
              :label-width="formLabelWidth"
              prop="phone"
            >
              <el-input
                v-model="newEmployee.phone"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item
              label="登录账户"
              :label-width="formLabelWidth"
              prop="loginName"
            >
              <el-input
                v-model="newEmployee.loginName"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item
              label="登录密码"
              :label-width="formLabelWidth"
              prop="password"
            >
              <el-input
                type="password"
                v-model="newEmployee.password"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item
              label="确认密码"
              :label-width="formLabelWidth"
              prop="checkPass"
            >
              <el-input
                type="password"
                v-model="newEmployee.checkPass"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button
              type="primary"
              @click="onCreateEmployee('createEmployeeForm')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>
    </el-col>
  </el-row>
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
    return {
      dialogFormVisible: false,
      newEmployee: {
        userName: "",
        gender: "",
        phone: "",
        permission: "",
        loginName: "",
        password: "",
        checkPass: "",
        storeCode: "",
        userId: ""
      },
      formLabelWidth: "120px",
      rules: {
        userName: [
          { required: true, message: "请输入用户名称", trigger: "blur" }
        ],
        phone: [{ required: true, message: "请输入联系电话", trigger: "blur" }],
        loginName: [
          { required: true, message: "请输入登录账户", trigger: "blur" }
        ],
        password: [{ validator: validatePass, trigger: "blur" }],
        checkPass: [{ validator: validatePass2, trigger: "blur" }]
      }
    };
  },
  computed: {
    ...mapGetters(["userInfo", "employeeList"])
  },
  methods: {
    ...mapActions({
      GetEmployeeList: "GetEmployeeList",
      CreateEmployee: "CreateEmployee"
    }),
    onCreateEmployee(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.newEmployee.storeCode = this.userInfo.storeCode;
          let payload = {
            employee: this.newEmployee,
            token: this.userInfo.userId
          };
          this.CreateEmployee(payload);
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
    }
  },
  beforeMount: function() {
    this.GetEmployeeList(this.userInfo);
  }
};
</script>
<style>
.employee-table {
  margin-top: 20px;
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
}
</style>
