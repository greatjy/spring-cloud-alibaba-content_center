package com.alibaba.study.contentcenter.sentineltest;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: wjy
 * @Date: 2020/1/8 16:34
 */
@Component
public class MyUrlBlockHandler implements UrlBlockHandler {
    @Override
    public void blocked(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws IOException {
        ErrMessage msg = null;
        if (e instanceof DegradeException) {
            //降级异常 根据不同的分支设置response返回不同的结果
            msg = ErrMessage.builder().status(100).message("降级异常").build();
        } else if (e instanceof FlowException) {
            //流控异常
            msg = ErrMessage.builder().status(101).message("流控异常").build();
        } else if (e instanceof ParamFlowException) {
            //参数热点流控异常
            msg = ErrMessage.builder().status(102).message("参数热点异常").build();
        } else if (e instanceof SystemBlockException) {
            //系统规则异常
            msg = ErrMessage.builder().status(103).message("系统规则异常").build();
        } else if (e instanceof AuthorityException) {
            // 授权异常
            msg = ErrMessage.builder().status(104).message("授权异常").build();
        }
        httpServletResponse.setStatus(500);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");

        //返回的消息体，使用springmvc 带的jackson来继续宁返回 writeValue方法
        new ObjectMapper().writeValue(httpServletResponse.getWriter(), msg);
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ErrMessage {
    //状态码
    private Integer status;
    //提示消息
    private String message;
}