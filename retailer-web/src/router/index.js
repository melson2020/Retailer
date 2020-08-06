import Vue from "vue";
import VueRouter from "vue-router";

const Login = () => import("../views/Login.vue");
const Register = () => import("../views/Register.vue");
const Main = () => import("../views/Main.vue");

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
  { path: "/main", name: "Main", component: Main }
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
