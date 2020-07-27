module.exports = {
  presets: [
    "@vue/app",  //这个是原始的 不一定重要 其他都是重要的,
    ["@babel/preset-env", { "modules": false,}]
  ],
  plugins: [
    // element官方教程
    [
      "component",
      {
        libraryName: "element-ui", 
        styleLibraryName: "theme-chalk"    
      }
    ]
  ]
};
