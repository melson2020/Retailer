<template>
  <el-row>
    <el-col :span="24">
      <div class="grid-content bg-purple">
        <div class="title-div">
          <span class="title-name">测试</span>
          <el-button icon="el-icon-plus" @click="resetForm('createEmployeeForm')">添加</el-button>
        </div>
        <el-table :data="supplyList" stripe style="width:100%" border class="margin-top:20px">
          <el-table-column prop="name" label="名称" align="left"></el-table-column>
          <el-table-column prop="address" label="地址" align="left"></el-table-column>
          <el-table-column prop="contact" label="联系人" align="left"></el-table-column>
          <el-table-column prop="phone" label="联系电话" align="left"></el-table-column> 
          <el-table-column label="操作" align="center">
            <template>
              <el-button>编辑</el-button>
            </template>
          </el-table-column> 
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
      restaurants: [],
      state2: ""
    };
  },
  computed:{
    ...mapGetters([
      "userInfo",
      "supplyList"
    ])
  },
  methods: {
    ...mapActions({
      GetSupplyList: "GetSupplyList"
    })
  },
  beforeMount:function() {
    this.GetSupplyList (this.userInfo);
  }
};
</script>

<style>
.el-row{
  height: 100%;
}
.el.col{
  border-radius: 4px;
  height: 100%;
}
.grid-content{
  border-radius: 4px;
  min-height: 36px;
  height: 100%;
}
.title-div{
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 80px;
}
.title-name{
  font-size: 30px;
  font-weight: bold;
  color: gray;
}
</style>
