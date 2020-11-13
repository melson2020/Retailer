<template>
  <div>
    <div class="storageout-header-div">
      <span class="storageout-title-name">出库单</span>
    </div>
    <div class="storageout-mian-content">
      <el-button
        v-if="!showTicket"
        class="storageout-create-storage-out"
        type="primary"
        icon="el-icon-plus"
        @click="direct"
        >新建出库单</el-button
      >
      <div v-else class="storageout-form-area">
        <el-card class="storageout-out-ticket-form">
          <div slot="header">
            <span class="storageout-form-title"
              >{{ userInfo.store.storeName }} 出库单</span
            >
            <el-button
              style="float: left; padding: 3px 0"
              type="text"
              @click="direct"
              >返回</el-button
            >
          </div>
          <el-form ref="outTicketForm" :rules="rules" :model="storageOutTicket">
            <el-col :span="12">
              <el-form-item class="item" label="出库单号:" prop="code">
                <span class="content-left">{{ storageOutTicket.code }}</span>
              </el-form-item>
              <el-form-item class="item" label="出库人员：" prop="employeeName">
                <span class="content-left">{{
                  storageOutTicket.employeeName
                }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="item" label="出库时间：" prop="date">
                <span class="content-left">{{ storageOutTicket.date }}</span>
              </el-form-item>
              <el-form-item class="item" label="出库类型:" prop="type">
                <el-select
                  v-model="storageOutTicket.type"
                  placeholder="请选择类型"
                  class="storageout-width-category"
                  size="mini"
                >
                  <el-option label="正常出库" value="normal"></el-option>
                  <el-option label="库存损耗" value="additional"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col>
              <el-form-item class="item" label="出库详细：" prop="details">
                <el-table border size="mini" :data="storageOutTicket.details">
                  <el-table-column
                    prop="productName"
                    label="商品名"
                    width="auto"
                  ></el-table-column>
                  <el-table-column
                    prop="storageInBatchNo"
                    label="批次号"
                    width="auto"
                  ></el-table-column>
                  <el-table-column label="是否含税" width="auto">
                    <template slot-scope="scope">
                      <el-tag v-if="scope.row.vat == 1" size="mini"
                        >税 ({{ scope.row.taxRate }}%)</el-tag
                      >
                    </template>
                  </el-table-column>
                  <el-table-column label="单价">
                    <template slot-scope="scope">
                      <span>{{ scope.row.outPrice }}￥</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="数量">
                    <template slot-scope="scope">
                      <span
                        >{{ scope.row.outCount }}{{ scope.row.countUnit }}</span
                      >
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="总价"
                    prop="totalPrice"
                  ></el-table-column>
                  <el-table-column label="操作">
                    <template slot-scope="scope">
                      <el-button
                        type="danger"
                        icon="el-icon-delete"
                        circle
                        size="mini"
                        @click="handleDelete(scope.$index, scope.row)"
                      ></el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <el-button
                  icon="el-icon-plus"
                  plain
                  class="add-detail"
                  @click="addStorageOutDetail"
                  >添加详细</el-button
                >
              </el-form-item>
            </el-col>
            <el-col>
              <el-form-item class="item" label="核算：">
                <el-table
                  border
                  size="mini"
                  :data="outTicketProfitList"
                  show-summary
                  :summary-method="getSummaries"
                >
                  <el-table-column
                    prop="productName"
                    label="商品名"
                  ></el-table-column>
                  <el-table-column
                    prop="batchNo"
                    label="批次号"
                    width="auto"
                  ></el-table-column>
                  <el-table-column
                    prop="unitPriceIn"
                    label="单价（进）"
                    width="auto"
                  ></el-table-column>
                  <el-table-column prop="vatIn" label="税（进）" width="auto">
                    <template slot-scope="scope">
                      <el-tag v-if="scope.row.vatIn == 1" size="mini">
                        税 ({{ scope.row.taxRateIn }}%)
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="discount"
                    label="回点"
                    width="auto"
                  ></el-table-column>
                  <el-table-column
                    prop="unitPriceOut"
                    label="单价（出）"
                    width="auto"
                  ></el-table-column>
                  <el-table-column label="税（出）" width="auto">
                    <template slot-scope="scope">
                      <el-tag v-if="scope.row.vatOut == 1" size="mini">
                        税 ({{ scope.row.taxRateOut }}%)
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="数量" width="auto">
                    <template slot-scope="scope">
                      {{ scope.row.outCount }}{{ scope.row.countUnit }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="profit" label="利润" width="auto">
                    <template slot-scope="scope">
                      <span class="color-orange"> {{ scope.row.profit }}</span>
                    </template></el-table-column
                  >
                </el-table>
              </el-form-item>
            </el-col>
            <el-col>
              <el-form-item>
                <el-button
                  type="primary"
                  @click="submitOutTicket('outTicketForm')"
                  >立即创建</el-button
                >
              </el-form-item>
            </el-col>
          </el-form>
        </el-card>
        <el-dialog
          title="添加出库详细"
          :visible.sync="dialogVisible"
          width="70%"
        >
          <div>
            <div class="storageout-dialog-header">
              <el-select
                class="storageout-width-input"
                size=mini
                v-model="selectPid"
                placeholder="选择商品"
                filterable
                @change="productChanegd"
              >
                <el-option
                  v-for="p in productList"
                  :key="p.id"
                  :label="p.name"
                  :value="p.id"
                ></el-option>
              </el-select>
            </div>
            <div
              v-if="storageComputedProductBtahList.length <= 0"
              class="warning-info"
            >
              <span>暂无批次数据</span>
            </div>
            <div v-else>
              <div
                v-for="pb in storageComputedProductBtahList"
                :key="pb.id"
                class="batch-info-area"
              >
                <el-col :span="7" class="ver">
                  <div class="flex-left">
                    <el-checkbox v-model="pb.checked"></el-checkbox>
                    <span class="margin-left-10"
                      >批次:{{ pb.batchNo }} ({{ pb.supplyName }})</span
                    >
                  </div>
                  <div class="margin-top-10 flex-left">
                    <span class="color-warning content-left"
                      >{{ pb.price }}￥ / {{ pb.countUnit }}</span
                    >
                    <el-tag
                      class="margin-left-10"
                      v-if="pb.vat == 1"
                      size="mini"
                      >税 ({{ pb.taxRate }}%)</el-tag
                    >
                    <el-tag
                      class="margin-left-10"
                      v-if="pb.discount > 0"
                      type="success"
                      size="mini"
                      >回点 ({{ pb.discount }}%)</el-tag
                    >
                  </div>
                </el-col>
                <el-col :span="3">
                  <span class="content-left"
                    >库存:{{ pb.count }} {{ pb.countUnit }}</span
                  >
                </el-col>
                <el-col :span="14" class="out-info">
                  <el-checkbox :checked="pb.outVat == 1" v-model="pb.outVat"
                    >是否含税</el-checkbox
                  >
                  <el-select
                    style="width:100px"
                    :disabled="!pb.outVat"
                    v-model="pb.outTaxRate"
                    filterable
                    placeholder="出货税点"
                    size="mini"
                  >
                    <el-option
                      v-for="taxRate in taxRateList"
                      :label="taxRate.description"
                      :value="taxRate.rate"
                      :key="taxRate.id"
                    ></el-option>
                  </el-select>
                  <el-input
                    class="margin-left-10"
                    :disabled="!pb.checked"
                    :placeholder="'>' + computePerSale(pb)"
                    size="mini"
                    style="width:100px"
                    v-model="pb.outPrice"
                  ></el-input>
                  <div>
                    出货量：
                    <el-input-number
                      size="mini"
                      :disabled="!pb.checked"
                      v-model="pb.outCount"
                      controls-position="right"
                      :min="0"
                      :max="pb.count"
                    ></el-input-number>
                    <span class="margin-left-10">{{ pb.countUnit }}</span>
                  </div>
                </el-col>
              </div>
              <div>
                <div class="cost-header">利润核算</div>
                <div v-for="c in checkedOutList" :key="c.id" class="cost-area">
                  <div class="cost-area-layout">
                    <div class="ver">
                      <div class="flex-left color-blue">{{ c.batchNo }}</div>
                      <div class="flex-left color-light-gray">
                        成本：( 单价{{ c.price }} - 回点{{ c.price }}*{{
                          c.discount
                        }}%)*数量{{ c.outCount }}={{ computeCost(c)
                        }}{{ c.cost }}
                      </div>
                      <div class="flex-left color-light-gray" v-if="!c.outVat">
                        出售：单价{{ c.outPrice }}*数量{{ c.outCount }}={{
                          computeSales(c)
                        }}{{ c.sales }}
                      </div>
                      <div class="flex-left color-light-gray" v-else>
                        出售：(单价{{ c.outPrice }}-税{{ c.outPrice }}*{{
                          c.outTaxRate
                        }}%)*数量{{ c.outCount }}={{ computeSales(c)
                        }}{{ c.sales }}
                      </div>
                    </div>
                    <div class="cost-area-result">
                      利润：
                      <span class="color-orange"
                        >{{ computeProfit(c) }}{{ c.profit }}</span
                      >
                    </div>
                  </div>
                </div>
                <div class="summary-area">
                  <span class="color-orange">{{ computeTotalProfit() }}</span>
                </div>
              </div>
              <div class="edit-area">
                <el-button type="primary" @click="addOutDetails()"
                  >添加</el-button
                >
              </div>
            </div>
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
export default {
  data() {
    return {
      showTicket: false,
      dialogVisible: false,
      prods: [],
      selectPid: "",
      storageOutTicket: {
        date: "",
        employeeId: "",
        employeeName: "",
        code: "",
        type: "",
        details: []
      },
      rules: {
        type: [{ required: true, message: "请选择类型", trigger: "blur" }],
        details: [{ required: true, message: "请添加商品", trigger: "blur" }]
      }
    };
  },
  computed: {
    ...mapGetters([
      "userInfo",
      "productList",
      "productBatchList",
      "taxRateList"
    ]),
    checkedOutList: function() {
      return this.productBatchList.filter(item => {
        return item.checked & (item.outCount > 0);
      });
    },
    storageComputedProductBtahList: function() {
      if (this.storageOutTicket.details.length >= 0) {
        this.storageOutTicket.details.map(item => {
          let existProductBatch = this.productBatchList.filter(p => {
            return p.batchNo == item.storageInBatchNo;
          })[0];
          if (existProductBatch) {
            existProductBatch.count = parseInt(
              this.NumberSub(existProductBatch.count, item.outCount)
            );
          }
        });
      }
      return this.productBatchList;
    },
    outTicketProfitList: function() {
      let list = [];
      this.storageOutTicket.details.map(item => {
        let l = {
          productId: item.productId,
          productName: item.productName,
          supplyId: item.supplyId,
          supplyName: item.supplyName,
          batchNo: item.storageInBatchNo,
          unitPriceIn: item.priceIn,
          vatIn: item.vatIn,
          taxRateIn: item.taxRateIn,
          discount: item.discount,
          vatOut: item.vat,
          taxRateOut: item.taxRate,
          outCount: item.outCount,
          unitPriceOut: item.outPrice,
          profit: item.profit,
          countUnit: item.countUnit
        };
        list.push(l);
      });
      return list;
    }
  },
  methods: {
    ...mapActions({
      GetProductList: "GetProductList",
      GetProductBatchList: "GetProductBatchList",
      ClearProductBatchList: "ClearProductBatchList",
      GetTaxRateList: "GetTaxRateList",
      SaveStorageOutTicket: "SaveStorageOutTicket"
    }),
    direct() {
      this.showTicket = !this.showTicket;
      if (this.showTicket) {
        this.initOutTicket();
      } else {
        this.clearOutTicket();
      }
    },
    initOutTicket() {
      let time = this.getFullTime();
      this.storageOutTicket.date = time.Y + "-" + time.M + "-" + time.D;
      this.storageOutTicket.employeeName = this.userInfo.userName;
      this.storageOutTicket.employeeId = this.userInfo.userId;
      this.storageOutTicket.code = "O" + new Date().valueOf();
    },
    clearOutTicket() {
      this.storageOutTicket.date = "";
      this.storageOutTicket.employeeName = "";
      this.storageOutTicket.employeeId = "";
      this.storageOutTicket.code = "";
      this.storageOutTicket.type = "";
      this.storageOutTicket.details = [];
    },
    addStorageOutDetail() {
      if (this.selectPid == "") {
        this.ClearProductBatchList();
      } else {
        this.productChanegd(this.selectPid);
      }
      this.dialogVisible = true;
    },
    getFullTime() {
      let date = new Date(), //时间戳为10位需*1000，时间戳为13位的话不需乘1000
        Y = date.getFullYear() + "",
        M =
          date.getMonth() + 1 < 10
            ? "0" + (date.getMonth() + 1)
            : date.getMonth() + 1,
        D = date.getDate() < 10 ? "0" + date.getDate() : date.getDate(),
        h = date.getHours() < 10 ? "0" + date.getHours() : date.getHours(),
        m =
          date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes(),
        s =
          date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
      return { Y: Y, M: M, D: D, h: h, m: m, s: s };
    },
    productChanegd(value) {
      let params = { storeCode: this.userInfo.storeCode, productId: value };
      this.GetProductBatchList(params);
    },
    //计算总成本
    computeCost(object) {
      let s = this.NumberSub(1, this.NumberDiv(object.discount, 100));
      let cost = this.NumberMul(
        object.outCount,
        this.NumberMul(object.price, s)
      );
      object.cost = cost;
      // return cost;
    },
    //计算总售价考虑税点
    computeSales(object) {
      if (object.outVat) {
        let c = this.NumberSub(1, this.NumberDiv(object.outTaxRate, 100));
        let s = this.NumberMul(object.outPrice, c);
        object.sales = this.NumberMul(s, object.outCount).toFixed(2);
      } else {
        object.sales = this.NumberMul(object.outPrice, object.outCount).toFixed(
          2
        );
      }
    },
    //计算最低出售单价
    computePerSale(object) {
      let s = this.NumberSub(1, this.NumberDiv(object.discount, 100));
      let cost = this.NumberMul(object.price, s);
      if (object.outVat) {
        let taxCost = this.NumberSub(1, this.NumberDiv(object.outTaxRate, 100));
        return this.NumberDiv(cost, taxCost).toFixed(2);
      } else {
        return cost.toFixed(2);
      }
    },
    //计算单批次利润
    computeProfit(object) {
      object.profit = this.NumberSub(object.sales, object.cost);
    },
    //计算出库详细总利润
    computeTotalProfit() {
      let profit = 0;
      this.checkedOutList.map(item => {
        if (item.profit && item.checked) {
          profit = this.NumberAdd(profit, item.profit);
        }
      });
      return profit.toFixed(2);
    },
    addOutDetails() {
      if (!this.validateOutDetails()) return;
      let pName = this.productList.filter(p => {
        return p.id == this.selectPid;
      })[0].name;
      this.checkedOutList.map(item => {
        let addItem = {
          productId: item.productId,
          productName: pName,
          storageInBatchNo: item.batchNo,
          supplyId: item.supplyId,
          supplyName: item.supplyName,
          priceIn: item.price,
          discount: item.discount,
          vat: item.outVat ? 1 : 0,
          taxRate: item.outTaxRate,
          outCount: item.outCount,
          outPrice: item.outPrice,
          storeCode: this.userInfo.storeCode,
          countUnit: item.countUnit,
          profit: item.profit,
          totalPrice: this.NumberMul(item.outCount, item.outPrice),
          vatIn: item.vat,
          taxRateIn: item.taxRate
        };
        this.addToDetailCheck(addItem);
      });
      this.dialogVisible = false;
    },
    //检验出库详细数据
    validateOutDetails() {
      if (this.checkedOutList.length <= 0) {
        this.$message.warning("请添加出库详细");
        return false;
      }
      let misPriceList = this.checkedOutList.filter(mis => {
        return mis.outPrice == "";
      });
      if (misPriceList.length > 0) {
        this.$message.warning("请填写出库单价");
        return false;
      }
      return true;
    },
    /**
     * 添加批次数据至出库详细，校验是否重复
     */
    addToDetailCheck(value) {
      let vKey = value.storageInBatchNo + value.taxRate + value.outPrice+value.supplyId;
      let existProduct = this.storageOutTicket.details.filter(p => {
        let key = p.storageInBatchNo + p.taxRate + p.outPrice+p.supplyId;
        return vKey == key;
       
      })[0];
      if (existProduct) {
        existProduct.outCount = this.NumberAdd(
          existProduct.outCount,
          value.outCount
        );
        existProduct.totalPrice = this.NumberAdd(
          existProduct.totalPrice,
          value.totalPrice
        );
        existProduct.profit = this.NumberAdd(existProduct.profit, value.profit);
      } else {
        this.storageOutTicket.details.push(value);
      }
    },
    handleDelete(index) {
      this.storageOutTicket.details.splice(index, 1);
    },
    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = "总价";
          return;
        }
        if (column.property == "profit") {
          const values = data.map(item => Number(item[column.property]));
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return this.NumberAdd(prev, curr).toFixed(2);
              } else {
                return prev;
              }
            }, 0);
          } else {
            sums[index] = "N/A";
          }
        }
      });
      return sums;
    },
    submitOutTicket(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.storageOutTicket.storeCode = this.userInfo.storeCode;
          let params = {
            outTicket: this.storageOutTicket,
            billDetailList: this.outTicketProfitList
          };
          this.SaveStorageOutTicket(params)
            .then(res => {
              console.log(res)
              if (res.resultStatus == 1) {
                this.showTicket = !this.showTicket;
                this.$message.success("保存成功");
                this.clearOutTicket();
                this.showTicket = false;
              } else {
                this.$message.error(res.message);
              }
            })
            .catch(err => {
              this.$message.error(err.message ? err.message : err);
            });
        } else {
          this.$message.warning("请填写必要信息");
          return false;
        }
      });
    }
  },
  beforeMount() {
    let params = { storeCode: this.userInfo.storeCode };
    this.GetProductList(params);
    this.GetTaxRateList();
  }
};
</script>
<style>
.storageout-header-div {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.storageout-title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
}
.storageout-create-storage-out {
  float: left;
  width: 500px;
  height: 200px;
  font-size: 28px;
  letter-spacing: 10px;
}
.storageout-mian-content {
  padding: 10px;
}
.storageout-out-ticket-form {
  min-height: 500px;
  width: 70vw;
  padding: 30px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
}
.storageout-form-area {
  display: flex;
  justify-content: center;
}
.storageout-form-title {
  height: 100px;
  color: #409eff;
  font-size: 30px;
  font-weight: bold;
}
.item /deep/.el-form-item__label {
  font-weight: bold !important;
  font-size: 28px;
  color: #606266;
  height: 80px;
}
.storageout-width-category{
  width: 200px;
  float: left;
}
.storageout-width-input{
  width: 400px;
  float: left;
}
.content-left {
  float: left;
}
.content-right {
  float: right;
}
.add-detail {
  margin-top: 10px;
  width: 100%;
  border: #dcdfe6 dashed 1px !important;
}
.batch-info-area {
  display: flex;
  justify-content: space-between;
  margin: 10px;
  padding: 10px;
  margin-top: 30px;
  align-items: flex-start;
  border-bottom: #dcdfe6 solid 1px;
}
.margin-left-10 {
  margin-left: 10px;
}
.margin-right-20 {
  margin-right: 20px;
}
.color-warning {
  color: #e6a23c;
  font-weight: bold;
}
.margin-top-10 {
  margin-top: 10px;
}
.ver {
  display: flex;
  flex-direction: column;
  justify-content: left;
}
.flex-left {
  display: flex;
  justify-content: left;
}
.warning-info {
  color: #f56c6c;
  font-size: 30px;
  margin-top: 30px;
  font-weight: bold;
}
.edit-area {
  display: flex;
  flex-direction: row-reverse;
  padding: 10px;
}
.out-info {
  display: flex;
  justify-content: space-between;
  align-content: center;
}
.storageout-dialog-header {
  display: flex;
  align-items: center;
}
.cost-area {
  border-bottom: #e4e7ed dashed 1px;
  padding: 10px;
  letter-spacing: 3px;
}
.cost-area-layout {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.color-orange {
  color: orange;
  font-weight: bold;
}
.summary-area {
  font-size: 30px;
  display: flex;
  flex-direction: row-reverse;
  padding: 10px;
  align-items: center;
}
.cost-header {
  font-size: 30px;
  color: #303133;
  padding: 10px;
  margin: 0px 30px;
}
.color-blue {
  color: #409eff;
}
.color-light-gray {
  color: #909399;
}
</style>