<template>
  <div>
    <div class="storagein-header-div">
      <span class="storagein-title-name">入库单</span>
    </div>
    <div class="storagein-mian-content">
      <el-button
        v-if="!showTicket"
        class="storagein-create-storage-in"
        type="primary"
        icon="el-icon-plus"
        @click="createTicket"
      >新建入库单</el-button>

      <div v-else class="storagein-form-area">
        <el-card class="storagein-in-ticket-form">
          <div slot="header">
            <div class="storagein-form-title">
              <span>{{userInfo.store.storeName}} 入库单</span>
              <el-button
              style="float: left; padding: 3px 0"
              type="text"
              @click="createTicket"
              >返回</el-button>
            </div>
          </div>
          <el-form
            ref="ticketForm"
            :model="storageInTicket"
            label-width="120px"
            label-position="left"
            :rules="rules"
          >
            <el-col :span="12" class="storagein-big-font-size">
              <el-form-item label="日期：" class="item" prop="date">
                <span class="storagein-content-left">{{storageInTicket.date}}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="批次号:" class="item" prop="batchNo">
                <span class="storagein-content-left">{{storageInTicket.batchNo}}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="入库单号:" class="item" prop="code">
                <span class="storagein-content-left">{{storageInTicket.code}}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="操作人员：" class="item" prop="employeeName">
                <span class="storagein-content-left">{{storageInTicket.employeeName}}</span>
              </el-form-item>
            </el-col>
            <el-form-item label="入库类型：" class="item" prop="type">
              <el-select
                v-model="storageInTicket.type"
                placeholder="请选择类型"
                class="storagein-content-left storagein-width-category"
                size="mini"
              >
                <el-option label="正常入库" value="normal"></el-option>
                <el-option label="临时补货" value="additional"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="入库详细" class="item" prop="products">
              <div class="storagein-detail-action-area">
                <el-select
                  v-model="addItem.productId"
                  filterable
                  placeholder="请选择商品"
                  size="mini"
                  class="storagein-width-input storagein-content-left"
                  @change="productChange"
                >
                  <el-option
                    v-for="item in productList"
                    :key="item.id"
                    :label="item.name+' ('+item.type+')'"
                    :value="item.id"
                  ></el-option>
                </el-select>
                <el-select
                  v-model="addItem.supplyId"
                  filterable
                  placeholder="请选择供应商"
                  size="mini"
                  class="storagein-width-input storagein-content-left"
                  @change="supplyChange"
                >
                  <el-option
                    v-for="item in supplyList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
                <div>
                  <el-input-number
                    v-model="addItem.count"
                    controls-position="right"
                    :min="0"
                    label="数量"
                    size="mini"
                    class="storagein-short-input"
                  ></el-input-number>
                  <span class="storagein-add-unit">{{addItem.unit}}</span>
                </div>
                <div>
                  <el-input
                    v-model="addItem.totalPrice"
                    placeholder="总价"
                    size="mini"
                    class="storagein-short-input"
                  ></el-input>
                  <span>￥</span>
                </div>
                <el-checkbox v-model="addItem.vat">是否含税</el-checkbox>
                <el-select
                  :disabled="!addItem.vat"
                  v-model="addItem.taxRate"
                  filterable
                  placeholder="税点"
                  size="mini"
                  class="storagein-tax-input"
                >
                  <el-option
                    v-for="taxRate in taxRateList"
                    :label="taxRate.description"
                    :value="taxRate.rate"
                    :key="taxRate.id"
                  ></el-option>
                </el-select>
                <div>
                  <el-button
                    type="primary"
                    icon="el-icon-plus"
                    size="mini"
                    class="storagein-add-button"
                    @click="addItemToDetail"
                  >添加</el-button>
                </div>
              </div>
              <div class="storagein-content-right storagein-font-info">
                <span class="storagein-addDetailInfoSpan">单价：{{addItemPrice}}￥/{{addItem.unit}}</span>
                <span class="storagein-addDetailInfoSpan">回点：{{addItem.discount}}</span>
                <span class="storagein-addDetailInfoSpan">成本价：{{addItemCostPrice}}￥/{{addItem.unit}}</span>
                <span
                  class="storagein-addDetailInfoSpan"
                >去税：{{addItemTepIn}}￥/{{addItem.unit}}</span>
                <span
                  class="storagein-addDetailInfoSpan"
                  v-if="addItem.vat"
                >税金：{{addItemTaxIn}}￥/{{addItem.unit}}</span>
              </div>
              <el-table
                :data="storageInTicket.products"
                border
                size="mini"
                :header-row-style="{height:'30px'}"
                :header-cell-style="{padding:'0px',background:'#C0C4CC',color:'white'}"
                :row-style="{height:'35px'}"
                :cell-style="{padding:'0px'}"
              >
                <el-table-column prop="productName" label="商品名" width="auto"></el-table-column>
                <el-table-column prop="supplyName" label="供应商" width="auto"></el-table-column>
                <el-table-column label="是否含税" width="auto">
                  <template slot-scope="scope">
                    <el-tag v-if="scope.row.vat==1" size="mini">税 ({{scope.row.taxRate}}%)</el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="discount" label="回点" width="auto"></el-table-column>
                <el-table-column label="单价">
                  <template slot-scope="scope">
                    <span>{{scope.row.price}}￥</span>
                  </template>
                </el-table-column>
                <el-table-column label="数量">
                  <template slot-scope="scope">
                    <span>{{scope.row.count}}{{scope.row.countUnit}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="总价">
                  <template slot-scope="scope">
                    <span>{{scope.row.totalPrice}}￥</span>
                  </template>
                </el-table-column>
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
            </el-form-item>
            <el-form-item label="描述" class="item" prop="description">
              <el-input type="textarea" v-model="storageInTicket.description"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="createStorageInTicket('ticketForm')">立即创建</el-button>
              <!-- <el-button @click="cancel('ticketForm')">取消</el-button> -->
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";

export default {
  data() {
    var validateDetail = (rule, value, callback) => {
      if (value === "" && this.storageInTicket.type == "additional") {
        callback(new Error("临时入库，请填写描述"));
      } else {
        callback();
      }
    };
    return {
      storageInTicket: {
        date: "",
        code: "",
        batchNo: "",
        employeeId: "",
        employeeName: "",
        type: "",
        products: [],
        description: ""
      },
      showTicket: false,
      addItem: {
        productId: "",
        productName: "",
        supplyId: "",
        supplyName: "",
        count: 1,
        unit: "",
        vat: false,
        taxRate: "",
        discount: "",
        totalPrice: "",
        price: "",
        netIn:"",
        tepIn:"",
        taxIn:""
      },
      rules: {
        type: [{ required: true, message: "请选择类型", trigger: "blur" }],
        products: [{ required: true, message: "请添加商品", trigger: "blur" }],
        description: [{ validator: validateDetail, trigger: "blur" }]
      }
    };
  },
  computed: {
    ...mapGetters(["userInfo", "supplyList", "productList", "taxRateList"]),
    addItemPrice: function() {
      return this.NumberDiv(
        this.addItem.totalPrice,
        this.addItem.count
      ).toFixed(2);
    },
    addItemCostPrice: function() {
      let discount = this.addItem.discount
        ? this.NumberDiv(this.addItem.discount, 100)
        : 0;
      let costRate = this.NumberSub(1, discount);
      let costPrice = this.NumberMul(this.addItem.totalPrice, costRate);
      return this.NumberDiv(costPrice, this.addItem.count).toFixed(2);
    },
    addItemTepIn: function() {
      // let beforeTax = this.addItem.vat
      //   ? this.NumberDiv(this.addItem.taxRate, 100)
      //   : 0;
      // let price = this.NumberMul(this.addItem.totalPrice, beforeTax);
      // return this.NumberDiv(price, this.addItem.count).toFixed(2);
      if (this.addItem.vat){
        let tax = this.addItem.vat
          ? this.NumberDiv((this.addItem.taxRate+100), 100)
          : 0;
        let discount = this.addItem.discount
          ? this.NumberDiv(this.addItem.discount, 100)
          : 0;
        let costRate = this.NumberSub(1, discount);
        let costPrice = this.NumberMul(this.addItem.totalPrice, costRate);
        let price = this.NumberDiv(costPrice, tax);
        return this.NumberDiv(price, this.addItem.count).toFixed(2);
      }
      else{
        let discount = this.addItem.discount
          ? this.NumberDiv(this.addItem.discount, 100)
          : 0;
        let costRate = this.NumberSub(1, discount);
        let costPrice = this.NumberMul(this.addItem.totalPrice, costRate);
        return this.NumberDiv(costPrice, this.addItem.count).toFixed(2);
      }
    },
    addItemTaxIn: function() {
        let tax = this.addItem.vat
          ? this.NumberDiv((this.addItem.taxRate+100), 100)
          : 0;
        let discount = this.addItem.discount
          ? this.NumberDiv(this.addItem.discount, 100)
          : 0;
        let costRate = this.NumberSub(1, discount);
        let costPrice = this.NumberMul(this.addItem.totalPrice, costRate);
        let price = this.NumberDiv(costPrice, tax);
        let taxIn=this.NumberDiv(costPrice, this.addItem.count) - this.NumberDiv(price, this.addItem.count)
        return taxIn.toFixed(2);
    },
    storageListShow: function() {
      return this.productStorageList.filter(item => {
        let key = item.productName + item.productType;
        return key.indexOf(this.searchContent) != -1;
      });
    }
  },
  methods: {
    ...mapActions({
      GetProductList: "GetProductList",
      GetSupplyList: "GetSupplyList",
      SaveTicket: "SaveTicket",
      GetTaxRateList: "GetTaxRateList"
    }),
    createTicket() {
      this.showTicket = !this.showTicket;
      if (this.showTicket) {
        this.clearAddItem();
        this.storageInTicket.products=[];
      }
      let localTime = this.getFullTime();
      this.storageInTicket.date =
        localTime.Y + "-" + localTime.M + "-" + localTime.D;
      this.storageInTicket.batchNo =
        localTime.Y +
        localTime.M +
        localTime.D +
        localTime.h +
        localTime.m +
        localTime.s;
      this.storageInTicket.employeeName = this.userInfo.userName;
      this.storageInTicket.employeeId = this.userInfo.userId;
      this.storageInTicket.code = "I" + new Date().valueOf();
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
      //   return Y +'-'+ M +'-'+ D +' '+ h +':'+ m +':'+ s;
      return { Y: Y, M: M, D: D, h: h, m: m, s: s };
    },
    // cancel(formName) {
    //   this.$refs[formName].resetFields();
    //   this.clearAddItem();
    // },
    productChange(value) {
      let p = this.productList.filter(item => {
        return item.id == value;
      });
      if (p.length > 0) {
        this.addItem.unit = p[0].unit;
        this.addItem.productName = p[0].name;
      }
    },
    supplyChange(value) {
      this.supplyList.map(item => {
        if (item.id == value) {
          this.addItem.supplyName = item.name;
          this.addItem.discount = item.discount;
        }
      });
    },
    addItemToDetail() {
      var regPos = /^\d+(\.\d+)?$/; //非负浮点数
      //判断总价是否为数字
      if (!regPos.test(this.addItem.totalPrice)) {
        this.$message.warning("总价请填写真确数字");
        return;
      }
      if (
        this.addItem.productId == "" ||
        this.addItem.supplyId == "" ||
        (this.addItem.taxRate == "" && this.addItem.vat) ||
        this.addItem.totalPrice == ""
      ) {
        this.$message.warning("请填写必要信息");
        return;
      }
      let p = {
        productId: this.addItem.productId,
        productName: this.addItem.productName,
        supplyId: this.addItem.supplyId,
        supplyName: this.addItem.supplyName,
        count: this.addItem.count,
        countUnit: this.addItem.unit,
        discount: this.addItem.discount,
        price: parseFloat(this.addItemPrice),
        vat: this.addItem.vat ? 1 : 0,
        taxRate: this.addItem.taxRate,
        totalPrice: parseFloat(this.addItem.totalPrice),
        netIn:parseFloat(this.addItemCostPrice),
        tepIn:parseFloat(this.addItemTepIn),
        taxIn:this.addItem.vat ? parseFloat(this.addItemTaxIn): 0
      };
      let pkey =
        p.productId +
        p.supplyId +
        p.countUnit +
        p.discount +
        p.price +
        p.vat +
        p.taxRate;
      let sameItems = this.storageInTicket.products.filter(item => {
        let itemKey =
          item.productId +
          item.supplyId +
          item.countUnit +
          item.discount +
          item.price +
          item.vat +
          item.taxRate;
        return itemKey == pkey;
      });
      if (sameItems.length > 0) {
        sameItems[0].count = this.NumberAdd(sameItems[0].count, p.count);
        sameItems[0].totalPrice = this.NumberAdd(
          sameItems[0].totalPrice,
          p.totalPrice
        );
      } else {
        this.storageInTicket.products.push(p);
      }
      this.clearAddItem();
    },
    clearAddItem() {
      this.addItem.productId = "";
      this.addItem.supplyId = "";
      this.addItem.count = 1;
      this.addItem.totalPrice = "";
      this.addItem.vat = false;
      this.addItem.taxRate = "";
      this.addItem.discount = "";
      this.addItem.netIn="";
      this.addItem.tepIn="";
      this.addItem.taxIn=""
    },
    handleDelete(index) {
      this.storageInTicket.products.splice(index, 1);
    },
    createStorageInTicket(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.storageInTicket.storeCode = this.userInfo.storeCode;
          this.SaveTicket(this.storageInTicket)
            .then(res => {
              if (res.resultStatus == 1) {
                this.showTicket = !this.showTicket;
                this.$message.success("保存成功");
                this.$refs["ticketForm"].resetFields();
                this.clearAddItem();
              } else {
                this.$message.error(res.message);
              }
            })
            .catch(err => {
              this.$message.error(err.message ? err.message : err);
            });
        } else {
          this.$message.warning("请填写准确信息");
          return false;
        }
      });
    }
  },
  beforeMount: function() {
    let params = { storeCode: this.userInfo.storeCode };
    this.GetProductList(params);
    this.GetSupplyList(params);
    this.GetTaxRateList();
  }
};
</script>
<style>
.storagein-header-div {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.storagein-title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
}
.storagein-mian-content {
  padding: 10px;
}
.storagein-create-storage-in {
  float: left;
  width: 500px;
  height: 200px;
  font-size: 28px;
  letter-spacing: 10px;
}
.storagein-form-area {
  display: flex;
  justify-content: center;
}
.storagein-in-ticket-form{
  min-height: 500px;
  width: 70vw;
  padding: 30px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
}
.storagein-form-title {
  height: 100px;
  color: #409eff;
  font-size: 30px;
  font-weight: bold;
}
.storagein-content-left {
  float: left;
}
.storagein-big-font-size {
  font-size: 30px;
}
.item /deep/.el-form-item__label {
  font-weight: bold !important;
  font-size: 28px;
}
.storagein-detail-action-area {
  padding: 12px 0px;
  display: flex;
  justify-content: space-between;
}
.storagein-width-input {
  width: 400px;
}
.storagein-width-category{
  width: 200px;
}
.storagein-add-unit {
  margin-left: 10px;
  font-size: 25px;
}
.storagein-short-input {
  width: 200px;
}
.storagein-tax-input{
  width: 150px;
}
.storagein-content-right {
  float: right;
}
.storagein-font-info {
  color: #e6a23c;
  font-size: 12px;
}
.storagein-addDetailInfoSpan {
  margin-left: 40px;
}
</style>
