package com.alibaba.study.contentcenter.configuration;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;

/**
 * @Author: wjy
 * @Date: 2020/1/4 16:20
 */
@Slf4j
public class NacosWeightedRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    /**
    读取配置文件，留空即可。 其他的ribbon rule 很多也采取了留空的方式
     */
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    /**
     *  具体的负载均衡规则，返回一个Server类型。通过封装Instance返回。
     * @param o
     * @return
     */
    @Override
    public Server choose(Object o) {
        // 通过LoadBanlancer ribbon的入口 获得想要访问的微服务的名称 包含ribbon的各种属性
        BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
        log.info("lb = {}", loadBalancer);
        // 请求微服务的名称
        String reqServiceName = loadBalancer.getName();

        // 实现负载均衡算法  NacosClient实现了内置的负载均衡算法 需要注入NacosDiscoveryProperties
        // 通过namingServiceInstance 得到服务发现的相关API
        NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
        try {
            Instance instance  = namingService.selectOneHealthyInstance(reqServiceName);
            return new NacosServer(instance);
        } catch (NacosException e) {
           return null;
        }
    }
}
