<template>
  <el-row>
    <el-col :span="24">
      <div class="grid-content bg-purple">
        <div class="title-div">
          <span class="title-name">{{userInfo.loginName}}</span>
          <el-button icon="el-icon-plus">添加</el-button>
        </div>
        <el-table :data="employeeList" stripe style="width: 100%" border class="employee-table">
          <el-table-column prop="userName" label="姓名" ></el-table-column>
          <el-table-column prop="gender" label="性别" ></el-table-column>
          <el-table-column prop="phone" label="联系电话"></el-table-column>
          <el-table-column prop="permission" label="角色"></el-table-column>
          <el-table-column prop="loginName" label="账户"></el-table-column>
        </el-table>
      </div>
    </el-col>
  </el-row>
</template>
<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
export default {
  data() {
    return {
      tableData: [
        {
          date: "2016-05-02",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄"
        },
        {
          date: "2016-05-04",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1517 弄"
        },
        {
          date: "2016-05-01",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1519 弄"
        },
        {
          date: "2016-05-03",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1516 弄"
        }
      ]
    };
  },
  computed: {
    ...mapGetters(["userInfo","employeeList"])
  },
  methods: {
    ...mapActions({
      GetEmployeeList: "GetEmployeeList"
    })
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
  color:  gray
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
