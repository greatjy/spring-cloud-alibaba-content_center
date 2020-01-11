package com.alibaba.study.contentcenter.domain.messageing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wjy
 * @Date: 2020/1/11 18:15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddBonusMsgDTO {
    private Integer userId;
    private Integer bonus;
}
