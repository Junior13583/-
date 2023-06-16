# webStarter

#### 介绍
该项目做为web模板，集成了接口监控服务，同时拥有全局异常处理，对接口响应数据进行了封装等一系列常用功能。为后续开发缩减了工作量

#### 环境

- java 1.8
- SpringBoot 2.3.6.RELEASE
- LomBok 1.18.16 (需要lombok插件)
- validator 6.1.6.Final
- mybatis 2.2.0
- pagehelper-spring-boot-starter 1.4.3
- mapstruct 1.4.2.Final
- mapstruct-processor 1.4.2.Final
- spring-cloud-starter-alibaba-sentinel 2021.1

#### sentinel 启动方式
 > java -Dserver.port=3333 -jar sentinel-dashboard-1.8.5.jar

#### 实用功能

1. 接口监控服务，记录接口访问时长、参数信息以及错误情况
2. 接口返回数据封装，统一的数据格式
3. 全局异常处理，捕获全局异常，封装错误信息
   - 后续需要可添加自定义异常
4. 异步任务，以及异步任务监控
5. 参数校验，以及校验失败异常统一捕获
   - POST 请求参数校验
     1. 数据格式校验是否符合序列化
     2. 参数封装成实体类，并对其中字段进行校验，出现异常捕获统一返回
     3. 分组校验实体类字段，出现异常捕获统一返回
     4. 增加对集合进行校验的 ValidationList 类
   - GET 请求参数校验，对每个参数进行校验，出现异常捕获统一返回
   - 自定义校验
6. 引入 mybatis 操作 mysql 增删改查
   - mybatis 添加分页查询
7. 实体类重构，添加 entity 类和 DTO 类相互转换
8. 引入 sentinel 对接口进行流量控制

#### 包结构说明

1. component包，放一些自定的组件
    - aop子包，放一些自定义切面监控类，例如接口监控和异步任务监控在这个里面。后续可以在此添加所需要的内容。
    - asyncTask子包，放一些异步任务，后续的异步任务都可以放在这里。
2. config包，放一些配置类
    - 线程池的一些相关配置
    - 后面需要配置类也在下面添加
3. controller包，控制层。
4. dto包，传输层数据类放在此包下面。
5. entity包，数据库映射实体类。
6. Handler包
    - exceptHandler包
      1. customException子包，存放自定义异常
      2. 全局异常拦截处理类
    - sentinelHandler包
      1. 自定义限流以及熔断后触发兜底方法。
      2. 自定义代码异常回调方法。
7. mapper包，数据库查询接口定义在此包下面。
8. mapStruct包，DO、DTO、VO等类相互转换映射再此包
9. service包，业务层
10. validate包，
    - customValidation子包，自定义校验注解
    - group子包，分组校验
    - 自定义集合校验
11. vo包，视图层类