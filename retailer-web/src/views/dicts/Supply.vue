<template>
  <el-row>
    <el-col :span="24">
      <div class="grid-content bg-purple">
        <div class="title-div">
          <span class="title-name">测试</span>
          <el-button icon="el-icon-plus" @click="resetForm('addNewSupply')">添加</el-button>
        </div>
        <el-table :data="supplyList" border class="supply-table">
          <el-table-column prop="name" label="名称" align="left"></el-table-column>
          <el-table-column prop="address" label="地址" align="left"></el-table-column>
          <el-table-column prop="contact" label="联系人" align="left"></el-table-column>
          <el-table-column prop="phone" label="联系电话" align="left"></el-table-column>
          <el-table-column label="操作" align="center">
            <template>
              <el-button>编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-dialog
          title="新增供应商"
          :visible.sync="dialogFormVisible"
          :close-on-click-modal="false"
          :show-close="false"
        >
          <el-form :model="newSupply" :rules="rules" ref="addNewSupply" label-width="120px">
            <el-form-item label="供应商名" prop="name">
              <el-input v-model="newSupply.name" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>
            <el-form-item label="联系地址" prop="address">
              <el-input v-model="newSupply.address" autocomplete="off" style="width: 100%;"></el-input>
            </el-form-item>

            <el-row>
              <el-col :span="12">
                <el-form-item label="联系人员" prop="contact">
                  <el-input v-model="newSupply.contact" autocomplete="off" style="width: 100%;"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" prop="phone">
                <el-form-item label="联系电话" prop="phone">
                  <el-input v-model="newSupply.phone" autocomplete="off" style="width: 100%;"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="备注信息" prop="comment">
              <el-input
                v-model="newSupply.comment"
                autocomplete="off"
                style="width: 100%;"
                rows="3"
                type="textarea"
                maxlength="200"
                show-word-limit
              ></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false" v-if="!loading">取消</el-button>
            <el-button type="primary" @click="onAddNewSupply('addNewSupply')" :loading="loading">确定</el-button>
          </div>
        </el-dialog>
      </div>
    </el-col>
  </el-row>
</template>


<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
export default {
  data() {
    return {
      dialogFormVisible: false,
      state2: "",
      loading: false,
      newSupply: {
        name: "",
        address: "",
        contact: "",
        phone: "",
        comment: "",
        storeCode: ""
      },
      rules: {
        name: [
          { required: true, message: "请输入厂商名称", trigger: "blur" },
          { min: 2, max: 55, message: "长度在 2 到 55 个字符", trigger: "blur" }
        ],
        address: [
          { required: true, message: "请输入联系地址", trigger: "blur" }
        ]
      }
    };
  },
  computed: {
    ...mapGetters(["userInfo", "supplyList"])
  },
  methods: {
    ...mapActions({
      GetSupplyList: "GetSupplyList",
      CreateSupply: "CreateSupply",
      PushSupplyList: "PushSupplyList"
    }),
    resetForm(formName) {
      if (this.$refs[formName]) {
        this.$refs[formName].resetFields();
      }
      this.dialogFormVisible = true;
    },
    onAddNewSupply(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.newSupply.storeCode = this.userInfo.storeCode;
          this.CreateSupply(this.newSupply)
            .then(res => {
              if (res.resultStatus == 1) {
                this.dialogFormVisible = false;
                this.PushSupplyList(res.data);
                this.$message({
                  showClose: true,
                  message: "操作成功",
                  type: "success"
                });
              } else {
                this.$message.error(res.message);
              }
            })
            .catch(err => {
              let alert = err.message ? err.message : err;
              this.$message.error(alert);
            });
        } else {
          this.$message.warning("请填写准确信息");
          return false;
        }
      });
    }
  },
  beforeMount: function() {
    this.GetSupplyList(this.userInfo);
  }
};
</script>

<style>
.el-row {
  height: 100%;
}
.el.col {
  border-radius: 4px;
  height: 100%;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
  height: 100%;
}
.title-div {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 80px;
}
.title-name {
  font-size: 30px;
  font-weight: bold;
  color: gray;
}
.supply-table {
  margin-top: 20px;
}
</style>
