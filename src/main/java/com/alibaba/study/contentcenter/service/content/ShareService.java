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

    public ShareDTO findShareById(Integer id){
        Share share =  shareMapper.selectByPrimaryKey(id);
        Integer userId = share.getUserId();

        String uri ="http://user-center/users/{id}";
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
