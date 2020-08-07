<template>
    
    <div>
      <h1>This is supply page</h1>
      <input type="text" v-on:keyup="getSupplyList"/>
    </div>
</template>
<script>
export default {
  data:{

  },
  methods:{
    popup:function(){
      console.log("sss");
    },
    getSupplyList:function(){
      console.log("获取supplyList");
    }
  }
};
</script>
<style></style>
