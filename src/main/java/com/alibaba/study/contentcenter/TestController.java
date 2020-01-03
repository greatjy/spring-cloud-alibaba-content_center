package com.alibaba.study.contentcenter;

import com.alibaba.study.contentcenter.dao.share.ShareMapper;
import com.alibaba.study.contentcenter.domain.entity.share.Share;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Author: wjy
 * @Date: 2019/12/29 21:59
 */
@RestController
public class TestController {

    @Autowired
    /**
     *     this autowired warning is not correct
     */
    private ShareMapper shareMapper;

    @Autowired
    private DiscoveryClient discoveryClient;

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

}
