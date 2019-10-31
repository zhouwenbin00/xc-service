package com.zwb.demo.xc.ucenter.controller;

import com.zwb.demo.xc.api.ucenter.UcenterControllerApi;
import com.zwb.demo.xc.domain.ucenter.ext.XcUserExt;
import com.zwb.demo.xc.ucenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Created by zwb on 2019/10/31 11:53 */
@RestController
@RequestMapping("/ucenter")
public class UcenterController implements UcenterControllerApi {
    @Autowired UserService userService;

    @Override
    @GetMapping("/getuserext")
    public XcUserExt getUserext(String username) {
        return userService.getUserext(username);
    }
}
