<template>
  <div class="page">
    <el-menu default-active="/main/product/dict" mode="horizontal" router>
      <el-menu-item index="/main/product/dict">商品目录</el-menu-item>
      <el-menu-item index="/main/product/import">数据导入</el-menu-item>
    </el-menu>
    <router-view />
  </div>
</template>
<script>
</script>
<style>
.el-menu.el-menu--horizontal {
  border-bottom: none;
}
.el-menu--horizontal > .el-menu-item {
  float: left;
  height: 60px;
  line-height: 60px;
  margin: 0;
  border-bottom: 2px solid transparent;
  color: #909399;
}
.page {
  height: 1700px;
}
</style>
