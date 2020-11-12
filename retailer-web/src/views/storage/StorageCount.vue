<template>
  <div>
    <div class="step-box">
      <el-steps :active="activeStep" finish-status="success">
        <el-step title="创建"></el-step>
        <el-step title="预览"></el-step>
        <el-step title="导出"></el-step>
        <el-step title="导入"></el-step>
        <el-step title="批次"></el-step>
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
    return {};
  },
  computed: {
    ...mapGetters(["activeStep", "currentStorageCountTicket"]),
  },
  methods: {
    ...mapActions({
      SetActiveSteps: "SetActiveSteps",
    }),
  },
  beforeMount: function () {
    // let path=this.$route.path;
    // if(path==='/main/storageCount'){
    //    this.$router.push({ path: "/main/storageCount/create" });
    // }
    if (this.currentStorageCountTicket.status) {
      let status = this.currentStorageCountTicket.status;
      switch (status) {
        case 1:this.$router.push({ path: "/main/storageCount/preview" });
          break;
        case 2:this.$router.push({ path: "/main/storageCount/import" });
          break;
        case 3:this.$router.push({ path: "/main/storageCount/import" });
          break;
        case 4:this.$router.push({ path: "/main/storageCount/updateBatch" });
          break;
        case 5:this.$router.push({ path: "/main/storageCount/complete" });
          break;
        default:this.$router.push({ path: "/main/storageCount/create" });
          break;
      }
    }
  },
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
.step-container {
  padding: 50px 0px;
}
</style>