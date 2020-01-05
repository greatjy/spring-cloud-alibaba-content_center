package com.alibaba.study.contentcenter.feignclient;

import com.alibaba.study.contentcenter.configuration.UserCenterFeignConfiguration;
import com.alibaba.study.contentcenter.domain.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: wjy
 * @Date: 2020/1/4 22:02
 */
//@FeignClient(name = "user-center", configuration = UserCenterFeignConfiguration.class)
@FeignClient(name = "user-center")
public interface UserCenterFeignClient {
    /**
     *  feign 会将访问findById方法的时候 创建一个http请求：http://user-center/users/{id} 并将返回
     *  的结果转成UserDTO类型。
     * @param id
     * @return
     */
    @GetMapping("/users/{id}")
    UserDTO findById(@PathVariable(value = "id") Integer id);
}
