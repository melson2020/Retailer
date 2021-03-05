<template>
  <div>
    <div class="storageoutrecord-content-header">
      <div>
        <span class="storageoutrecord-title-name">出库记录</span>
      </div>
      <div>
        <el-popover placement="left" width="500" trigger="click">
          <div class="storageoutrecord-popover-items-area">
            <div class="message-info">*时间跨度最多30天</div>
            <div class="datepickerrow">
              <el-date-picker
                v-model="date"
                class="storageoutrecord-margin-top"
                type="daterange"
                align="right"
                size="small"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="yyyy-MM-dd"
                format="yyyy 年 MM 月 dd 日"
                :picker-options="pickerOptions"
                @focus="focusOn"
              ></el-date-picker>
              <el-checkbox
                class="storageoutrecord-margin-top"
                v-model="isTax"
                :checked="isTax === 'Y'"
                true-label="Y"
                false-label="N"
                >待开票</el-checkbox>
            </div>
            <el-input
              size="small"
              class="storageoutrecord-margin-top"
              v-model="searchValue"
              placeholder="出库单号/客户名称"
            ></el-input>
          </div>
          <el-button slot="reference" type="primary" size="small"
            >查询选项</el-button
          >
        </el-popover>


        <el-button
          :disabled="date ? false : true"
          type="primary"
          size="small"
          class="storageoutrecord-margin-left"
          icon="el-icon-search"
          @click="searchOnClick"
          >查询</el-button>
      </div>
    </div>
    <div class="content-no-shadow">
      <el-scrollbar class="content-scrollbar">
        <div
          v-for="(item, index) in storageOutRecordList"
          :key="index"
          class="record-card"
        >
          <div>
            <span class="card-area-title">{{ item.date }}</span>
          </div>
          <el-row :gutter="30">
            <el-col
              :span="6"
              v-for="ticket in item.outTickets"
              :key="ticket.id"
            >
              <el-card class="box-card-out" shadow="hover">
                <div slot="header" class="clearfix">
                  <span class="card-title"
                    >出库单号： {{ ticket.outTicket.code }}</span
                  >
                  <div>
                    <el-button
                      style="float: right; padding: 3px 0; margin-left: 10px"
                      type="text"
                      :id="ticket.outTicket.code"
                      :name="ticket.outBill.code"
                      @click.prevent.stop="detailOnClick($event)"
                    >
                      查看详细</el-button
                    >
                    <el-button
                      style="float: right; padding: 3px 0"
                      type="text"
                      @click.prevent.stop="
                        printDetail(ticket.outTicket.code, ticket)
                      "
                    >
                      打印</el-button
                    >
                  </div>
                </div>
                <div>
                  <el-row class="card-content">
                    <el-col :span="6" class="card-content-col-name"
                      >时间:</el-col
                    >
                    <el-col :span="15" class="card-content-col-content">{{
                      getFullTime(ticket.outTicket.createTime)
                    }}</el-col>
                  </el-row>
                  <el-row class="card-content">
                    <el-col :span="6" class="card-content-col-name"
                      >客户:</el-col
                    >
                    <el-col :span="15" class="card-content-col-content">{{
                      ticket.outTicket.customerName
                    }}</el-col>
                  </el-row>
                  <el-row class="card-content">
                    <el-col :span="6" class="card-content-col-name"
                      >快递号:</el-col
                    >
                    <el-col :span="15" class="card-content-col-content"
                      >{{ ticket.outTicket.deliveryCode }}
                      <el-button
                        class="type-in-button"
                        type="text"
                        size="mini"
                        @click="loadCurrentTicket(ticket.outTicket)"
                        >录入</el-button
                      >
                    </el-col>
                  </el-row>
                  <el-row class="card-content">
                    <el-col :span="6" class="card-content-col-name"
                      >发票号:</el-col
                    >
                    <el-col :span="15" class="card-content-col-content"
                      >{{ ticket.outTicket.invoiceCode }}
                      <el-button
                        class="type-in-button"
                        type="text"
                        size="mini"
                        @click="loadCurrentTicket(ticket.outTicket)"
                        >录入</el-button
                      ></el-col
                    >
                  </el-row>
                  <el-row class="card-content">
                    <el-col :span="6" class="card-content-col-name"
                      >出库类型:</el-col
                    >
                    <el-col :span="15" class="card-content-col-content">
                      <span
                        v-if="ticket.outTicket.type == 'normal'"
                        class="normal-span"
                        >正常出库</span
                      >
                      <span v-else class="addtinal-span">库存损耗</span>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="6" class="card-content-col-name"
                      >描述:</el-col
                    >
                    <el-col :span="15" class="card-content-col-content">
                      <el-popover
                        v-if="
                          ticket.outTicket.description &&
                          ticket.outTicket.description.length > 15
                        "
                        placement="top-start"
                        title="详细描述"
                        width="300"
                        trigger="hover"
                        :content="ticket.outTicket.description"
                      >
                        <el-button
                          slot="reference"
                          class="detail-button"
                          type="warning"
                          plain
                          >内容过长,点击查看</el-button
                        >
                      </el-popover>
                      <span v-else>{{ ticket.outTicket.description }}</span>
                    </el-col>
                  </el-row>
                  <el-divider></el-divider>
                  <el-row class="card-content">
                    <el-col :span="12">
                      <span class="green-span"
                        >进价：{{ ticket.outBill.totalPriceIn }}</span
                      ></el-col
                    >
                    <el-col :span="12"
                      ><span class="green-span"
                        >成本：{{ ticket.outBill.cost }}</span
                      ></el-col
                    >
                  </el-row>
                  <el-row>
                    <el-col :span="12"
                      ><span class="crimson-span"
                        >售价：{{ ticket.outBill.totalPriceOut }}</span
                      ></el-col
                    >
                    <!-- <el-col :span="8"
                      ><span class="crimson-span"
                        >去税：{{ ticket.outBill.sales }}</span
                      ></el-col
                    > -->
                    <el-col :span="12"
                      ><span class="color-orange"
                        >利润：{{ ticket.outBill.profit }}</span
                      ></el-col
                    >
                  </el-row>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-scrollbar>
      <el-dialog
        title="打印详细"
        :visible.sync="printDialogVisible"
        width="60%"
      >
        <div id="exportPdf" ref="exportPdf">
          <div class="padding-15 bold-large">
            {{ userInfo.store.storeName }}出库单
          </div>
          <div>
            <el-row>
              <el-col :span="8" class="text-left padding-10"
                >日期: {{ outTicketInfo.date }}</el-col
              >
              <el-col :span="8" class="text-left padding-10"
                >单号: {{ outTicketInfo.code }}</el-col
              >
              <el-col :span="8" class="text-left padding-10"
                >出库人员: {{ outTicketInfo.employeeName }}</el-col
              >
            </el-row>
            <el-row>
              <el-col :span="8" class="text-left padding-10"
                >创建时间：
                {{
                  new Date(outTicketInfo.createTime).format(
                    "yyyy-MM-dd hh:mm:ss"
                  )
                }}</el-col
              >
              <el-col :span="8" class="text-left padding-10"
                >打印时间：{{
                  new Date().format("yyyy-MM-dd hh:mm:ss")
                }}</el-col
              >
              <el-col :span="8" class="text-left padding-10"
                >种类数量： {{ outTicketInfo.categroyCount }}</el-col
              >
            </el-row>
          </div>
          <div class="print-detail-div">详细内容</div>
          <el-table border size="small" :data="outTicketInfo.details">
            <el-table-column prop="productName" label="产品名称">
            </el-table-column>
            <el-table-column prop="outCount" label="数量" width="auto">
            </el-table-column>
            <el-table-column prop="countUnit" label="单位" width="auto">
            </el-table-column>
            <el-table-column prop="outPrice" label="单价" width="auto">
            </el-table-column>
            <el-table-column prop="totalPrice" label="总价" width="auto">
            </el-table-column>
            <el-table-column prop="taxRate" label="税(%)" width="100px">
            </el-table-column>
          </el-table>
          <div class="signature-div">签名/盖章___________</div>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="cancelOnClick">取 消</el-button>
          <el-button type="primary" @click="printPdf">打印</el-button>
        </span>
      </el-dialog>
      <el-dialog title="填写单号" :visible.sync="CodeDialogVisible" :close-on-click-modal="false" :show-close="false" width="60%">
        <el-form
          label-position="right"
          label-width="80px"
          :model="currentTicket"
        >
          <el-form-item label="快递单号">
            <el-input v-model="currentTicket.deliveryCode"></el-input>
          </el-form-item>
          <el-form-item label="发票号">
            <el-input v-model="currentTicket.invoiceCode"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="saveCurrentTicket"
              >提 交</el-button
            >
            <el-button @click="codeUpdateCannel">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
