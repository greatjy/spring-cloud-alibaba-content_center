package com.alibaba.study.contentcenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = "com.alibaba.study")
@SpringBootTest
class UserCenterApplicationTests {

    @Test
    void contextLoads() {
    }

}
