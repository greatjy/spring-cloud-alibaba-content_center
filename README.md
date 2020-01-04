# spring-cloud-alibaba-content_center
springcloudalibaba 项目微信小程序 中的用户中心微服务
## 目前完成了第一个版本
0. 对内容中心微服务进行数据建模，逻辑划分，编写API文档 
1. 整合了spring mvc，通用mapper代替原生mybatis
2. 使用了lombok
3. 微服务间通信使用的是RestTemplate的getObject API 其中内容中心是服务调用者 用户中心是服务提供者
4. 整合了springcloud 和 springcloudalibaba 
5. 整合了 Nacos 服务发现 
6. 整合了 Ribbon 客户端侧负载均衡，并且扩展了两种方式负载均衡：按照nacos权重和优先调用统一cluster
