package com.zwb.demo.xc.ucenter.dao;

import com.zwb.demo.xc.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/** Created by zwb on 2019/10/31 11:53 */
public interface XcUserRepository extends JpaRepository<XcUser, String> {
    XcUser findXcUserByUsername(String username);
}
