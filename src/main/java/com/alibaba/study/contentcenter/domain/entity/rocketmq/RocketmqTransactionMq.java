package com.alibaba.study.contentcenter.domain.entity.rocketmq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "rocketmq_transaction_mq")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RocketmqTransactionMq {
    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 事务id
     */
    @Column(name = "transaction_id")
    private String transactionId;

    /**
     * 日志
     */
    private String log;
}