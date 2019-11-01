package com.zwb.demo.xc.ucenter.dao;

import com.zwb.demo.xc.domain.ucenter.XcMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/** Created by zwb on 2019/11/1 13:45 */
@Mapper
public interface XcMenuMapper {

    // 根据用户id查询用户权限
    public List<XcMenu> selectPermissionByUserId(String userid);
}
