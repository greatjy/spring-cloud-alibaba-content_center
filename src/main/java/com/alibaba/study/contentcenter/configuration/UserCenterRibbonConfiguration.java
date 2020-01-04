package com.alibaba.study.contentcenter.configuration;

import com.alibaba.study.ribbonconfiguration.RibbonConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wjy
 * @Date: 2020/1/3 22:44
 */
@Configuration
@RibbonClient(name = "user-center", configuration = RibbonConfiguration.class)
public class UserCenterRibbonConfiguration {

}
