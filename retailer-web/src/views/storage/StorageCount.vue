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
    return {
    };
  },
  computed: {
    ...mapGetters(["activeStep", "userInfo", "currentStorageCountTicket"]),
  },
  methods: {
    ...mapActions({
      SetActiveSteps: "SetActiveSteps"
    }),
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