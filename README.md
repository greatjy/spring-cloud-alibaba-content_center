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
7. 整合了http客户端feign，重构了RestTemplate代码逻辑 
使用阿里云ahas 进行生产级别的服务容错
8. 引入了MQ组件进行消息通信（RocketMq）用户中心和内容中心之间的异步api通信调用。mq主要使用的场景：1.消息异步处理，解决主流程中的耗时操作，提高用户体验。2.流量削峰填谷。防止秒杀活动流量洪峰将应用打死。丢弃掉超过阈值的请求。3.解耦，微服务之间的消息通信存在mq中，即使一方挂了，对另一方没有影响。提高可用性，降低耦合。
