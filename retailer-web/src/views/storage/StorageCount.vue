<template>
  <div>
<<<<<<< HEAD
    <!-- <el-button
      v-if="!showStorageCountTicket"
=======
    <div class="header-div">
      <span class="title-name">盘点</span>
    </div>
    <el-button
      v-if="!showTicket"
>>>>>>> a7ade4f0588e9e64047034cfb6449545982a5e1f
      class="button-box"
      type="warning"
      icon="el-icon-plus"
      @click="createTicket"
      >新建盘点单</el-button
    > -->
    <div  class="step-box">
      <el-steps :active="activeStep" finish-status="success">
        <el-step title="创建"></el-step>
        <el-step title="预览"></el-step>
        <el-step title="导出"></el-step>
        <el-step title="导入"></el-step>
        <el-step title="完成"></el-step>
      </el-steps>
      <div class="step-container">
        <router-view />
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";

export default {
  data() {
    return {
     
    };
  },
  computed: {
    ...mapGetters(["activeStep"])
  },
  methods: {
    ...mapActions({
      SetActiveSteps: "SetActiveSteps",
      SetStorageCountTicket:"SetStorageCountTicket"
    }),
    createTicket() {
      this.SetStorageCountTicket(true);
      this.$router.push({ path: "/main/storageCount/create" });
    }
  },
  beforeMount: function() {
    let path=this.$route.path;
    if(path=='/main/storageCount'){
       this.$router.push({ path: "/main/storageCount/create" });
    }
  }
};
</script>
<style>
.header-div {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
}
.button-box {
  float: left;
  width: 500px;
  height: 200px;
  font-size: 30px;
  letter-spacing: 10px;
}
.count-steps {
  margin: 20px;
  padding: 20px;
}
.step-box {
  padding: 20px;
  text-align: left;
}
.step-container{
  padding: 50px 0px;
}
</style>