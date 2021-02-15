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
    <div
      v-if="storageOutTickets.length <= 0"
      class="goods-return-empty-data-div"
    >
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
            <div class="goods-return-card-header" slot="header">
              <div>
                <span class="goods-return-font-bold goods-return-color-title">{{
                  item.code
                }}</span>
                <span
                  class="goods-return-color-light-gary goods-return-margin-left"
                  >{{ item.date }}</span
                >
                <span
                  class="goods-return-margin-left goods-return-color-light-gary"
                  >{{ item.customerName }}</span
                >
                <span v-if="item.status > 0" class="goods-return-color-warning"
                  >（退）</span
                >
              </div>
              <el-button type="text" @click="goodsReturnOnClick(item)"
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
      <div>
        <div class="goods-return-dialog-info-area goods-return-space-betwween">
          <span class="goods-return-color-title"
            >日期：{{ GoodsReturn_outTicketForReturn.date }}</span
          >
          <span class="goods-return-color-title"
            >单号：{{ GoodsReturn_outTicketForReturn.code }}</span
          >
          <span class="goods-return-color-title"
            >人员：{{ GoodsReturn_outTicketForReturn.employeeName }}</span
          >
          <span class="goods-return-color-title"
            >类型：{{
              GoodsReturn_outTicketForReturn.type == "normal"
                ? "正常出库"
                : "零时出库"
            }}</span
          >
          <span class="goods-return-color-title"
            >客户：{{ GoodsReturn_outTicketForReturn.customerName }}</span
          >
        </div>
        <div class="goods-return-dialog-info-area">
          <div class="goods-return-color-title">描述：</div>
          <p>{{ GoodsReturn_outTicketForReturn.description }}</p>
        </div>
        <div class="goods-return-dialog-info-area goods-return-color-title">
          出库详细
        </div>
      </div>
      <el-table
        class="goods-return-detail-table"
        :header-row-style="{ height: '40px' }"
        :row-style="{ height: '40px' }"
        :cell-style="{ padding: '2px', color: '#909399' }"
        :header-cell-style="{ background: '#909399', color: 'white' }"
        :data="GoodsReturn_outTicketForReturn.details"
        size="small"
      >
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
            <span
              v-if="scope.row.returnCount > 0"
              class="goods-return-color-warning"
            >
              {{ scope.row.returnCount }}{{ scope.row.countUnit }}</span
            >
            <span v-else></span>
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

      <div class="dialog-footer-text">
        <span>退货金额:</span>
        <span class="goods-return-color-light-orange font-bold">{{
          returnBackPrice
        }}</span>
      </div>

      <span slot="footer" class="goods-return-dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="SubmitGoodsReturn">退 货</el-button>
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
            batchId:item.storageInBatchId,
            batchNo: item.storageInBatchNo,
            date: new Date().format("yyyy-MM-dd"),
            operationEmployeeName: this.userInfo.userName,
            operationEmployeeUserId:this.userInfo.userId,
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
            const h = this.$createElement;
            this.$messageBox({
              title: "保存成功，本次退款金额",
              message: h("p", null, [
                h("i", { style: "color:#e6a23c;font-size:1.3em" }, res.message),
              ]),
              showCancelButton: false,
              confirmButtonText: "确定",
            }).then(() => {
              this.dialogVisible = !this.dialogVisible;
            });
           
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
    returnBackPrice: function () {
      var price = 0;
      if (this.GoodsReturn_outTicketForReturn.details) {
        this.GoodsReturn_outTicketForReturn.details.map((item) => {
          if (item.backCount > 0) {
            price = this.NumberAdd(
              price,
              this.NumberMul(item.outPrice, item.backCount)
            );
          }
        });
      }
      return price.toFixed(2);
    },
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
  font-size: 1.2em;
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
.goods-return-color-light-gary {
  color: #909399;
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
.goods-return-result-area {
  display: flex;
  flex-direction: column;
}
.dialog-footer-text {
  padding-right: 20px;
  padding-top: 20px;
  text-align: right;
  font-size: 30px;
  align-items: center;
}
.goods-return-card-header {
  height: auto;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
.goods-return-dialog-info-area {
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 10px;
}
.goods-return-space-betwween {
  justify-content: space-between;
}
.goods-return-detail-table {
  margin: 20px 10px;
}
</style>