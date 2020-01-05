package com.alibaba.study.contentcenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author: wjy
 * @Date: 2020/1/5 22:18
 */
@FeignClient(name = "baidu", url = "www.baidu.com")
public interface TestBaiduFeignClient {

    @GetMapping("")
    String testBaidu();
}
