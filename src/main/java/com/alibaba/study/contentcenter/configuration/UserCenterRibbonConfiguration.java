package com.alibaba.study.contentcenter.configuration;

import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wjy
 * @Date: 2020/1/3 22:44
 */
@Configuration
@RibbonClients(defaultConfiguration =  NacosWeightedRule.class)
public class UserCenterRibbonConfiguration {

}
