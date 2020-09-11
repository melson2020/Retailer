<template>
  <div>
    <div class="top-area">
      <span class="title-name">商品库存</span>
      <el-input
        class="fliter-input"
        v-model="searchContent"
        placeholder="搜索名称 / 型号"
        suffix-icon="el-icon-search"
        @focus="searchFocus"
      ></el-input>
    </div>
    <el-table :data="productLishPageShow" border class="product-table">
      <el-table-column prop="name" label="名字" align="left"></el-table-column>
      <el-table-column prop="type" label="联系地址" align="center"></el-table-column>
    </el-table>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
export default {
  data() {
    return {
      searchContent: ""
    };
  },
  methods: {
      ...mapActions({
      GetProductList: "GetProductList",
    }),
    searchFocus() {
      console.log("haha");
    }
  },
  computed: {
    ...mapGetters(["userInfo", "productList"]),
    productLishPageShow(){
        return this.productList;
    }
  },
  beforeMount() {
      console.log("jiemian xuanran")
    let params = { storeCode: this.userInfo.storeCode };
    this.GetProductList(params);
  }
};
</script>
<style>
</style>
