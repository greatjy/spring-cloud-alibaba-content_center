package com.alibaba.study.contentcenter.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wjy
 * @Date: 2020/1/5 17:31
 */
@Configuration
public class UserCenterFeignConfiguration {

    @Bean
    public Logger.Level setLevel(){
        return Logger.Level.FULL;
    }
}
