package com.alibaba.study.contentcenter.rocketmq;

import com.alibaba.study.contentcenter.dao.rocketmq.RocketmqTransactionMqMapper;
import com.alibaba.study.contentcenter.domain.dto.content.ShareAuditDTO;
import com.alibaba.study.contentcenter.domain.entity.rocketmq.RocketmqTransactionMq;
import com.alibaba.study.contentcenter.service.content.ShareService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * @Author: wjy
 * @Date: 2020/1/13 12:00
 */
@RocketMQTransactionListener(txProducerGroup = "tx_group")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddBonusTransactionListener implements RocketMQLocalTransactionListener {

    private final ShareService shareService;
    private final RocketmqTransactionMqMapper rocketmqTransactionMqMapper;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object arg) {
        MessageHeaders headers = message.getHeaders();

        // 后面的内容就是一个string内容，所以需要将其headers也设置成 RocketMQHeaders.TRANSACTION_ID
        //将header的内容获得到
        // 注入service执行本地事务的transaction方法
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        Integer shareId = Integer.parseInt((String) (headers.get("share_id")));

        // 处理本地事务 需要使用try-catch包装  正常执行否则异常
        try {
            this.shareService.auditByIdWithMQLog(shareId, (ShareAuditDTO) (arg), transactionId);
            // 回查 是因为在这一步，可能本地事务已经完成，但是在这一步抛出了异常。所以mq不知道本地事务的状态。
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;

        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        //需要本地事务执行过程中记录执行日志 （这些操作是@Transactional的）
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        Integer shareId = Integer.parseInt((String) (headers.get("share_id")));

        // 查询日志表中是否有数据
        RocketmqTransactionMq transactionLog = this.rocketmqTransactionMqMapper.selectOne(RocketmqTransactionMq.builder().transactionId(transactionId).build());
        if(transactionLog != null){
            //transactionLog 不是空，表示可以提交
            return RocketMQLocalTransactionState.COMMIT;
        } else{
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }
}
