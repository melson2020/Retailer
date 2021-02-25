<template>
  <div>
    <div class="dashborad-header">
      <span class="dashborad-title-name">销售报表</span>
      <div>
        <el-date-picker
          v-model="month"
          type="month"
          placeholder="选择月"
          size="small"
        >
        </el-date-picker>
        <el-button
          :disabled="month ? false : true"
          type="primary"
          size="small"
          class="dashborad-margin-left-10"
          icon="el-icon-search"
          @click="searchOnClick"
          >查询</el-button
        >
      </div>
    </div>
    <div id="dashbprad-content-div" class="dashboard-main">
      <div class="dashboard-main-left-content">
        <div id="chart-1" class="echart-bar-1"></div>
        <div id="chart-2" class="echart-bar-1"></div>
      </div>
      <div id="chart-3" class="dashboard-main-right-content"></div>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
import * as echarts from "echarts"; //引入echart
export default {
  data() {
    return {
      month: new Date(),
      productChart: {},
      employeeChart: {},
      sortChart: {},
      pieBaseOption: {},
    };
  },
  methods: {
    ...mapActions({
      GetDashBoardData: "GetDashBoardData",
    }),

    drawCharts(productOption, employeeOption, sortOption) {
      let optionData = {
        backgroundColor: "white",
        title: {
          text: productOption.title,
          left: "left",
          top: 0,
          textStyle: {
            color: "#606266",
            fontSize: 14,
          },
        },
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b} ({d}%) ",
        },
        series: productOption.series,
        color: [
          "#9489fa",
          "#f06464",
          "#f7af59",
          "#16afcc",
          "#898989",
          "#c4cc38",
        ],
      };

      let eOption = {
        backgroundColor: "white",
        title: {
          text: employeeOption.title,
          left: "left",
          top: 0,
          textStyle: {
            color: "#606266",
            fontSize: 14,
          },
        },
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b} ({d}%) ",
        },
        series: employeeOption.series,

        color: [
          "#9489fa",
          "#f06464",
          "#f7af59",
          "#f0da49",
          "#71c16f",
          "#2aaaef",
          "#5690dd",
          "#bd88f5",
          "#009db2",
          "#024b51",
          "#765005",
        ],
      };

      //柱状图
      // 指定图表的配置项和数据
      var sOption = {
        title: {
          text: sortOption.title,
          left: "left",
          textStyle: {
            color: "#606266",
            fontSize: 14,
          },
        },
        grid: {
          left: "2%",
          right: "2%",
          bottom: "2%",
          containLabel: true,
        },
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{c}",
        },
        xAxis: [
          {
            type: "log",
            position: "top",
            splitLine: {
              show: true,
            },
            axisLine: {
              show: true, //这里的show用于设置是否显示x轴那一条线 默认为true
              lineStyle: {
                //lineStyle里面写x轴那一条线的样式
                color: "#6FC6F3",
                width: 2, //轴线的粗细 我写的是2 最小为0，值为0的时候线隐藏
              },
            },
          },
        ],
        yAxis: [
          {
            type: "category",
            max: 20,
            inverse: true,
            axisLabel: {
              show: true,
              textStyle: {
                color: "#889EC6",
              },
            },
            splitLine: {
              show: false,
            },
            axisTick: {
              show: true,
            },
            axisLine: {
              show: true,
              lineStyle: {
                //lineStyle里面写x轴那一条线的样式
                color: "#6FC6F3",
                width: 2, //轴线的粗细 我写的是2 最小为0，值为0的时候线隐藏
              },
            },
            data: sortOption.yAxisData,
          },
        ],
        series: sortOption.series,
      };

      this.productChart.setOption(optionData);
      this.employeeChart.setOption(eOption);
      this.sortChart.setOption(sOption);
    },
    //方法
    Init: function () {
      //父级div大小改变，图表跟着变化，也可以写成 Init(){}
      window.addEventListener(
        "resize",
        function () {
          this.productChart.resize();
          this.employeeChart.resize();
          this.sortChart.resize();
          this.setDashBoradAreaSize();
        }.bind(this)
      );
    },
    searchOnClick() {
      var nowMonth = this.month.getMonth(); //当前月
      var nowYear = this.month.getFullYear(); //当前年
      //本月的开始时间
      var monthStartDate = new Date(nowYear, nowMonth, 1).format("yyyy-MM-dd");
      //本月的结束时间
      var monthEndDate = new Date(nowYear, nowMonth + 1, 1).format(
        "yyyy-MM-dd"
      );
      var params = {
        storeCode: this.userInfo.storeCode,
        startDate: monthStartDate,
        endDate: monthEndDate,
      };
      this.GetDashBoardData(params)
        .then((res) => {
          if (res.resultStatus == 1) {
            //产品饼图排序
            var productList = res.data.productList;
            var pSeries = [
              {
                name: "产品占比",
                type: "pie",
                clockwise: "true",
                startAngle: "0",
                radius: "80%",
                center: ["50%", "50%"],
                data: [],
              },
            ];
            if (productList.length > 0) {
              productList.map((item) => {
                var dataItem = { value: item.itemValue, name: item.itemName };
                pSeries[0].data.push(dataItem);
              });
            } else {
              pSeries[0].data.push({ value: 1, name: "无数据" });
            }
            //员工饼图排序
            var employeeList = res.data.employeeList;
            var eSeries = [
              {
                name: "员工占比",
                type: "pie",
                clockwise: "true",
                startAngle: "0",
                radius: "80%",
                center: ["50%", "50%"],
                data: [],
              },
            ];
            if (employeeList.length > 0) {
              employeeList.map((item) => {
                var dataItem = { value: item.itemValue, name: item.itemName };
                eSeries[0].data.push(dataItem);
              });
            } else {
              eSeries[0].data.push({ value: 1, name: "无数据" });
            }
            //柱状图排序
            var sortList = res.data.sortList;
            var sSeries = [
              {
                name: "销售额",
                type: "bar",
                itemStyle: {
                  //通常情况下：
                  normal: {
                    //每个柱子的颜色即为colorList数组里的每一项，如果柱子数目多于colorList的长度，则柱子颜色循环使用该数组
                    color: function (params) {
                      var colorList = [
                        "#9489fa",
                        "#f06464",
                        "#f7af59",
                        "#f0da49",
                        "#71c16f",
                        "#2aaaef",
                        "#5690dd",
                        "#bd88f5",
                        "#009db2",
                        "#024b51",
                        "#765005",
                      ];
                      return colorList[params.dataIndex % colorList.length];
                    },
                  },
                  //鼠标悬停时：
                  emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: "rgba(0, 0, 0, 0.5)",
                  },
                },
                data: [],
                barCategoryGap: "2%",
              },
            ];
            var sortYdata = [];
            if (sortList.length > 0) {
              if (sortList.length > 20) sortList = sortList.slice(0, 20);
              sortList.map((item) => {
                var dataItem = { value: item.itemValue, name: item.itemName };
                sSeries[0].data.push(dataItem);
                sortYdata.push(item.itemName);
              });
            } else {
              for (let index = 1; index <= 20; index++) {
                sortYdata.push(index);
              }
            }
            var pOption = { title: "产品占比", series: pSeries };
            var eOption = { title: "员工占比", series: eSeries };
            var sOption = {
              title: "热销产品",
              series: sSeries,
              yAxisData: sortYdata,
            };
            this.drawCharts(pOption, eOption, sOption);
          } else {
            this.$message.warning(res.message);
          }
        })
        .catch((err) => {
          this.$message.error(err.message);
        });
    },
    setDashBoradAreaSize() {
      var areaHeight = window.innerHeight - 200;
      var areaWidth = window.innerWidth - 230;
      var element = document.getElementById("dashbprad-content-div");
      element.style.width = areaWidth + "px";
      element.style.height = areaHeight + "px";
    },
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  //生命周期钩子
  mounted() {
    this.setDashBoradAreaSize();
    //html elemet 挂载charts
    this.productChart = echarts.init(document.getElementById("chart-1"));
    this.employeeChart = echarts.init(document.getElementById("chart-2"));
    this.sortChart = echarts.init(document.getElementById("chart-3"));
    this.Init(); //方法调用
    this.searchOnClick();
  },
};
</script>
<style>
.echart-bar-1 {
  height: 48%;
  width: 96%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  margin: 2%;
  border:1px solid #E9E9EB;
  padding: 10px;
}
.dashboard-main {
  display: flex;
  flex-direction: row;
}
.dashboard-main-left-content {
  height: 100%;
  width: 50%;
}
.dashboard-main-right-content {
  height: 100%;
  width: 48%;
   box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  margin: 1%;
  border:1px solid #E9E9EB;
  padding: 10px;
}
.dashborad-header {
  height: 100px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}
.dashborad-margin-left-10 {
  margin-left: 20px;
}
.dashborad-title-name {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
}
</style>