import print from "print-js";
export default {
  data() {
    return {
      date: "",
      isTax:"N",
      timeDefaultShow: "",
      startDateMin: null,
      printDialogVisible: false,
      CodeDialogVisible: false,
      searchValue: "",
      currentTicket: {},
      pickerOptions: {
        onPick: (obj) => {
          this.startDateMin = new Date(obj.minDate).getTime();
        },
        disabledDate: (time) => {
          if (this.startDateMin) {
            let maxDate = this.startDateMin + 3600 * 1000 * 24 * 31;
            let minDate = this.startDateMin - 3600 * 1000 * 24 * 31;
            return (
              time.getTime() > maxDate ||
              time.getTime() > Date.now() ||
              time.getTime() < minDate
            );
          } else {
            return time.getTime() > Date.now();
          }
        },
        shortcuts: [
          {
            text: "今天",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近三天",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 3);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
    };
  },
  computed: {
    ...mapGetters(["userInfo", "storageOutRecordList", "outTicketInfo"]),
  },
  methods: {
    ...mapActions({
      GetStorageOutRecordList: "GetStorageOutRecordList",
      GetStorageOutRecordDetails: "GetStorageOutRecordDetails",
      GetOutTicketInfo: "GetOutTicketInfo",
      UpdateOutTicket: "UpdateOutTicket",
    }),
    searchOnClick() {
      let params = {
        storeCode: this.userInfo.storeCode,
        startDate: this.date[0],
        endDate: this.date[1],
        searchValue:this.searchValue,
        isTax:this.isTax
      };
      this.GetStorageOutRecordList(params);
    },
    focusOn() {
      this.startDateMin = null;
    },
    getFullTime(time) {
      let date = new Date(time), //时间戳为10位需*1000，时间戳为13位的话不需乘1000
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
      return Y + "-" + M + "-" + D + " " + h + ":" + m + ":" + s;
    },
    detailOnClick: function (e) {
      let ticketCode = e.currentTarget.id;
      let billCode = e.currentTarget.name;
      let params = {
        ticketCode: ticketCode,
        billCode: billCode,
        storeCode: this.userInfo.storeCode,
      };
      this.$router.replace({ path: "storageOutRecord/detail" });
      this.GetStorageOutRecordDetails(params);
    },
    printDetail(ticketCode) {
      let params = {
        ticketCode: ticketCode,
        storeCode: this.userInfo.storeCode,
      };
      this.GetOutTicketInfo(params);
      this.printDialogVisible = !this.printDialogVisible;
    },
    cancelOnClick() {
      this.printDialogVisible = !this.printDialogVisible;
    },
    loadCurrentTicket(ticket) {
      this.currentTicket = ticket;
      this.CodeDialogVisible = !this.CodeDialogVisible;
    },
    codeUpdateCannel() {
      this.CodeDialogVisible = !this.CodeDialogVisible;
    },
    saveCurrentTicket() {
      console.log(1);
      this.UpdateOutTicket(this.currentTicket);
      this.CodeDialogVisible = false;
    },
    printPdf() {
      print({
        printable: "exportPdf",
        type: "html",
        maxWidth: "100%",
        targetStyles: ["*"],
      });
    },
  },
  beforeMount: function () {
    this.timeDefaultShow = new Date().setMonth(new Date().getMonth() - 1);
  },
};
</script>
<style>
@page {
  margin-bottom: 1mm;
}

.storageoutrecord-content-header {
  height: 60px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
.storageoutrecord-title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  margin-left: 20px;
}
.datepickerrow{
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
.content-no-shadow {
  height: 1450px;
  /* box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2); */
  margin: 20px 0px;
  padding: 10px;
}
.content-scrollbar /deep/.el-scrollbar__wrap {
  overflow-x: hidden;
}
.content-scrollbar {
  height: 90vh;
}
.message-info {
  color: #79bbff;
  font-size: 20px;
}
.card-area-title {
  float: left;
  color: #e6a23c;
  font-size: 30px;
  font-weight: bold;
  padding: 10px;
}
.storageoutrecord-margin-left{
  margin-left: 20px;
}
.storageoutrecord-margin-top{
    margin-top: 20px;
  width: auto;
}
.record-card {
  display: flex;
  flex-direction: column;
  margin-top: 20px;
}
.box-card-out {
  margin-top: 20px;
}
.card-title {
  float: left;
  padding: 3px 0;
  font-weight: bold;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}
.card-content {
  font-size: 18px;
  margin-bottom: 30px;
}
.card-content-col-content {
  text-align: left;
  margin-left: 10px;
  color: #303133;
  height: 30px;
  line-height: 30px;
}
.card-content-col-name {
  text-align: right;
  color: #a6a8aa;
}
.detail-button {
  height: 30px !important;
  padding: 3px !important;
  font-size: 18px !important;
}
.additional-span {
  color: #f56c6c;
}
.normal-span {
  color: #67c23a;
}
.green-span {
  color: #3cb371;
}
.crimson-span {
  color: #dc143c;
}
.color-orange {
  color: orange;
  font-weight: bold;
}
.text-left {
  text-align: left;
}
.padding-10 {
  padding: 10px;
}
.padding-15 {
  padding: 15px;
}
.bold-large {
  font-weight: bold;
  font-size: 35px;
}
.print-detail-div {
  padding: 10px;
  text-align: left;
}
.signature-div {
  text-align: right;
  font-size: 25px;
  padding: 40px 10px;
  font-weight: 500;
}
.type-in-button {
  padding: 0;
  text-align: center;
}
.storageoutrecord-popover-items-area{
  display: flex;
  flex-direction: column;
  padding: 20px;
}
/* .search-area{
  margin-right: 20px;
} */
</style>