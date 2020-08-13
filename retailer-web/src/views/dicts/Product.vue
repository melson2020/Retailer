<template>
  <el-row>
    <div class="header"></div>
    <el-col :span="10">
      <div class="show-categroy">
        <div class="title-div">
          <span class="table-title">商品类别</span>
          <!-- <el-button type="primary" size="medium">保存修改</el-button> -->
        </div>
        <el-table border class="table-top" :data="tableData" style="width: 100%">
           <el-table-column type="index" label="#"></el-table-column>
          <el-table-column prop="date" label="日期" width="180"></el-table-column>
          <el-table-column prop="name" label="姓名" width="180"></el-table-column>
          <el-table-column prop="address" label="地址"></el-table-column>
        </el-table>
        <div class="el-table-add-row">
          <i class="el-icon-plus"></i>
        </div>
      </div>
    </el-col>
    <el-col :span="14">
      <div class="show-dict">
        <div class="title-div">
          <span class="table-title">商品目录</span>
          <!-- <el-button type="primary" size="medium">保存修改</el-button> -->
        </div>
        <el-table border class="table-top" :data="tableData" style="width: 100%">
          <el-table-column prop="date" label="日期" width="180"></el-table-column>
          <el-table-column prop="name" label="姓名" width="180"></el-table-column>
          <el-table-column prop="address" label="地址"></el-table-column>
        </el-table>
        <div class="el-table-add-row">
          <i class="el-icon-plus"></i>
        </div>
      </div>
    </el-col>
  </el-row>
</template>
<script>
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
      ],
      categroyTableColums:[]
    };
  }
};
</script>
<style>
.table-title {
  float: left;
  font-size: 30px;
  color: #909399;
  font-weight: bold;
}
.title-div {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 80px;
}
.table-top {
  margin-top: 20px;
}
.show-categroy {
  height: 100%;
  padding: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
}
.show-dict {
  height: 100%;
  padding: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
  margin-left: 30px;
}
.header {
  height: 100px;
}
.el-row {
  height: 100%;
}
.el-col {
  height: 100%;
}
.el-table-add-row {
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
  margin-top: 12px;
  height: 80px;
  border: #c0c4cc dashed 1px;
}
</style>
