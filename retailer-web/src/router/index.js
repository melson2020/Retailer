import Vue from "vue";
import VueRouter from "vue-router";

const Login = () => import("../views/Login.vue");
const Register = () => import("../views/Register.vue");
const Main = () => import("../views/Main.vue");
const Supply = () => import("../views/Supply.vue");
const Product = () => import("../views/Product.vue");
const Error = () => import("../views/404.vue");

Vue.use(VueRouter);

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
        path: "product", //以“/”开头的嵌套路径会被当作根路径，所以子路由上不用加“/”;在生成路由时，主路由上的path会被自动添加到子路由之前，所以子路由上的path不用在重新声明主路由上的path了。
        name: "product",
        component: Product
      },
      {
        path: "supply",
        name: "supply",
        component: Supply
      },
      {
        path: "/",
        name: "root",
        component: Product
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
