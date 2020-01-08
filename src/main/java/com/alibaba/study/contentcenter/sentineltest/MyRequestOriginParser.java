package com.alibaba.study.contentcenter.sentineltest;

import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: wjy
 * @Date: 2020/1/8 17:24
 */

public class MyRequestOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {

        //从request中接收origin参数，并返回。如果没有orgin参数就抛出异常
        String origin = httpServletRequest.getParameter("origin");
        if(StringUtils.isBlank(origin)){
            throw new IllegalArgumentException("origin must be specified");
        }
        return origin;
    }
}
