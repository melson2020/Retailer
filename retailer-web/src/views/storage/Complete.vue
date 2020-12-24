<template>
  <div class="complete-success">
    <el-button
      type="success"
      icon="el-icon-link"
      plain
      @click="loadNewTicketOnClick()"
      >盘点完成!</el-button
    >
  </div>
</template>
<script>
import { mapActions } from "vuex";
export default {
  methods: {
        ...mapActions({
      RefreshTicket: "RefreshTicket"
    }),
    loadNewTicketOnClick() {
      this.RefreshTicket();
      this.$router.push({path: "/main/storageCount/create"});
      // window.location.reload("/main/storageCount/create" );
      // this.$router.replace({ path: "/main/storageCount/create" });
      // this.$router.push({ path: "/main/productStorage" });
    },
  },
};
</script>
<style>
.complete-success {
  text-align: center;
  padding: 100px;
  font-size: 50px;
  color: #67c23a;
  font-weight: bold;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  margin: 100px;
  border: 0.5px solid #E4E7ED;
}
</style>