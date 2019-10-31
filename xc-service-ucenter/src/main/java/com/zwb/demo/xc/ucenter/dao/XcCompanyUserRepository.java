package com.zwb.demo.xc.ucenter.dao;

import com.zwb.demo.xc.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/** Created by zwb on 2019/10/31 11:54 */
public interface XcCompanyUserRepository extends JpaRepository<XcCompanyUser, String> {
    // 根据用户id查询所属企业id
    XcCompanyUser findByUserId(String userId);
}
