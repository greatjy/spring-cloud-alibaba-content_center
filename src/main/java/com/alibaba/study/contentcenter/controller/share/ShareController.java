package com.alibaba.study.contentcenter.controller.share;

import com.alibaba.study.contentcenter.domain.dto.content.ShareDTO;
import com.alibaba.study.contentcenter.service.content.ShareService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: wjy
 * @Date: 2020/1/2 20:17
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/shares")
public class ShareController {

    private final ShareService shareService;

    @GetMapping("/{id}")
    public ShareDTO findShareById(@PathVariable Integer id){
        return this.shareService.findShareById(id);
    }
}
