package com.alibaba.study.contentcenter.dao.share;

import com.alibaba.study.contentcenter.domain.entity.share.Share;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
@Component
public interface ShareMapper extends Mapper<Share> {
}