<template>
  <div>
    <div class="header">盘点单</div>
    <el-form ref="form" label-width="120px" class="create-ticket-form">
      <el-form-item label="商户名称" size="mini">
        <el-input v-model="ticket.storeName"></el-input>
      </el-form-item>
      <el-form-item label="创建时间" size="mini">
        <el-input v-model="ticket.date"></el-input>
      </el-form-item>
      <el-form-item label="盘点人员" size="mini">
        <el-input v-model="ticket.employeeName"></el-input>
      </el-form-item>
      <el-form-item label="盘点类型" size="mini">
        <el-select v-model="ticket.type" placeholder="请选择类型">
          <el-option label="月度盘点" value="monthly"></el-option>
          <el-option label="临时盘点" value="addtional"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="盘点内容" size="mini">
        <el-select v-model="ticket.productType" placeholder="请选择盘点产品">
          <el-option label="全部" value="all"></el-option>
          <el-option label="常用" value="normal"></el-option>
          <el-option label="数量大于0" value="morethanzero"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="描述" size="mini">
        <el-input
          type="textarea"
          v-model="ticket.description"
          maxlength="100"
          show-word-limit
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" @click="createOnClick">立即创建</el-button>
        <el-button  size="mini"
          @click="nextStep"
          :disabled="!this.currentStorageCountTicket.type"
          >下一页</el-button
        >
      </el-form-item>
    </el-form>
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
      ticket: {
        storeName: "",
        employeeId: "",
        employeeName: "",
        date: "",
        type: "",
        productType: "",
        description: "",
      },
      dialogVisible: false,
      unfinishedTickets: [],
    };
  },
  computed: {
    ...mapGetters(["userInfo", "currentStorageCountTicket"]),
  },
  methods: {
    ...mapActions({
      CreateStorageCountTicket: "CreateStorageCountTicket",
      SetCurrentStorageCountTicket: "SetCurrentStorageCountTicket",
      SetActiveSteps: "SetActiveSteps",
      GetUnFinishedCountTickets: "GetUnFinishedCountTickets",
    }),
    getFullTime() {
      let date = new Date(), //时间戳为10位需*1000，时间戳为13位的话不需乘1000
        Y = date.getFullYear() + "",
        M =
          date.getMonth() + 1 < 10
            ? "0" + (date.getMonth() + 1)
            : date.getMonth() + 1,
        D = date.getDate() < 10 ? "0" + date.getDate() : date.getDate(),
        h = date.getHours() < 10 ? "0" + date.getHours() : date.getHours(),
        m =
          date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes(),
        s =
          date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
      return { Y: Y, M: M, D: D, h: h, m: m, s: s };
    },
    nextStep() {
      this.$router.push({ path: "/main/storageCount/preview" });
    },
    createOnClick() {
      this.CreateStorageCountTicket(this.ticket)
        .then((res) => {
          if (res.resultStatus == 1) {
            this.SetCurrentStorageCountTicket(res.data);
            this.SetActiveSteps(1);
            this.$router.replace({ path: "/main/storageCount/preview" });
          } else {
            this.$message.error(res.message);
          }
        })
        .catch((error) => {
          this.$message.error(error.message ? error.message : error);
        });
    },
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
      this.routerTo(this.unfinishedTickets[0].status,'fromLoad');
    },
  },
  mounted: function () {
    let dateTime = this.getFullTime();
    this.ticket.date = dateTime.Y + "-" + dateTime.M + "-" + dateTime.D;
    this.ticket.storeName = this.userInfo.store.storeName;
    this.ticket.employeeId = this.userInfo.userId;
    this.ticket.employeeName = this.userInfo.userName;
    this.ticket.storeCode = this.userInfo.storeCode;
  },
  beforeMount: function () {
    //防止多次点击menu 跳过检测
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
.header {
  height: 80px;
  line-height: 80px;
  font-size: 30px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}
.create-ticket-form {
  border: #e4e7ed solid 1px;
  padding: 20px;
  border-radius: 10px;
}
</style>