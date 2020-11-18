<template>
  <div class="register" :style="loginBackground">
    <el-card class="register-card">
      <div slot="header">
        <el-button
          style="float: left; padding: 3px 0"
          type="text"
          @click="backClick"
          >返回</el-button
        >
        <!-- <i class="el-icon-back back-icon" @click="backClick" font-size="20px"></i> -->
        <span class="register-title">商户注册</span>
      </div>
      <el-form
        ref="storeForm"
        :model="store"
        label-width="100px"
        :rules="rules"
      >
        <el-form-item label="商户名称" prop="storeName">
          <el-input v-model="store.storeName"></el-input>
        </el-form-item>
        <el-form-item label="商户地址">
          <div class="register-row-between">
            <el-select
              filterable
              placeholder="省"
              v-model="store.provinceCode"
              @change="provinceChanged"
            >
              <el-option
                v-for="item in provinceList"
                :key="item.id"
                :label="item.name"
                :value="item.code"
              ></el-option>
            </el-select>
            <el-select
              filterable
              placeholder="市"
              :disabled="showCity"
              v-model="store.cityCode"
              @change="cityChanged"
            >
              <el-option
                v-for="city in cityList"
                :key="city.id"
                :label="city.name"
                :value="city.code"
              ></el-option>
            </el-select>
            <el-select
              filterable
              placeholder="区"
              :disabled="showArea"
              v-model="store.areaCode"
              @change="areaChanged"
            >
              <el-option
                v-for="area in areaList"
                :key="area.id"
                :label="area.name"
                :value="area.code"
              ></el-option>
            </el-select>
          </div>
        </el-form-item>
        <el-form-item prop="location">
          <el-input placeholder="详细地址" v-model="store.location"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
            type="textarea"
            autosize
            v-model="address"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="联系人" prop="communicateName">
          <el-input v-model="store.communicateName"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input
            placeholder="号码会被用作管理员登录账户"
            v-model="store.phone"
          ></el-input>
        </el-form-item>
        <el-form-item label="登录密码" prop="password">
          <el-input
            type="password"
            v-model="store.password"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="再次输入密码" prop="checkpassword">
          <el-input
            type="password"
            v-model="store.checkpassword"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            rows="3"
            type="textarea"
            maxlength="200"
            show-word-limit
            v-model="store.description"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            class="register-button"
            @click="onSubmit('storeForm')"
            >注册</el-button
          >
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script>
import { mapActions } from "vuex";
import { mapGetters } from "vuex";

export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.store.checkpassword !== "") {
          this.$refs.storeForm.validateField("checkpassword");
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.store.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    var validatePhone = (rule, value, callback) => {
      var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
      if (value === "") {
        callback(new Error("请输入联系电话"));
      } else if (!myreg.test(value)) {
        callback(new Error("请输入正确的号码"));
      } else {
        let phoneNumber = { phoneNumber: value };
        this.CheckPhone(phoneNumber)
          .then((res) => {
            if (res.data) {
              callback();
            } else {
              callback(new Error("该号码已存在"));
            }
          })
          .catch((error) => {
            this.$message.error(error.message);
          });
      }
    };
    return {
      store: {
        storeName: "",
        location: "",
        provinceCode: "",
        provinceName: "",
        cityCode: "",
        cityName: "",
        areaCode: "",
        areaName: "",
        communicateName: "",
        phone: "",
        password: "",
        checkpassword: "",
        description: "",
      },
      loginBackground: {
        backgroundImage:
          "url(" + require("../assets/login-background.png") + ")",
        backgroundRepeat: "no-repeat",
      },
      rules: {
        storeName: [
          { required: true, message: "请输入商户名称", trigger: "blur" },
        ],
        location: [
          { required: true, message: "请填写商户地址", trigger: "blur" },
        ],
        communicateName: [
          { required: true, message: "请填写联系人员", trigger: "blur" },
        ],
        phone: [
          {
            validator: validatePhone,
            trigger: "blur",
          },
        ],
        password: [{ validator: validatePass, trigger: "blur" }],
        checkpassword: [{ validator: validatePass2, trigger: "blur" }],
      },
    };
  },
  computed: {
    ...mapGetters(["alterMessage", "provinceList", "cityList", "areaList"]),
    showCity: function () {
      return (
        typeof this.store.provinceCode == "undefined" ||
        this.store.provinceCode == null ||
        this.store.provinceCode == ""
      );
    },
    showArea: function () {
      return (
        typeof this.store.cityCode === "undefined" ||
        this.store.cityCode == null ||
        this.store.cityCode == ""
      );
    },
    address: function () {
      return (
        this.store.provinceName +
        this.store.cityName +
        this.store.areaName +
        this.store.location
      );
    },
  },
  watch: {
    // 如果 `errorMessage` 发生改变，这个函数就会运行
    alterMessage: function () {
      if (this.alterMessage.show) {
        this.$message({
          message: this.alterMessage.message,
          type: this.alterMessage.type,
        });
      }
    },
  },
  methods: {
    ...mapActions({
      GetProvinceList: "GetProvinceList",
      GetCityList: "GetCityWithCode",
      GetAreaList: "GetAreaWithCode",
      RegisterStore: "RegisterStore",
      CheckPhone: "CheckPhone",
    }),
    onSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.RegisterStore(this.store);
        } else {
          this.$message.warning("请填写准确信息");
          return false;
        }
      });
    },

    backClick: function () {
      this.$router.push({ path: "/" });
    },
    provinceChanged: function (item) {
      let privinceCode = { provinceCode: item };
      let provinceName = this.provinceList.find((province) => {
        return province.code == item;
      }).name;
      this.store.provinceName = provinceName;
      this.GetCityList(privinceCode);
    },
    cityChanged: function (item) {
      let cityCode = { cityCode: item };
      let cityName = this.cityList.find((city) => {
        return city.code == item;
      }).name;
      this.store.cityName = cityName;
      this.GetAreaList(cityCode);
    },
    areaChanged: function (item) {
      this.store.areaName = this.areaList.find((area) => {
        return area.code == item;
      }).name;
    },
  },
  beforeMount: function () {
    this.GetProvinceList();
  },
};
</script>
<style>
.register-back-icon {
  float: left;
  font-size: 2em;
  color: rgb(226, 215, 215);
  height: 100%;
}
.register {
  display: flex;
  height: 100vh;
  margin: 0;
  align-items: center;
  justify-content: center;
  background-size: 100% 100%;
}
.register-card {
  width: 1500px;
}
.register-title {
  color: rgb(64, 136, 231);
  font-size: 2em;
  font-weight: bold;
  letter-spacing: 0.5em;
}
.register-row-between {
  display: flex;
  flex-direction: row;
  width: 100%;
  justify-content: space-between;
}
.register-button {
  width: 100%;
  font-size: 1.5em;
  letter-spacing: 0.5em;
  font-weight: bold;
}
.register-textblock {
  float: left;
  width: 100%;
  border: 0;
}
</style>
