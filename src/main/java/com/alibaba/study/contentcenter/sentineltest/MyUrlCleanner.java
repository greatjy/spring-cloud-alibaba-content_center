package com.alibaba.study.contentcenter.sentineltest;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlCleaner;
import org.apache.commons.lang.math.NumberUtils;

import java.util.Arrays;

/**
 * @Author: wjy
 * @Date: 2020/1/8 17:39
 */
public class MyUrlCleanner implements UrlCleaner {
    @Override
    public String clean(String originUrl) {
        String[] split = originUrl.split("/");
        return Arrays.stream(split).map(string -> {
            if(NumberUtils.isNumber(string)){
                return "{number}";
            }
            return string
        })
                .reduce((a,b) -> a + "/" + b)
                .orElse("");
    }
}
