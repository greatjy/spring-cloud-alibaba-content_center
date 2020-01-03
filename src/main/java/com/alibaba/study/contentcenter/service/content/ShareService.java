package com.alibaba.study.contentcenter.service.content;

import com.alibaba.study.contentcenter.dao.share.ShareMapper;
import com.alibaba.study.contentcenter.domain.dto.content.ShareDTO;
import com.alibaba.study.contentcenter.domain.dto.user.UserDTO;
import com.alibaba.study.contentcenter.domain.entity.share.Share;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author: wjy
 * @Date: 2020/1/2 17:02
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ShareService {

    private final ShareMapper shareMapper;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    public ShareDTO findShareById(Integer id){
        Share share =  shareMapper.selectByPrimaryKey(id);
        Integer userId = share.getUserId();

        // 通过discoveryClient 可以得到user-center的所有实例信息
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        // instance中的uri正式我们需要的发送http请求的地址 然后拼接上查询userDTO的接口
        //再这里我们实现随机算法进行负载均衡
        int size = instances.size();
        int index = 0;
        if(size <= 1){
            index = 0;
        }
        else{
            index = ThreadLocalRandom.current().nextInt(size);
        }
        String uri =instances.get(index).getUri().toString()+"/users/{id}";
        log.info("we will request this uri {}", uri);

        // 通过RestTemplate 发送http请求 实现异步调用
        // 将http返回的响应转成userDTO类
        UserDTO userDTO = this.restTemplate.getForObject(uri,
                UserDTO.class, userId);
        //消息的装配  封装一个shareDTO 用来返回所有需要的字段内容
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());
        return shareDTO;
    }

}
