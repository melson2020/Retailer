module.exports = {
  root: true,
  env: {
    node: true
  },
  extends: ["plugin:vue/essential", "eslint:recommended", "@vue/prettier"],
  parserOptions: {
    parser: "babel-eslint"
  },
  rules: {
    "no-console": process.env.NODE_ENV === "production" ? "warn" : "off",
    "no-debugger": process.env.NODE_ENV === "production" ? "warn" : "off",
<<<<<<< HEAD
    "prettier/prettier":"off"
=======
    "prettier/prettier": "off"
>>>>>>> a20534c37d5d9d99db951293f79ecf09dffc9851
  }
};
