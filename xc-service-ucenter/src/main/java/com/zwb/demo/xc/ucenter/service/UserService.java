package com.zwb.demo.xc.ucenter.service;

import com.zwb.demo.xc.domain.ucenter.XcCompanyUser;
import com.zwb.demo.xc.domain.ucenter.XcUser;
import com.zwb.demo.xc.domain.ucenter.ext.XcUserExt;
import com.zwb.demo.xc.ucenter.dao.XcCompanyUserRepository;
import com.zwb.demo.xc.ucenter.dao.XcUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Created by zwb on 2019/10/31 11:54 */
@Service
public class UserService {
    @Autowired XcUserRepository xcUserRepository;
    @Autowired XcCompanyUserRepository xcCompanyUserRepository;

    /**
     * 根据账号查询用户信息
     *
     * @param username
     * @return
     */
    public XcUser findXcUserByUsername(String username) {
        return xcUserRepository.findXcUserByUsername(username);
    }

    /**
     * 根虎账号查询用户信息，返回扩展信息
     *
     * @param username
     * @return
     */
    public XcUserExt getUserext(String username) {
        XcUser xcUser = this.findXcUserByUsername(username);
        if (xcUser == null) {
            return null;
        }
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser, xcUserExt);
        // 用户id
        String userId = xcUserExt.getId();
        XcCompanyUser xcCompanyUser = xcCompanyUserRepository.findByUserId(userId);
        if (xcCompanyUser != null) {
            String companyId = xcCompanyUser.getCompanyId();
            xcUserExt.setCompanyId(companyId);
        }
        return xcUserExt;
    }
}
