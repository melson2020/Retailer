<template>
  <div style="height:100%">
    <div class="storageinrecord-content-header">
      <div>
        <span class="storageinrecord-title-name">入库记录</span>
      </div>
      <div>
        <span class="message-info">*最多查询30天记录</span>
        <el-date-picker
          size="small"
          v-model="date"
          class="date-picker"
          type="daterange"
          align="right"
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
            :disabled="date?false:true"
            type="primary"
            size="small" 
            icon="el-icon-search"
            @click="searchOnClick"
          >查询</el-button>
      </div>
    </div>
    <div class="storageinrecord-content">
      <el-scrollbar style="height:100%" class="content-scrollbar">
        <div class="record-display-area">
          <span v-if="recordList.length<=0" class="non-data-info">未查询到数据</span>
          <div v-else class="record-card" v-for="(record,index) in recordList" :key="index">
            <div>
              <span class="card-area-title">{{record.date}}</span>
            </div>
            <div>
              <el-col v-for="detail in record.tickets" :key="detail.id" class="card-col">
                <el-card shadow="never" class="box-card">
                  <div slot="header" class="clearfix">
                    <span class="card-title">入库单号: {{detail.code}}</span>
                    <el-button
                      class="card-detail-button"
                      :id="detail.code"
                      type="text"
                      @click.prevent.stop="detailOnClick($event)"
                    >查看详细</el-button>
                  </div>
                  <div class="card-content-area">
                    <el-row class="card-content">
                      <el-col :span="6" class="card-content-col-name">批次号:</el-col>
                      <el-col :span="15" class="card-content-col-content">{{detail.batchNo}}</el-col>
                    </el-row>
                    <el-row class="card-content">
                      <el-col :span="6" class="card-content-col-name">创建时间:</el-col>
                      <el-col
                        :span="15"
                        class="card-content-col-content"
                      >{{getFullTime(detail.createTime)}}</el-col>
                    </el-row>
                    <el-row class="card-content">
                      <el-col :span="6" class="card-content-col-name">创建人员:</el-col>
                      <el-col :span="15" class="card-content-col-content">{{detail.employeeName}}</el-col>
                    </el-row>
                    <el-row class="card-content">
                      <el-col :span="6" class="card-content-col-name">类型:</el-col>
                      <el-col :span="15" class="card-content-col-content">
                        <span v-if="detail.type=='additional'" class="additional-span">临时入库</span>
                        <span v-else class="normal-span">正常入库</span>
                      </el-col>
                    </el-row>
                    <el-row class="card-content">
                      <el-col :span="6" class="card-content-col-name">描述:</el-col>
                      <el-col :span="15" class="card-content-col-content">
                        <el-popover
                          v-if="detail.description.length>20"
                          placement="top-start"
                          title="详细描述"
                          width="300"
                          trigger="hover"
                          :content="detail.description"
                        >
                          <el-button
                            slot="reference"
                            class="detail-button"
                            type="warning"
                            plain
                          >内容过长,点击查看</el-button>
                        </el-popover>
                        <span v-else>{{detail.description}}</span>
                      </el-col>
                    </el-row>
                  </div>
                </el-card>
              </el-col>
            </div>
          </div>
        </div>
      </el-scrollbar>
      <el-dialog title="入库详细" :visible.sync="dialogVisible" width="40%">
        <el-table :data="recordDetails" border size="mini">
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
        </el-table>
      </el-dialog>
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
      dialogVisible: false,
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
    ...mapGetters(["userInfo", "recordList", "recordDetails"])
  },
  methods: {
    ...mapActions({
      GetRecordList: "GetRecordList",
      GetRecordDetails: "GetRecordDetails"
    }),
    searchOnClick() {
      let params = {
        storeCode: this.userInfo.storeCode,
        startDate: this.date[0],
        endDate: this.date[1]
      };
      this.GetRecordList(params);
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
      let code = e.currentTarget.id;
      let params = { storeInTicketCode: code };
      this.GetRecordDetails(params);
      this.dialogVisible = true;
    }
  },
  beforeMount: function() {
    this.timeDefaultShow = new Date().setMonth(new Date().getMonth() - 1);
  }
};
</script>
<style>
.storageinrecord-content-header {
  height: 60px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
.storageinrecord-title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  margin-left: 20px;
}
.storageinrecord-content{
  margin-top: 5px;
}

/* .header-title {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
} */
.date-picker {
  margin: 0px 20px;
}
/* .content {
  height: 85vh;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
  margin: 20px 0px;
} */
.content-scrollbar /deep/.el-scrollbar__wrap {
  overflow-x: hidden;
}
.message-info {
  color: #79bbff;
  font-size: 20px;
}
.card-area-title {
  float: left;
  color: #e6a23c;
  font-size: 28px;
  font-weight: bold;
  padding: 10px;
}
.record-display-area {
  padding: 0px 20px;
}
.record-card {
  display: flex;
  flex-direction: column;
  margin-top: 20px;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}
.record-card-area {
  display: flex;
  flex-direction: row;
}
.card-col {
  padding-top: 10px;
  margin-right: 10px;
  width: 515px;
  height: 480px;
}
.card-title {
  float: left;
  padding: 3px 0;
  font-weight: bold;
}
.card-detail-button {
  float: right;
  padding: 3px 0;
}
.box-card {
  height: 100%;
}
.card-content {
  font-size: 18px;
  margin-bottom: 30px;
}
.card-content-area {
  display: flex;
  flex-direction: column;
  width: 100%;
}
.card-content-col-name {
  text-align: right;
  color: #606266;
}
.card-content-col-content {
  text-align: left;
  margin-left: 10px;
  color: #303133;
  height: 30px;
  line-height: 30px;
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
.non-data-info {
  color: #e6a23c;
  font-size: 30px;
  padding: 40px;
  margin-top: 20px;
  height: 100px;
  line-height: 100px;
}
</style>