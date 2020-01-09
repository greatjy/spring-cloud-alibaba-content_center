package com.alibaba.study.contentcenter.domain.dto.content;

import com.alibaba.study.contentcenter.domain.enums.AuditStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wjy
 * @Date: 2020/1/9 21:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareAuditDTO {
    /**
     *   审核状态
     */

    private AuditStatusEnum auditStatusEnum;
    /**
     * 审核原因 通过ornot通过原因
     */
    private String reason;
}
