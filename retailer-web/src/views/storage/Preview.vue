<template>
  <div>
    <div class="header-div">
      <span class="title-name">盘点单预览</span>
      <div>
        <el-button
          type="primary"
          @click="exportToExcel"
          :disabled="previewStorageList.length <= 0"
          >导出文件</el-button
        >
        <el-button @click="prePageOnClick">返回</el-button>
      </div>
    </div>
    <div v-if="previewStorageList.length > 0" class="table-div">
      <el-table
        border
        :data="previewStorageList"
        ref="storagePreviewTable"
        :cell-style="{ color: '#909399' }"
        class="storage-table"
        :header-cell-style="{ background: '#606266', color: 'white' }"
      >
        <el-table-column
          v-for="(item, i) in tableColums"
          :key="i"
          :prop="item.field"
          :label="item.label"
          :width="item.width"
        ></el-table-column>
      </el-table>
      <div class="el-table-pagination-row">
        <el-pagination background layout="prev, pager, next"></el-pagination>
      </div>
    </div>
    <div v-else class="data-empty-info">
      暂无数据，请先创建盘点单
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
export default {
  data() {
    return {
      tableColums: [
        { field: "productName", label: "名称", width: "auto" },
        { field: "productType", label: "型号", width: "auto" },
        { field: "productSpecification", label: "规格", width: "auto" },
        { field: "count", label: "数量", width: "150px" },
        { field: "unit", label: "单位", width: "150px" }
      ]
    };
  },
  computed: {
    ...mapGetters([
      "previewStorageList",
      "currentStorageCountTicket",
      "userInfo"
    ])
  },
  methods: {
    ...mapActions({
      GetPreviewStorageList: "GetPreviewStorageList",
      ExportCountTicket: "ExportCountTicket",
      SetCurrentStorageCountTicket: "SetCurrentStorageCountTicket",
      SetActiveSteps: "SetActiveSteps",
      DownStorageCountTicketExportExcel: "DownStorageCountTicketExportExcel"
    }),
    prePageOnClick() {
      this.$router.replace({ path: "/main/storageCount/create" });
    },
    exportToExcel() {
      if (this.currentStorageCountTicket.type) {
        this.ExportCountTicket(this.currentStorageCountTicket)
          .then(res => {
            if (res.resultStatus == 1) {
              this.DownStorageCountTicketExportExcel(res.data)
                .then(response => {
                  let blob = new Blob([response]);
                  let fileName = res.data.excelExportFileName;
                  if (window.navigator.msSaveOrOpenBlob) {
                    navigator.msSaveBlob(blob, fileName);
                  } else {
                    var link = document.createElement("a");
                    link.href = window.URL.createObjectURL(blob);
                    link.download = fileName;
                    link.click();
                    //释放内存
                    window.URL.revokeObjectURL(link.href);
                  }
                  this.SetCurrentStorageCountTicket(res.data);
                  this.SetActiveSteps(3);
                  this.$router.replace({ path: "/main/storageCount/import" });
                })
                .catch(error => {
                  this.$message.error(error.message ? error.message : error);
                });
            } else {
              this.$message.error(res.message);
            }
          })
          .catch(err => {
            this.$message.error(err.message ? err.message : err);
          });
      } else {
        this.$message.warning("请先创建盘点单");
      }
    },
    getDateString() {
      let date = new Date(), //时间戳为10位需*1000，时间戳为13位的话不需乘1000
        Y = date.getFullYear() + "",
        M =
          date.getMonth() + 1 < 10
            ? "0" + (date.getMonth() + 1)
            : date.getMonth() + 1,
        D = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      return Y + "-" + M + "-" + D;
    }
  },
  mounted: function() {
    if (this.currentStorageCountTicket.productType) {
      let params = {
        storeCode: this.userInfo.storeCode,
        searchType: this.currentStorageCountTicket.productType
      };
      this.GetPreviewStorageList(params);
    }
    // let params = {
    //   storeCode: this.userInfo.storeCode,
    //   searchType: "normal"
    // };
    // this.GetPreviewStorageList(params);
  }
};
</script>
<style>
.data-empty-info {
  font-size: 30px;
  color: #e6a23c;
  font-weight: bold;
  padding-top: 100px;
  text-align: center;
}
.header-div {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.title-name {
  font-size: 30px;
  font-weight: bold;
  color: #409eff;
}
.table-div {
  padding-top: 30px;
}
</style>