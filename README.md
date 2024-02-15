# xuetang
**学堂在线慕课平台**

项目简介：基于最新的技术选型（Java 17 + Spring Boot 3 + Spring Cloud 2022），仿照学堂在线完成的一个分布式微服务项目，提供线上学习慕课以及教师发布课程。

技术架构：Spring Boot、Spring Cloud、Spring Security、MyBatis-Plus、Redis 等中间件

项目内容：

- 使用 Nacos、Gateway 等组件形成分布式架构，利用 OpenFeign 进行远程调用
- 使用 Spring Security 框架，整合 JWT 实现用户认证授权以及单点登录
- 使用分布式文件系统 Minio 来存储课程视频、课程图片等文件，对于大视频文件的上传使用了断点续传的策略
- 对于课程搜索功能，使用 Elasticsearch 实现全文检索
- 对于常用的课程信息，使用 Redis 进行缓存，提高查询速度
- 整体采用前后端分离的架构，但对于一些已发布的课程，其页面内容基本固定不变，所以对于已发布课程的页面使用了模板引擎技术生成静态页面
- 对于一些需要后台完成的耗时操作，例如上述根据发布课程信息来生成静态页面的任务，使用了 RabbitMQ 消息中间件来通知另一个微服务异步执行
