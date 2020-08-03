import Vue from "vue";
import VueRouter from "vue-router";

const Login = () => import("../views/Login.vue");
const Register = () => import("../views/Register.vue");

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Login",
    component: Login
  },
  {
    path: "/register",
    name: "Register",
    component: Register
  }
];

const router = new VueRouter({
  mode: "history",
  routes
});

export default router;
