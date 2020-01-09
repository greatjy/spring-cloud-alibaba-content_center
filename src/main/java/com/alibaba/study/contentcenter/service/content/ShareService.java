package com.alibaba.study.contentcenter.service.content;

import com.alibaba.study.contentcenter.dao.share.ShareMapper;
import com.alibaba.study.contentcenter.domain.dto.content.ShareAuditDTO;
import com.alibaba.study.contentcenter.domain.dto.content.ShareDTO;
import com.alibaba.study.contentcenter.domain.dto.user.UserDTO;
import com.alibaba.study.contentcenter.domain.entity.share.Share;
import com.alibaba.study.contentcenter.feignclient.UserCenterFeignClient;
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
import java.util.Objects;
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
    private final UserCenterFeignClient userCenterFeignClient;

    public ShareDTO findShareById(Integer id){
        Share share =  shareMapper.selectByPrimaryKey(id);
        Integer userId = share.getUserId();
        // 使用feign 声明式http请求 自动请求和自动转换成指定的对象  只需要定义一个接口，声明一个方法
        UserDTO userDTO = this.userCenterFeignClient.findById(id);
        //消息的装配  封装一个shareDTO 用来返回所有需要的字段内容
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());
        return shareDTO;
    }

    public Share auditById(Integer id, ShareAuditDTO auditDTO) {
        // 查询id对应的share是否存在，以及其对应的审核状态是否是not_yet
        Share share = this.shareMapper.selectByPrimaryKey(id);
        if(share == null ){
            throw new IllegalArgumentException("参数非法，该share不存在!");
        }
        if(!Objects.equals("NOT_YET)", share.getAuditStatus())){
            throw new IllegalArgumentException("该share已经经过审核");
        }
        //审核资源，pass/reject 将前端传过来的dto的审核值赋给share 更新share对象
        share.setAuditStatus(auditDTO.getAuditStatusEnum().toString());
        this.shareMapper.updateByPrimaryKey(share);

        // pass给作者添加积分。 需要调用用户中心
        // 可以使用feignclient 来更新user的属性 如果耗时，用户体验不好。可以将这个操作当作一个异步的操作
        // 有效缩短响应时间

        return share;
    }
}
