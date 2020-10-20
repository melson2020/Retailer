import Vue from "vue";
import VueRouter from "vue-router";

const Login = () => import("../views/Login.vue");
const Register = () => import("../views/Register.vue");
const Main = () => import("../views/Main.vue");
const Error = () => import("../views/404.vue");
const Supply = () => import("../views/dicts/Supply.vue");
const Employee = () => import("../views/dicts/Employee.vue");
const ProductDict=()=> import("../views/dicts/ProductDict.vue");
const ProductImport=()=> import("../views/dicts/ProductImport.vue")
const ProductStock=()=>import("../views/storage/ProductStorage.vue")
const StorageCount=()=>import("../views/storage/StorageCount.vue")
const StorageIn=()=>import("../views/storageIn/StorageIn.vue")
const StorageInRecord=()=>import("../views/storageIn/StorageInRecord.vue")
const StorageOut=()=>import("../views/storageOut/StorageOut.vue")
const StorageOutRecord=()=>import("../views/storageOut/StorageOutRecord.vue")
const StorageOutRecordDetail=()=>import("../views/storageOut/StorageOutRecordDetail.vue")

Vue.use(VueRouter);

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
},
VueRouter.prototype.replace = function replace (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: "/login",
    name: "Login",
    component: Login
  },
  {
    path: "/register",
    name: "Register",
    component: Register
  },
  {
    path: "/main",
    name: "Main",
    component: Main,
    // children: [
    //   {
    //     path: "product", //以“/”开头的嵌套路径会被当作根路径，所以子路由上不用加“/”;在生成路由时，主路由上的path会被自动添加到子路由之前，所以子路由上的path不用在重新声明主路由上的path了。
    //     name: "product",
    //     component: Product,
    //     children:[
    //       {
    //         path: "dict",
    //         name: "dict",
    //         component: ProductDict
    //       },
    //       {
    //         path: "import",
    //         name: "import",
    //         component: ProductImport
    //       },
    //       {
    //         path: "/",
    //         name: "productRoot",
    //         component: ProductDict
    //       },
    //     ]
    //   },
    children:[
      {
        path: "dict",
        name: "dict",
        component: ProductDict
      },
      {
        path: "import",
        name: "import",
        component: ProductImport
      },
      {
        path: "supply",
        name: "supply",
        component: Supply
      },
      {
        path: "employee",
        name: "employee",
        component: Employee
      },
      {
        path:"productStorage",
        name:"productStorage",
        component:ProductStock
      },
      {
        path:"storageCount",
        name:"StorageCount",
        component:StorageCount
      },
      {
        path:"storageIn",
        name:"storageIn",
        component:StorageIn
      },
      {
        path:"storageRecord",
        name:"storageRecord",
        component:StorageInRecord
      },
      {
        path:"storageOut",
        name:"storageOut",
        component:StorageOut
      },
      {
        path:"storageOutRecord",
        name:"storageOutRecord",
        component:StorageOutRecord,
      },
      {
        path:"storageOutRecord/detail",
        name:"storageOutRecordDetail",
        component:StorageOutRecordDetail,
      },
      {
        path: "/",
        name: "root",
        component: ProductDict
      },
      {
        path: "*",
        name: "404",
        component: Error
      }
    ]
  }
];

const router = new VueRouter({
  mode: "history",
  routes
});

/**
 * 路由守卫
 * 可添加权限验证
 */
router.beforeEach((to, from, next) => {
  if (to.path == "/") {
    next({ path: "/login" });
  } else {
    if (to.path !== "/login" && to.path !== "/register") {
      let userInfo = localStorage.getItem("userInfo");
      if (userInfo) {
        next();
      } else {
        next({ path: "/login" });
      }
    } else {
      next();
    }
  }
});
export default router;
