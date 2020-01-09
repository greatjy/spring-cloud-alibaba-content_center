package com.alibaba.study.contentcenter.controller.share;

import com.alibaba.study.contentcenter.domain.dto.content.ShareAuditDTO;
import com.alibaba.study.contentcenter.domain.entity.share.Share;
import com.alibaba.study.contentcenter.service.content.ShareService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wjy
 * @Date: 2020/1/9 21:41
 */
@RestController
@RequestMapping("/admin/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareAdminController {

    private final ShareService shareService;

    public Share auditById(@PathVariable Integer id, @RequestBody ShareAuditDTO auditDTO){
        // toDo 认证授权
        return this.shareService.auditById(id, auditDTO);

    }

}
