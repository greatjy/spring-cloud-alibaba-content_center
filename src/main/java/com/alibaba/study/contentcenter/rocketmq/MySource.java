package com.alibaba.study.contentcenter.rocketmq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @Author: wjy
 * @Date: 2020/1/15 0:01
 */
public interface MySource {
    String MY_OUTPUT = "my-output";

    @Output(MY_OUTPUT)
    MessageChannel  output();

}
