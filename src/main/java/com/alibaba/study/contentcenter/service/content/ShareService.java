package com.alibaba.study.contentcenter.service.content;

import com.alibaba.study.contentcenter.dao.share.ShareMapper;
import com.alibaba.study.contentcenter.domain.dto.content.ShareDTO;
import com.alibaba.study.contentcenter.domain.dto.user.UserDTO;
import com.alibaba.study.contentcenter.domain.entity.share.Share;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: wjy
 * @Date: 2020/1/2 17:02
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareService {

    private final ShareMapper shareMapper;
    @Autowired
    private final RestTemplate restTemplate;

    public ShareDTO findShareById(Integer id){
        Share share =  shareMapper.selectByPrimaryKey(id);
        Integer userId = share.getUserId();
        // 通过RestTemplate 发送http请求 实现异步调用
        // 将http返回的响应转成userDTO类
        UserDTO userDTO = this.restTemplate.getForObject(
                "http://localhost:8080/users/{id}",
                UserDTO.class, userId);
        //消息的装配  封装一个shareDTO 用来返回所有需要的字段内容
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());
        return shareDTO;
    }

}
