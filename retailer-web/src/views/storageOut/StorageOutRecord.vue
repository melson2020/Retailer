<template>
  <div>
    <div class="storageoutrecord-content-header">
      <div>
        <span class="storageoutrecord-title-name">出库记录</span>
      </div>
      <div>
        <span class="message-info">*时间跨度最多30天</span>
        <el-date-picker
          v-model="date"
          class="date-picker"
          type="daterange"
          align="right"
          size="small" 
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          :default-value="timeDefaultShow"
          format="yyyy 年 MM 月 dd 日"
          :picker-options="pickerOptions"
          @focus="focusOn"
        ></el-date-picker>
        <el-button
          :disabled="date ? false : true"
          type="primary"
          size="small" 
          icon="el-icon-search"
          @click="searchOnClick"
          >查询</el-button
        >
      </div>
    </div>
    <div class="content-no-shadow">
      <el-scrollbar  class="content-scrollbar">
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
                  <el-button
                    style="float: right; padding: 3px 0"
                    type="text"
                    :id="ticket.outTicket.code"
                    :name="ticket.outBill.code"
                    @click.prevent.stop="detailOnClick($event)"
                  >
                    查看详细</el-button
                  >
                </div>
                <div>
                  <el-row class="card-content">
                    <el-col :span="6" class="card-content-col-name"
                      >创建时间:</el-col
                    >
                    <el-col :span="15" class="card-content-col-content">{{
                      getFullTime(ticket.outTicket.createTime)
                    }}</el-col>
                  </el-row>
                  <el-row class="card-content">
                    <el-col :span="6" class="card-content-col-name"
                      >创建人员:</el-col
                    >
                    <el-col :span="15" class="card-content-col-content">{{
                      ticket.outTicket.employeeName
                    }}</el-col>
                  </el-row>
                  <el-row class="card-content">
                    <el-col :span="6" class="card-content-col-name"
                      >类型:</el-col
                    >
                    <el-col :span="15" class="card-content-col-content">
                      <span
                        v-if="ticket.outTicket.type == 'normal'"
                        class="normal-span"
                        >正常入库</span
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
                        v-if="ticket.outTicket.description&&ticket.outTicket.description.length > 15"
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
                    <el-col :span="8">
                      <span class="green-span"
                        >进价：{{ ticket.outBill.totalPriceIn }}</span
                      ></el-col
                    >
                    <el-col :span="8"
                      ><span class="green-span"
                        >成本：{{ ticket.outBill.cost }}</span
                      ></el-col
                    >
                  </el-row>
                  <el-row>
                    <el-col :span="8"
                      ><span class="crimson-span"
                        >售价：{{ ticket.outBill.totalPriceOut }}</span
                      ></el-col
                    >
                    <el-col :span="8"
                      ><span class="crimson-span"
                        >去税：{{ ticket.outBill.sales }}</span
                      ></el-col
                    >
                    <el-col :span="8"
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
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
export default {
  data() {
    return {
      date: "",
      timeDefaultShow: "",
      startDateMin: null,
      pickerOptions: {
        onPick: obj => {
          this.startDateMin = new Date(obj.minDate).getTime();
        },
        disabledDate: time => {
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
            }
          },
          {
            text: "最近三天",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 3);
              picker.$emit("pick", [start, end]);
            }
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            }
          }
        ]
      }
    };
  },
  computed: {
    ...mapGetters(["userInfo", "storageOutRecordList"])
  },
  methods: {
    ...mapActions({
      GetStorageOutRecordList: "GetStorageOutRecordList",
      GetStorageOutRecordDetails:"GetStorageOutRecordDetails"
    }),
    searchOnClick() {
      let params = {
        storeCode: this.userInfo.storeCode,
        startDate: this.date[0],
        endDate: this.date[1]
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
    detailOnClick: function(e) {
      let ticketCode = e.currentTarget.id;
      let billCode=e.currentTarget.name;
      let params={ticketCode:ticketCode,billCode:billCode}
      this.$router.replace({ path: "storageOutRecord/detail" });
      this.GetStorageOutRecordDetails(params)
    }
  },
  beforeMount: function() {
    this.timeDefaultShow = new Date().setMonth(new Date().getMonth() - 1);
  }
};
</script>
<style>
/* .content-header {
  height: 80px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
.header-title {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
} */
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
.date-picker {
  margin: 0px 20px;
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
.content-scrollbar{
  height: 85vh;
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
  color: #606266;
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
</style>