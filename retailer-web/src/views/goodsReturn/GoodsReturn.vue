<template>
  <div>
    <div class="search-area">
      <div class="search-box">
        <span class="font-size-35 color-title">查询出货单</span>
        <el-divider></el-divider>
        <div class="value-area">
          <el-input
            v-model="searchValue"
            placeholder="出库单号/客户名称"
          ></el-input>
          <el-date-picker
            v-model="searchDate"
            class="margin-left"
            type="date"
            placeholder="选择日期"
          >
          </el-date-picker>
        </div>
        <div class="button-area">
          <el-button @click="searchOnClick" type="primary">查询</el-button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      searchValue: "",
      searchDate: "",
    };
  },
  methods: {
    searchOnClick() {
      console.log(this.searchValue, this.searchDate);
    },
  },
};
</script>
<style>
.search-area {
  display: flex;
  justify-content: center;
}
.search-box {
  width: 18vw;
  border-radius: 10px;
  padding: 20px;
  border: 1px solid lightgray;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
}
.font-size-35 {
  font-size: 1.5em;
  font-weight: bold;
}
.color-title {
  color: #303133;
}
.button-area {
  padding: 20px 0;
  text-align: right;
}
.value-area {
  display: flex;
  flex-direction: row;
}
.margin-left {
  margin-left: 10px;
}
</style>