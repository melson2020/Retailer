import Vue from "vue";
import VueRouter from "vue-router";

const Login = () => import("../views/Login.vue");
const Register = () => import("../views/Register.vue");
const Main = () => import("../views/Main.vue");
const Error = () => import("../views/404.vue");
const Supply = () => import("../views/dicts/Supply.vue");
const Employee = () => import("../views/dicts/Employee.vue");
const ProductDict = () => import("../views/dicts/ProductDict.vue");
const ProductImport = () => import("../views/dicts/ProductImport.vue")
const ProductStock = () => import("../views/storage/ProductStorage.vue")
const StorageCount = () => import("../views/storage/StorageCount.vue")
const StorageCountRecord = () => import("../views/storage/StorageCountRecord.vue")
const CreateCountTicket = () => import("../views/storage/CreateTicket.vue")
const Preview = () => import("../views/storage/Preview.vue")
const Import = () => import("../views/storage/Import.vue")
const UpdateBatch = () => import("../views/storage/BatchUpdate.vue")
const Complete = () => import("../views/storage/Complete.vue")
const StorageIn = () => import("../views/storageIn/StorageIn.vue")
const StorageInRecord = () => import("../views/storageIn/StorageInRecord.vue")
const StorageOut = () => import("../views/storageOut/StorageOut.vue")
const StorageOutRecord = () => import("../views/storageOut/StorageOutRecord.vue")
const StorageOutRecordDetail = () => import("../views/storageOut/StorageOutRecordDetail.vue")
const OutboundDelivery = () => import("../views/report/OutboundDelivery.vue")
const ProductStorageRecord = () => import("../views/report/ProductStorageRecord.vue")
const GoodsReturn = () => import("../views/goodsReturn/GoodsReturn.vue")

Vue.use(VueRouter);

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
},
  VueRouter.prototype.replace = function replace(location) {
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
    children: [
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
        path: "productStorage",
        name: "productStorage",
        component: ProductStock
      },
      {
        path: "storageCount",
        name: "storageCount",
        component: StorageCount,
        children: [
          {
            path: "create",
            name: "create",
            component: CreateCountTicket
          },
          {
            path: "preview",
            name: "preview",
            component: Preview
          },
          {
            path: "import",
            name: "import",
            component: Import
          },
          {
            path: "updateBatch",
            name: "updateBatch",
            component: UpdateBatch
          },
          {
            path: "complete",
            name: "complete",
            component: Complete
          },
          {
            path: "/",
            name: "storageCountRoot",
            component: CreateCountTicket
          },
        ]
      },
      {
        path: 'storageCountRecord',
        name: "storageCountRecord",
        component: StorageCountRecord
      },
      {
        path: "storageIn",
        name: "storageIn",
        component: StorageIn
      },
      {
        path: "storageRecord",
        name: "storageRecord",
        component: StorageInRecord
      },
      {
        path: "storageOut",
        name: "storageOut",
        component: StorageOut
      },
      {
        path: "storageOutRecord",
        name: "storageOutRecord",
        component: StorageOutRecord,
      },
      {
        path: "storageOutRecord/detail",
        name: "storageOutRecordDetail",
        component: StorageOutRecordDetail,
      },
      {
        path: "OutboundDelivery",
        name: "OutboundDelivery",
        component: OutboundDelivery,
      },
      {
        path: "productStorageRec",
        name: "productStorageRec",
        component: ProductStorageRecord,
      },
      {
        path: "goodsReturn",
        name: "goodsReturn",
        component: GoodsReturn
      },
      {
        path: "pageNotFound",
        name: "pageNotFound",
        component: Error,
      },
      {
        path: "/",
        name: "root",
        component: ProductStock
      },
      {
        path: "*",
        name: "404",
        component: Error
      }
    ]
  },
  {
    path: "/",
    name: "sysroot",
    component: Login
  },
];

const router = new VueRouter({
  mode: "history",
  routes
});

const noPermissionPage = ['/', "/login", '/register'];
const messgaePage = ['/main/pageNotFound']

/**
 * 路由守卫
 * 权限验证逻辑：进入下个路由前，判断是否在用户menuList 中 不在则无权限
 * 下个路由也有可能是不存在的路劲 如（/ssss) 这种情况也无法查找出
 */
router.beforeEach((to, from, next) => {
  var index = noPermissionPage.indexOf(to.path);
  if (index >= 0) {
    next()
  } else {
    let userInfo = localStorage.getItem("userInfo");
    if (userInfo) {
      if (messgaePage.indexOf(to.path) >= 0) {
        next()
      } else {
        var jsonUser = JSON.parse(userInfo);
        var menuList = jsonUser.menuList;
        var path = to.path;
        var hasPermission = false;
        for (let i = 0; i < menuList.length; i++) {
          const menu = menuList[i];
          for (let j = 0; j < menu.subMenus.length; j++) {
            const subMenu = menu.subMenus[j];
            if (path.indexOf(subMenu.index) >= 0) {
              hasPermission = true;
              break
            }
          }
        }
        if (hasPermission) {
          next()
        } else {
          next({ path: "/main/pageNotFound" })
        }
      }
    } else { next({ path: "/login" }) }
  }
});
export default router;
