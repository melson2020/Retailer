<template>
  <div>
    <div class="goods-return-search-area">
      <div class="goods-return-search-box">
        <span class="goods-return-font-size-35 color-title">查询出货单</span>
        <el-divider></el-divider>
        <div class="goods-return-value-area">
          <el-input
            v-model="searchValue"
            placeholder="出库单号/客户名称"
          ></el-input>
          <el-date-picker
            v-model="searchDate"
            class="goods-return-margin-left"
            type="date"
            placeholder="选择日期"
          >
          </el-date-picker>
        </div>
        <div class="goods-return-button-area">
          <el-button @click="searchOnClick" type="primary">查询</el-button>
        </div>
      </div>
    </div>
    <div v-if="storageOutTickets.length <= 0" class="goods-return-empty-data-div">
      暂无数据，请查询
    </div>
    <div class="goods-return-result-area" v-else>
      <div class="goods-return-card-area-title">搜寻到以下出库单：</div>
      <el-row :gutter="30">
        <el-col
          v-for="item in storageOutTickets"
          :key="item.id"
          :span="8"
          class="goods-return-el-col-card"
        >
          <el-card>
            <div slot="header">
              <span class="goods-return-font-bold color-title float-left"
                >{{ item.code }}</span
              >
              <span class="goods-return-color-light-orange">{{ item.date }}</span>
              <span class="goods-return-margin-left color-light-orange">{{
                item.customerName
              }}</span>
              <el-button
                style="float: right; padding: 3px 0"
                type="text"
                @click="goodsReturnOnClick(item)"
                >退货</el-button
              >
            </div>
            <el-scrollbar class="goods-return-card-content">
              <el-table :data="item.details" size="mini" :show-header="false">
                <el-table-column
                  prop="storageInBatchNo"
                  label="批次"
                  width="auto"
                >
                </el-table-column>
                <el-table-column
                  prop="productName"
                  label="产品名称"
                  width="auto"
                >
                </el-table-column>
                <el-table-column prop="supplyName" label="供货商" width="auto">
                </el-table-column>
                <el-table-column prop="outCount" label="数量" width="auto">
                  <template slot-scope="scope">
                    {{ scope.row.outCount }}{{ scope.row.countUnit }}
                  </template>
                </el-table-column>
              </el-table>
            </el-scrollbar>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <el-dialog
      :visible.sync="dialogVisible"
      width="70%"
      :show-close="false"
      :close-on-click-modal="false"
    >
      <div slot="title" class="goods-return-dialog-title">
        <el-tag
          v-if="
            GoodsReturn_outTicketForReturn &&
            GoodsReturn_outTicketForReturn.status == 2
          "
          class="goods-return-dialog-title-tag"
          type="danger"
          >已退完</el-tag
        >
        <el-tag
          v-if="
            GoodsReturn_outTicketForReturn &&
            GoodsReturn_outTicketForReturn.status == 1
          "
          class="goods-return-dialog-title-tag"
          type="warning"
          >有退货</el-tag
        >
        退货
      </div>
      <el-table :data="GoodsReturn_outTicketForReturn.details" size="small">
        <el-table-column prop="storageInBatchNo" label="批次" width="auto">
        </el-table-column>
        <el-table-column prop="productName" label="产品名称" width="auto">
        </el-table-column>
        <el-table-column prop="supplyName" label="供货商" width="auto">
        </el-table-column>
        <el-table-column prop="outPrice" label="出库单价" width="auto">
        </el-table-column>
        <el-table-column prop="outCount" label="数量" width="auto">
          <template slot-scope="scope">
            {{ scope.row.outCount }}{{ scope.row.countUnit }}
          </template>
        </el-table-column>
        <el-table-column prop="returnCount" label="已退数量" width="auto">
          <template slot-scope="scope">
            <span class="goods-return-color-warning">
              {{ scope.row.returnCount }}{{ scope.row.countUnit }}</span
            >
          </template>
        </el-table-column>
        <el-table-column label="退货" width="auto">
          <template slot-scope="scope">
            <el-input-number
              size="mini"
              v-model="scope.row.backCount"
              controls-position="right"
              :min="0"
              :max="
                Number(NumberSub(scope.row.outCount, scope.row.returnCount))
              "
            ></el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="退货总价" width="auto">
          <template slot-scope="scope">
            <span>{{
              NumberMul(
                scope.row.backCount == null ? 0 : scope.row.backCount,
                scope.row.outPrice
              )
            }}</span>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="goods-return-dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="SubmitGoodsReturn">确 定</el-button>
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
      searchValue: "",
      searchDate: "",
      dialogVisible: false,
    };
  },
  methods: {
    ...mapActions({
      FindOutTicketsWithSearchVale: "FindOutTicketsWithSearchVale",
      FindOutTicketForGoodsReturn: "FindOutTicketForGoodsReturn",
      SaveGoodsReturnRecords: "SaveGoodsReturnRecords",
    }),
    searchOnClick() {
      if (
        this.searchValue == "" &&
        (this.searchDate == "" || this.searchDate == null)
      ) {
        this.$message.warning("请输入查询数据");
        return;
      }
      var params =
        this.searchDate == null || this.searchDate == ""
          ? {
              searchValue: this.searchValue,
              storeCode: this.userInfo.storeCode,
            }
          : {
              searchValue: this.searchValue,
              date: this.searchDate.format("yyyy-MM-dd"),
              storeCode: this.userInfo.storeCode,
            };
      this.FindOutTicketsWithSearchVale(params);
    },
    goodsReturnOnClick(ticket) {
      this.dialogVisible = !this.dialogVisible;
      console.log(ticket);
      let params = { storeCode: ticket.storeCode, ticketCode: ticket.code };
      this.FindOutTicketForGoodsReturn(params);
    },
    SubmitGoodsReturn() {
      var returnDetails = [];
      this.GoodsReturn_outTicketForReturn.details.map((item) => {
        if (item.backCount > 0) {
          var backDetail = {
            storeCode: item.storeCode,
            outTicketCode: item.outTicketCode,
            productId: item.productId,
            productName: item.productName,
            supplyId: item.supplyId,
            supplyName: item.supplyName,
            customerId: item.customerId,
            customerName: item.customerName,
            count: item.backCount,
            countUnit: item.countUnit,
            totalPrice: this.NumberMul(item.backCount, item.outPrice).toFixed(
              2
            ),
            priceUnit: item.outPrice,
            batchNo: item.storageInBatchNo,
            date: new Date().format("yyyy-MM-dd"),
            operationEmployeeName: this.userInfo.userName,
            billCode: this.GoodsReturn_outTicketForReturn.billCode,
            outDetailCode: item.code,
          };
          returnDetails.push(backDetail);
        }
      });
      if (returnDetails.length <= 0) {
        this.$message.warning("请选择退货商品");
        return;
      }
      var params = {
        records: returnDetails,
        outDetails: this.GoodsReturn_outTicketForReturn.details,
      };
      this.SaveGoodsReturnRecords(params)
        .then((res) => {
          if (res.resultStatus == 1) {
            this.$message.success("退货成功");
            this.dialogVisible = !this.dialogVisible;
          } else {
            this.$message.warning(res.message);
          }
        })
        .catch((err) => {
          this.$message.error(err.message);
        });
    },
  },
  computed: {
    ...mapGetters([
      "userInfo",
      "storageOutTickets",
      "GoodsReturn_outTicketForReturn",
    ]),
  },
};
</script>
<style>
.goods-return-search-area {
  display: flex;
  justify-content: center;
}
.goods-return-search-box {
  width: 25vw;
  border-radius: 10px;
  padding: 20px;
  background: rgb(236, 245, 255);
  border: 1px solid lightgray;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
}
.goods-return-font-size-35 {
  font-size: 1.5em;
  font-weight: bold;
}
.goods-return-color-title {
  color: #303133;
}
.goods-return-button-area {
  padding: 20px 0;
  text-align: right;
}
.goods-return-float-left {
  float: left;
}
.goods-return-value-area {
  display: flex;
  flex-direction: row;
}
.goods-return-margin-left {
  margin-left: 10px;
}
.goods-return-empty-data-div {
  padding: 50px;
  color: #909399;
}
.goods-return-el-col-card {
  padding: 20px;
}
.goods-return-title-div-goods-return {
  float: left;
}
.goods-return-color-light-orange {
  color: #e6a23c;
}
.goods-return-font-bold {
  font-weight: bold;
}
.goods-return-text-item {
  text-align: left;
  font-size: small;
}
.goods-return-clearfix:before,
.goods-return-clearfix:after {
  display: table;
  content: "";
}
.goods-return-clearfix:after {
  clear: both;
}
.goods-return-card-content {
  height: 250px;
}
.goods-return-card-content /deep/.el-scrollbar__wrap {
  overflow-x: hidden;
}
.goods-return-el-card__body {
  padding: 10px 30px !important;
}
.goods-return-dialog-title {
  font-size: x-large;
  font-weight: bold;
  color: #409eff;
  text-align: center;
}
.goods-return-dialog-title-tag {
  float: left;
  border-radius: 10px;
  font-size: medium;
  font-weight: bold;
}
.goods-return-card-area-title {
  text-align: left;
  padding: 20px;
  color: #67c23a;
  font-size: medium;
}
.goods-return-color-warning {
  color: #f56c6c;
}
.goods-return-result-area{
  display: flex;
  flex-direction: column;
}
</style>