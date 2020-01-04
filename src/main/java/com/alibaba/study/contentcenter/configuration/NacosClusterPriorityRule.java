package com.alibaba.study.contentcenter.configuration;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;

import java.util.List;
import java.util.ArrayList;

/**
 * @Author: wjy
 * @Date: 2020/1/4 17:11
 */
@Slf4j
public class NacosClusterPriorityRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        //0 获取自身服务的clusetr-name
        String clusterName = nacosDiscoveryProperties.getClusterName();
        //1  获取当前被调用服务的服务名称 以及 所有实例对象
        BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
        String reqName = loadBalancer.getName();
        NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
        try {
            // 通过namingService 获取这个服务名称下面的所有健康的实例
            List<Instance> instances = namingService.selectInstances(reqName, true);
            // 2 过滤出相同集群下面的实例
            List<Instance> filteredInstances = new ArrayList<Instance>();
            for(Instance instance : instances){
                if (clusterName.equals(instance.getClusterName())){
                    filteredInstances.add(instance);
                }
            }
            // 3 判断filteredInstance 是否为空
            List<Instance> choosenInstances = new ArrayList<Instance>();
            if (filteredInstances.size() <= 0){
                choosenInstances = instances;
                log.warn("发生了跨集群的调用 server_name={}, origin_cluster_name={}",
                        reqName,
                        clusterName);
            }
            else{
                choosenInstances = filteredInstances;
            }
            //4 基于权重的负载均衡算法 返回一个实例 找到nacos 负载均衡算法
            Instance instance = ExtendBalancer.getHostByWeighted(choosenInstances);
            log.info("选择的端口 {}, instance={}", instance.getPort(), instance);
            return new NacosServer(instance);

        } catch (NacosException e) {
            log.error("exception happens {}", e);
            return null;
        }

    }
}
class ExtendBalancer extends Balancer {
    public static Instance getHostByWeighted(List<Instance> hosts){
        return getHostByRandomWeight(hosts);
    }
}
