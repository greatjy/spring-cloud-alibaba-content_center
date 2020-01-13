package com.alibaba.study.contentcenter.service.content;

import com.alibaba.study.contentcenter.dao.rocketmq.RocketmqTransactionMqMapper;
import com.alibaba.study.contentcenter.dao.share.ShareMapper;
import com.alibaba.study.contentcenter.domain.dto.content.ShareAuditDTO;
import com.alibaba.study.contentcenter.domain.dto.content.ShareDTO;
import com.alibaba.study.contentcenter.domain.dto.user.UserDTO;
import com.alibaba.study.contentcenter.domain.entity.rocketmq.RocketmqTransactionMq;
import com.alibaba.study.contentcenter.domain.entity.share.Share;
import com.alibaba.study.contentcenter.domain.enums.AuditStatusEnum;
import com.alibaba.study.contentcenter.domain.messageing.UserAddBonusMsgDTO;
import com.alibaba.study.contentcenter.feignclient.UserCenterFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
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
    private final RocketMQTemplate rocketMQTemplate;
    private final RocketmqTransactionMqMapper rocketmqTransactionMqMapper;

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

    public Share auditById(Integer shareId, ShareAuditDTO auditDTO) {
        // 查询id对应的share是否存在，以及其对应的审核状态是否是not_yet
        Share share = this.shareMapper.selectByPrimaryKey(shareId);
        if(share == null ){
            throw new IllegalArgumentException("参数非法，该share不存在!");
        }
        if(!Objects.equals("NOT_YET", share.getAuditStatus())){
            throw new IllegalArgumentException("该share已经经过审核");
        }

        // 审核资源，pass/reject 将前端传过来的dto的审核值赋给share 更新share对象
        // auditByIdInDB(id,auditDTO); 改成使用事务接口的方式执行本地事务
        // auditByIdWit(shareId, auditDTO);

        //如果审核结果是pass的话，就进行事务操作 为用户加上积分。
        if(AuditStatusEnum.PASS.equals(auditDTO.getAuditStatusEnum())){
        String transactionId = UUID.randomUUID().toString();
                this.rocketMQTemplate.sendMessageInTransaction(
                        "tx_group",
                        "addBonus",
                        MessageBuilder.withPayload(
                                UserAddBonusMsgDTO.
                                        builder().
                                        userId(share.getUserId()).
                                        bonus(50).build()).
                                // header 也有妙用
                                setHeader("transaction_id",transactionId).
                                setHeader("share_id", shareId).
                                build(),
                        auditDTO);}
                        else{
            //处理结果是reject，只进行更改数据库操作，不再进行rocketmq通信操作
            this.auditByIdInDB(shareId, auditDTO);
        }

        return share;
    }

    public void auditByIdInDB (Integer id, ShareAuditDTO shareAuditDTO){
        Share share = Share.builder().
                id(id).
                auditStatus(shareAuditDTO.getAuditStatusEnum().toString()).
                reason(shareAuditDTO.getReason())
                .build();
        shareMapper.updateByPrimaryKeySelective(share);
    }

    @Transactional(rollbackFor = Exception.class)
    public void auditByIdWithMQLog(Integer id, ShareAuditDTO shareAuditDTO, String transactionId) {
        this.auditByIdInDB(id, shareAuditDTO);
        // 记录日志 注入日志表mapper 完成注入
        this.rocketmqTransactionMqMapper.insertSelective(
                RocketmqTransactionMq.builder()
                        .transactionId(transactionId)
                        .log("审核分享，目的是回查有没有数据")
                        .build()
        );
    }
}
