# springboot-single
Spring Boot 单体项目架构搭建
CSDN：[加了白糖的老干妈](https://blog.csdn.net/qq_21358931/article/details/87877984)    
[TOC]

## 架构介绍
此次搭建的架构是面向前后端分离开发，纯后台服务，未集成前端模板内容。
## 技术栈

- JDK 1.8
- Spring Boot 2.1.3
- Mybatis 3.5.6
- PageHelper 4.2.1
- Redis
- Swagger 2.9.2
- Spring Cloud Stream(RabbitMQ) Greenwich.RELEASE

## 技术详情
此次架构搭建是以Spring Boot 2.1.3最新版为基础,集成的内容有:

1.ORM框架Mybatis,手写了Mybatis插件的SQL拦截器用于打印SQL(PS:优于默认配置)  
2.集成了MySQL多数据源配置  
3.分页插件PageHelper,封装了PageHelper的若干内容,分页查询更为便利。  
4.集成了Redis并封装了操作Redis的工具方法,提供两种存储String和Hash。  
5.集成Swagger,便于生成API文档和接口测试  
6.封装了统一异常管理,封装了自定义异常，结合国际化Message使用,统一了接口返回参数,封装了@Validated返回参数  
7.集成了Spring Cloud Stream消息驱动用于操作RabbitMQ    
8.AOP封装了请求监听，用于打印请求生命周期的信息
