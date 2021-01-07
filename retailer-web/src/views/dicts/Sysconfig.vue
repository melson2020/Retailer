<template>
  <div class="sysconifg-container">
    <div class="sysconifg-title">系统配置</div>
    <div class="sysconifg-content-area">
      <!-- <div class="sysconifg-box-item">
        <div class="sysconifg-box-div">
          <span class="sysconifg-char-nolmal">分享链接:</span>
          <label class="sysconfig-margin-left sysconifg-char-nolmal"
            >https://www.melson.top</label
          >
        </div>
        <div class="sysconifg-box-div">
          <el-select v-model="value" placeholder="选择有效时长" size="small">
          </el-select>
          <el-button type="primary" class="sysconfig-margin-left" size="small"
            >创建链接</el-button
          >
          <span class="sysconfig-margin-left sysconfig-char-sub"
            >有效期至：</span
          >
        </div> -->

      <!-- </div> -->
      <el-input placeholder="请创建链接" v-model="sharePath" readonly>
        <el-button
          slot="prepend"
          v-clipboard:copy="sharePath"
          v-clipboard:success="onCopy"
          v-clipboard:error="onError"
          >分享链接：</el-button
        >
        <el-button slot="append" @click="createSharePath">创建链接</el-button>
      </el-input>
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
    ...mapGetters(["userInfo", "systemSharePath"]),
    sharePath() {
      return this.systemSharePath ? this.systemSharePath.sharePath : "";
    },
  },
  methods: {
    ...mapActions({
      CreateSharePath: "CreateSharePath",
      FindSharePath: "FindSharePath",
    }),
    createSharePath() {
      var params = { storeCode: this.userInfo.storeCode };
      this.CreateSharePath(params);
    },
    onCopy: function () {
      this.$message.success("已成功复制至剪贴板");
    },
    onError() {
      this.$message.error("复制失败");
    },
  },
  beforeMount() {
    var params = { storeCode: this.userInfo.storeCode };
    this.FindSharePath(params);
  },
};
</script>
<style>
.sysconifg-title {
  text-align: left;
  color: #409eff;
  font-size: 28px;
  font-weight: bold;
  padding: 20px;
}
.sysconifg-box-div {
  display: flex;
  flex-direction: row;
  padding: 10px;
  align-items: center;
}

.sysconfig-margin-left {
  margin-left: 15px;
}

.sysconifg-char-nolmal {
  color: #606266;
  font-size: medium;
}

.sysconfig-char-sub {
  color: #909399;
}
.sysconifg-content-area {
  padding: 20px;
}

.sysconifg-box-item {
  border: 1px solid lightgray;
  border-radius: 10px;
  margin: 0 20px;
  padding: 20px;
}
</style>