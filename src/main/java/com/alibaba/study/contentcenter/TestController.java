package com.alibaba.study.contentcenter;

import com.alibaba.study.contentcenter.dao.share.ShareMapper;
import com.alibaba.study.contentcenter.domain.dto.user.UserDTO;
import com.alibaba.study.contentcenter.domain.entity.share.Share;
import com.alibaba.study.contentcenter.feignclient.TestBaiduFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

/**
 * @Author: wjy
 * @Date: 2019/12/29 21:59
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TestController {

    @Autowired
    /**
     *     this autowired warning is not correct
     */
    private ShareMapper shareMapper;

    @Autowired
    private DiscoveryClient discoveryClient;

    private final TestBaiduFeignClient testBaiduFeignClient;

    private final RestTemplate restTemplate;

    private final Source source;


    @GetMapping("testInsert")
    public List<Share> testInsert() {
        // 1. 新建Mapper对象，并测试插入功能
        Share share = new Share();
        share.setCreateTime(new Date());
        share.setUpdateTime(new Date());
        share.setTitle("test_title");
        share.setAuthor("wjy");
        share.setPrice(10);
        this.shareMapper.insertSelective(share);
        // 2. 测试查询功能；查询数据库当前所有share
        List<Share> shares = this.shareMapper.selectAll();
        return shares;
    }

    @GetMapping("test2")
    public List<ServiceInstance> gettingStarted(){
        return this.discoveryClient.getInstances("user-center");
    }

    @GetMapping("test3")
    public List<String> test_services(){
        return this.discoveryClient.getServices();
    }

    @GetMapping("testBaidu")
    public String testBaidu(){
        return this.testBaiduFeignClient.testBaidu();
    }

    @GetMapping("test/{userid}")
    public UserDTO test(@PathVariable("userid") Integer userId) {
        log.warn("restTemplate {}",this.restTemplate);
        UserDTO userDTO = this.restTemplate.getForObject("http://user-center/users/{userId}", UserDTO.class, userId);
        return userDTO;
    }


//    @GetMapping("teststream")
//    public String testStream(){
//        this.source.output().send(MessageBuilder.withPayload("消息体").build()
//        );
//        return "success";
//    }



}
