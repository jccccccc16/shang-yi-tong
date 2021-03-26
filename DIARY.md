## 1.swagger页面api不显示

在这个项目中，由于我的swagger配置类与controller不是写在同一个模块中，swagger在service-util模块中，controller在service模块中，所以我们需要在service的启动类上添加上`@ComponentScan("com.cjc.syt.common.config")`去扫描swagger配置类。

但是当启动项目时，swaggerui却看不到有api，经过修改`@ComponentScan("com.cjc.syt.common.config")`为`@ComponentScan("com.cjc")`，路径需要覆盖controller和swagger才行。

后来发现，当添加了componentScan之后，springboot不会自动扫描它目录下的组件了,

所以导致扫描不到controller