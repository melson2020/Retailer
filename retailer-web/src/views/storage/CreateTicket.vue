<template>
  <div>
    <div class="header">
      盘点单
    </div>
    <el-form ref="form" label-width="120px" class="create-ticket-form">
      <el-form-item label="商户名称">
        <el-input v-model="ticket.storeName"></el-input>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-input v-model="ticket.date"></el-input>
      </el-form-item>
      <el-form-item label="盘点人员">
        <el-input v-model="ticket.employeeName"></el-input>
      </el-form-item>
      <el-form-item label="盘点类型">
        <el-select v-model="ticket.type" placeholder="请选择类型">
          <el-option label="月度盘点" value="monthly"></el-option>
          <el-option label="临时盘点" value="addtional"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="盘点内容">
        <el-select v-model="ticket.productType" placeholder="请选择盘点产品">
          <el-option label="全部" value="all"></el-option>
          <el-option label="常用" value="normal"></el-option>
          <el-option label="数量大于0" value="morethanzero"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="描述">
        <el-input
          type="textarea"
          v-model="ticket.description"
          maxlength="100"
          show-word-limit
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="createOnClick">立即创建</el-button>
        <el-button @click="nextStep" :disabled="!this.currentStorageCountTicket.type">下一页</el-button>
      </el-form-item>
    </el-form>
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
        productType:"",
        description: ""
      }
    };
  },
   computed: {
    ...mapGetters([
      "userInfo",
      "currentStorageCountTicket",
    ]),
  },
  methods:{
      ...mapActions({
      CreateStorageCountTicket:"CreateStorageCountTicket",
      SetCurrentStorageCountTicket:"SetCurrentStorageCountTicket",
      SetActiveSteps:"SetActiveSteps"
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
    nextStep(){
       this.$router.push({ path: "/main/storageCount/preview" });
    },
    createOnClick(){
      this.CreateStorageCountTicket(this.ticket).then(res=>{
            if(res.resultStatus==1){
              this.SetCurrentStorageCountTicket(res.data)
              this.SetActiveSteps(1)
              this.$router.replace({path:"/main/storageCount/preview"})
            }else{
              this.$message.error(res.message)
            }
      }).catch(error=>{
             this.$message.error(error.message ? error.message : error) 
      })
    }
  },
  mounted:function(){
    let dateTime=this.getFullTime();
    this.ticket.date=dateTime.Y+'-'+dateTime.M+'-'+dateTime.D
    this.ticket.storeName=this.userInfo.store.storeName
    this.ticket.employeeId=this.userInfo.userId;
    this.ticket.employeeName=this.userInfo.userName;
    this.ticket.storeCode=this.userInfo.storeCode
  }
};
</script>
<style>
.header {
  height: 80px;
  line-height: 80px;
  font-size: 30px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 20px;
}
.create-ticket-form {
  border: #e4e7ed solid 1px;
  padding: 20px;
  border-radius: 10px;
}
</style>