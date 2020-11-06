<template>
  <div>
    <div class="count-record-content-header">
      <div>
        <span class="count-record-title-name">盘点记录</span>
      </div>
      <div></div>
    </div>
    <div>
      <el-table>
        <el-table-column
          v-for="(item, i) in tableColums"
          :key="i"
          :prop="item.field"
          :label="item.label"
          :width="item.width"
        ></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      tableColums: [
        { field: "code", label: "单号", width: "auto" },
        { field: "type", label: "盘点类型", width: "auto" },
        { field: "employeeName", label: "盘点人员", width: "auto" },
        { field: "date", label: "盘点日期", width: "auto" },
        { field: "status", label: "进度", width: "auto" },
        { field: "productType", label: "产品类型", width: "auto" },
        { field: "excelExportFileName", label: "盘点单(初始)", width: "auto" },
        { field: "excelImportFileName", label: "盘点单(完成)", width: "auto" },
        { field: "description", label: "描述", width: "auto" }
      ]
    };
  }
};
</script>
<style>
.count-record-content-header {
  height: 60px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
.count-record-title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  margin-left: 20px;
}
</style>