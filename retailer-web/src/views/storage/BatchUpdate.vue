<template>
  <div>
    <div class="header-div">
      <span class="title-name">完善批次信息</span>
      <div>
        <el-button type="primary" @click="submit">提交</el-button>
        <el-button>返回</el-button>
      </div>
    </div>
    <div class="batch-update-div">
      <el-table
        :header-cell-style="{ background: '#606266', color: 'white' }"
        :data="updateBatchListShow"
      >
        <el-table-column prop="productName" label="产品名" width="auto">
        </el-table-column>
        <el-table-column prop="batchNo" label="批次号" width="auto">
        </el-table-column>
        <el-table-column prop="batchType" label="创建类型" width="auto">
          <template slot-scope="scope">
            <span v-if="scope.row.batchType === 'C'">盘点创建</span>
            <span v-else>入库</span>
          </template>
        </el-table-column>
        <el-table-column label="数量" width="auto">
          <template slot-scope="scope">
            {{ scope.row.count }}{{ scope.row.countUnit }}
          </template>
        </el-table-column>
        <el-table-column prop="vat" label="税" width="auto">
          <template slot-scope="scope">
            <el-checkbox
              v-model="scope.row.vat"
              :checked="scope.row.vat === 1"
              :true-label="1"
              :false-label="0"
              >是否含税</el-checkbox
            >
          </template>
        </el-table-column>
        <el-table-column prop="taxRate" label="税率" width="auto">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.taxRate"
              placeholder="请选择"
              :disabled="scope.row.vat !== 1"
            >
              <el-option
                v-for="taxRate in taxRateList"
                :label="taxRate.description"
                :value="taxRate.rate"
                :key="taxRate.id"
              ></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="supplyId" label="供货商" width="auto">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.supplyId"
              filterable
              placeholder="请选择供应商"
            >
              <el-option
                v-for="item in supplyList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="totalPrice" label="总价" width="auto">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.totalPrice"
              placeholder="请输入总价"
            ></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="dataCorrect" label="数据是否完整" width="150px">
          <template slot-scope="scope">
            <i
              v-if="scope.row.dataCorrect"
              class="el-icon-check icon-green"
            ></i>
            <i v-else class="el-icon-close icon-red"></i>
          </template>
        </el-table-column>
      </el-table>
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
    ...mapGetters([
      "userInfo",
      "currentStorageCountTicket",
      "updateBatchList",
      "taxRateList",
      "supplyList"
    ]),
    updateBatchListShow() {
      return this.updateBatchList.filter(item => {
        if (item.supplyId && item.supplyId !== "") {
          this.supplyList.map(supply => {
            if (supply.id == item.supplyId) {
              item.supplyName = supply.name;
              item.discount = supply.discount;
            }
          });
        }
        if (item.totalPrice && item.totalPrice !== "") {
          item.price = this.NumberDiv(item.totalPrice, item.count).toFixed(2);
        }
        var dataCorrect =
          item.supplyId !== null &&
          item.supplyId !== "" &&
          item.supplyName !== null &&
          item.supplyName !== "" &&
          item.price !== null &&
          item.price > 0 &&
          item.discount !== null &&
          item.discount !== "" &&
          item.totalPrice !== null &&
          item.totalPrice !== "" &&
          ((item.vat === 1 && item.taxRate !== null && item.taxRate !== "") ||
            item.vat !== 1);
        item.dataCorrect = dataCorrect;
        return item != null;
      });
    }
  },
  methods: {
    ...mapActions({
      GetTaxRateList: "GetTaxRateList",
      GetSupplyList: "GetSupplyList",
      GetBatchListForUpdate: "GetBatchListForUpdate",
      UpdateProductBatchList: "UpdateProductBatchList",
      UpdateCountTicket: "UpdateCountTicket",
      SetActiveSteps: "SetActiveSteps",
      SetTicketStatus: "SetTicketStatus",
      SetCurrentStorageCountTicket: "SetCurrentStorageCountTicket"
    }),
    submit() {
      var updateList = this.updateBatchListShow.filter(item => {
        return item.dataCorrect;
      });
      if (updateList.length <= 0) {
        this.$message.warning("暂无数据可更新");
        return;
      }
      this.UpdateProductBatchList(updateList)
        .then(res => {
          if (res.resultStatus === 1) {
            if (updateList.length === this.updateBatchListShow.length) {
              this.SetTicketStatus(5);
              this.UpdateCountTicket(this.currentStorageCountTicket)
                .then(r => {
                  if (r.resultStatus == 1) {
                    this.$router.push({ path: "/main/storageCount/complete" });
                    this.SetActiveSteps(5);
                    this.SetCurrentStorageCountTicket(r.data);
                  } else {
                    this.$message.error(r.message);
                  }
                })
                .catch(e => {
                  this.$message.error(e.message);
                });
            } else {
              this.GetUpdateBatchList();
            }
          } else {
            this.$message.error(res.message);
          }
        })
        .catch(err => {
          this.$message.error(err.message);
        });
    },
    GetUpdateBatchList() {
      if (
        !this.currentStorageCountTicket ||
        !this.currentStorageCountTicket.code
      ) {
        this.$message.error("暂无盘点单，请创建或者加载");
        return
      }
      let params = {
        storeCode: this.userInfo.storeCode,
        ticketCode: this.currentStorageCountTicket.code
      };
      if (this.updateBatchList.length <= 0) {
        this.GetBatchListForUpdate(params);
      }
    }
  },
  mounted() {
    this.GetUpdateBatchList()
    if (this.taxRateList.length <= 0) {
      this.GetTaxRateList();
    }
    if (this.supplyList.length <= 0) {
      this.GetSupplyList({ storeCode: this.userInfo.storeCode });
    }
  }
};
</script>
<style>
.batch-update-div {
  padding-top: 30px;
}
.icon-green {
  color: #008000;
}
.icon-red {
  color: #ff0000;
}
</style>