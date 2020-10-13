<template>
  <div>
    <el-button
      v-if="!showTicket"
      class="button-box"
      type="warning"
      icon="el-icon-plus"
      @click="createTicket"
      >新建盘点单</el-button
    >
    <div v-else>
      <el-steps :active="0" finish-status="success" align-center>
        <el-step title="创建"></el-step>
        <el-step title="预览"></el-step>
        <el-step title="导出"></el-step>
        <el-step title="导入"></el-step>
        <el-step title="完成"></el-step>
      </el-steps>
      <router-view/>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      showTicket: false
    };
  },
  methods: {
    createTicket() {
      this.showTicket = !this.showTicket;
    }
  }
};
</script>
<style>
.button-box {
  float: left;
  width: 500px;
  height: 200px;
  font-size: 30px;
  letter-spacing: 10px;
}
</style>