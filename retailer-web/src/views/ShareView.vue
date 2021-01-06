<template>
  <div>This is share view page</div>
</template>
<script>
import { mapActions } from "vuex";
export default {
  data() {
    return {};
  },

  methods: {
    ...mapActions({
      FindShareProductStorage: "FindShareProductStorage",
    }),
  },

  beforeMount() {
    let fullPath = window.location.href;
    var pathCode = fullPath.substring(fullPath.indexOf("?") + 1).split("=")[1];
    if (!pathCode) {
      this.$message.error("请输入正确的连接");
      return;
    }
    var params={shareCode:pathCode}
    this.FindShareProductStorage(params)
  },
};
</script>
<style>
</style>