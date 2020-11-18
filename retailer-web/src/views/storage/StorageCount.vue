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
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="30%"
      :show-close="false"
    >
      <span>{{ unfinishedTickets.length }}个盘点单未完成</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="loadExistTicket"
          >加载至工作区</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";

export default {
  data() {
    return {
      dialogVisible: false,
      unfinishedTickets: [],
    };
  },
  computed: {
    ...mapGetters(["activeStep", "userInfo", "currentStorageCountTicket"]),
  },
  methods: {
    ...mapActions({
      SetActiveSteps: "SetActiveSteps",
      GetUnFinishedCountTickets: "GetUnFinishedCountTickets",
      SetCurrentStorageCountTicket: "SetCurrentStorageCountTicket",
    }),
    routerTo(status, type) {
      switch (status) {
        case 1:
          this.$router.push({ path: "/main/storageCount/preview" });
          this.SetActiveSteps(2);
          break;
        case 2:
          if (type) {
            this.$message.warning("为确保库存正确，请重新导入导出");
            this.$router.push({ path: "/main/storageCount/preview" });
            this.SetActiveSteps(2);
          } else {
            this.$router.push({ path: "/main/storageCount/import" });
            this.SetActiveSteps(3);
          }
          break;
        case 3:
          if (type) {
            this.$message.warning("为确保库存正确，请重新导入导出");
            this.$router.push({ path: "/main/storageCount/preview" });
            this.SetActiveSteps(2);
          } else {
            this.$router.push({ path: "/main/storageCount/import" });
            this.SetActiveSteps(3);
          }
          break;
        case 4:
          this.$router.push({ path: "/main/storageCount/updateBatch" });
          this.SetActiveSteps(4);
          break;
        case 5:
          this.$router.push({ path: "/main/storageCount/complete" });
          this.SetActiveSteps(5);
          break;
        default:
          this.$router.push({ path: "/main/storageCount/create" });
          break;
      }
    },
    loadExistTicket() {
      this.SetCurrentStorageCountTicket(this.unfinishedTickets[0]);
      this.dialogVisible = false;
      this.routerTo(this.unfinishedTickets[0].status, "fromLoad");
    },
  },
  beforeMount: function () {
    var type = this.$route.query.type;
    //从盘点记录转跳过来或者本身有未完成的ticket
    if (this.currentStorageCountTicket.status || type) {
      let status = this.currentStorageCountTicket.status;
      this.routerTo(status, type);
    } else {
      //查询数据库查询是否有未完成单子
      let params = {
        storeCode: this.userInfo.storeCode,
      };
      this.GetUnFinishedCountTickets(params)
        .then((res) => {
          if (res.resultStatus == 1) {
            var existList = res.data;
            if (existList.length > 0) {
              this.dialogVisible = true;
              this.unfinishedTickets = existList;
            }
          } else {
            this.$message.error(res.message);
          }
        })
        .catch((err) => {
          this.$message.error(err.message);
        });
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