<template>
  <div>
    <div class="header-div">
      <span class="title-name">盘点单预览</span>
      <div>
        <el-button
          type="primary" size="small"
          @click="exportToExcel"
          :disabled="previewStorageList.length <= 0"
          >导出文件</el-button
        >
        <!-- <el-button @click="prePageOnClick">返回</el-button> -->
      </div>
    </div>
    <div v-if="previewStorageList.length > 0" class="table-div">
      <el-table
        border
        :data="previewStorageList"
        ref="storagePreviewTable"
        size="small"
        :header-row-style="{height:'40px'}"
          :row-style="{height:'40px'}"
          :cell-style="{ padding: '2px', color: '#909399' }"
          :header-cell-style="{ background: '#808080', color: 'white'}"
        :span-method="objectSpanMethod"
      >
        <el-table-column
          v-for="(item, i) in tableColums"
          :key="i"
          :prop="item.field"
          :label="item.label"
          :width="item.width"
        ></el-table-column>
      </el-table>
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
        { field: "type", label: "型号", width: "auto" },
        { field: "specification", label: "规格", width: "auto" },
        { field: "batchNo", label: "批次", width: "auto" },
        { field: "supplyName", label: "供应商", width: "auto" },
        { field: "count", label: "现有数量", width: "auto" },
        { field: "totalCount", label: "现有总数", width: "auto" },
        { field: "countUnit", label: "单位", width: "auto" }
      ]
    };
  },
  computed: {
    ...mapGetters([
      "previewStorageList",
      "currentStorageCountTicket",
      "userInfo"
    ]),
    spanArr() {
      let spanRowArr=[];
      let pos=0;
      for (let index = 0; index < this.previewStorageList.length; index++) {
         if(index==0){
           spanRowArr.push(1);
           pos=0;
         }else{
           var pre=this.previewStorageList[index-1];
           var current=this.previewStorageList[index];
           if(pre.productId===current.productId){
               spanRowArr[pos]+=1;
               spanRowArr.push(0)
           }else{
             spanRowArr.push(1)
             pos=index;
           }
         }      
      }
      return spanRowArr;
    }
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
    },
    objectSpanMethod({rowIndex, columnIndex }) {
       if (columnIndex <= 2||columnIndex>=6) {
            const _row = this.spanArr[rowIndex];
            const _col = _row > 0 ? 1 : 0;
            return {
                  rowspan: _row,
                  colspan: _col
            }
      }

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
  padding-top: 5px;
}
.el-table__expanded-cell {
  padding-top: 0px !important;
  padding-right: 0px !important;
  padding-bottom: 0px !important;
}
.preview-inner-table {
  font-size: 16px;
}
</style>