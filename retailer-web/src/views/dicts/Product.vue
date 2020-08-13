<template>
  <el-row>
    <div class="header"></div>
    <el-col :span="10">
      <div class="show-categroy">
        <div class="title-div">
          <span class="table-title">商品类别</span>
          <!-- <el-button type="primary" size="medium">保存修改</el-button> -->
        </div>
        <el-table border class="table-top" :data="categroyList" style="width: 100%">
          <el-table-column type="index" label="#"></el-table-column>
          <el-table-column
            v-for="(v,i) in categroyTableColums"
            :prop="v.field"
            :label="v.label"
            :width="v.width"
            :key="i"
          >
            <template slot-scope="scope">
              <span v-if="scope.row.isSet">
                <el-input size="mini" placeholder="请输入内容" v-model="selectedItem[v.field]"></el-input>
              </span>
              <span v-else>{{scope.row[v.field]}}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <span
                class="el-tag el-tag--info el-tag--mini"
                style="cursor: pointer;"
                @click="pwdChange(scope.row,scope.$index,true)"
              >{{scope.row.isSet?'保存':"修改"}}</span>
              <span
                v-if="!scope.row.isSet"
                class="el-tag el-tag--danger el-tag--mini"
                style="cursor: pointer;"
              >删除</span>
              <span
                v-else
                class="el-tag el-tag--mini"
                style="cursor: pointer;"
                @click="pwdChange(scope.row,scope.$index,false)"
              >取消</span>
            </template>
          </el-table-column>
        </el-table>
        <div class="el-table-add-row">
          <i class="el-icon-plus" @click="categroyAdd"></i>
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
      tableData: [],
      selectedItem: {
        name: "",
        comment: ""
      },
      categroyList: [
        { id: 1, name: "电缆", comment: "我就随便写写", isSet: false },
        { id: 2, name: "钢丝", comment: "这是钢丝，捆人的那种", isSet: false }
      ],
      categroyTableColums: [
        { field: "name", label: "类别", width: "auto" },
        { field: "comment", label: "描述", width: "auto" }
      ]
    };
  },
  methods: {
    pwdChange(row, index, cg) {   
      if (!cg) {//取消按钮点击
        row.isSet = false;
        if(row.id==0){
          this.categroyList.splice(index,1)
        }
      } else {      
        if (row.isSet) {//保存操作
          console.log("保存")
          row.isSet=false;
          row.name=this.selectedItem.name;
          row.comment=this.selectedItem.comment
          row.id=index
          this.selectedItem.name=""
          this.selectedItem.comment=""
        } else {//修改按钮点击
          console.log("修改")
          this.selectedItem.name=row.name
          this.selectedItem.comment=row.comment
          row.isSet=true;
        }
      }
    },
    categroyAdd(){
      this.selectedItem.name=""
      this.selectedItem.comment=""
      this.categroyList.push( { id: 0, name: "", comment: "", isSet: true })
    }
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
